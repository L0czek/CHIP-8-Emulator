import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

public class VirtualMachineState {
    public static class VMException extends Exception {
        public String reason;
        VirtualMachineState stateAtFault;
        public VMException(String reason, VirtualMachineState stateAtFault) {
            this.reason = reason;
            this.stateAtFault = stateAtFault;
        }
    }

    private int[] regs = null;
    private byte[] memory = null;

    private int delayTimerCounter = 0;
    private int soundTimerCounter = 0;

    private Stack<Integer> callStack = null;
    private int regI;
    private int ip;

    private int[][] screen = null;

    private int[] linens;

    private Disassembler disassembler;
    private Optional<Events.ViewForModel> view;

    private Keyboard keyboard;
    private Timer timer;

    public VirtualMachineState(Assembler.Assembled code, Disassembler disassembler, Optional<Events.ViewForModel> view) {
        regs = new int[16];
        memory = new byte[0x1000];
        callStack = new Stack<Integer>();
        screen = new int[32][64];
        regI = 0;
        ip = 0x200;
        linens = code.getLineNumbers();
        System.arraycopy(code.getByteCode(), 0, memory, 0x200, code.getByteCode().length);
        System.arraycopy(BuiltinSprites, 0, memory, 0, BuiltinSprites.length);
        this.disassembler = disassembler;
        this.view = view;
        keyboard = new Keyboard();
        timer = new Timer();
        final int timer_freq = 1000 / 60;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timerInterrupt();
            }
        }, timer_freq, timer_freq);
    }

    public int getReg(int n) {
        switch(n) {
            case 16: return getRegI();
            case 17: return getIp();
            default:
                return regs[n];
        }
    }
    public void setReg(int n, int value) {
        switch(n) {
            case 16:
                setRegI(value);
                break;
            case 17:
                setIp(value);
                break;
            default:
                regs[n] = value;
        }
    }

    public void setIp(int value) {
        ip = value;
    }

    public byte memoryGetByte(int address) {
        return memory[address];
    }

    public void memorySetByte(int address, byte value) {
        memory[address] = value;
    }

    public short memoryGetShort(int address) {
        return (short)(((memory[address]<<8) & 0xff00) | (memory[address+1] & 0xff));
    }

    public void memorySetShort(int address, short value) {
        memory[address] = (byte)(value >> 8);
        memory[address+1] = (byte)value;
    }

    public int getRegI() {
        return regI;
    }
    public int getIp() { return ip; }

    public void setRegI(int value) {
        regI = value;
    }
    public void callSubroutine(int address) {
        callStack.push(ip + 2);
        ip = address;
    }
    public void returnFromSubroutine() {
        if(!callStack.isEmpty()) {
            ip = callStack.peek();
            callStack.pop();
        }
    }
    public void jump(int address) {
        ip = address;
    }
    public void skipInstruction() {
        ip += 4;
    }
    public void nextInstruction() { ip += 2;}
    public void timerInterrupt() {
        if(delayTimerCounter > 0) {
            delayTimerCounter--;
        }
        if(soundTimerCounter > 0) {
            soundTimerCounter--;
            if(soundTimerCounter == 0) {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    public int getDelayTimerCounter() {
        return delayTimerCounter;
    }
    public void setDelayTimerCounter(int value) {
        delayTimerCounter = value;
    }
    public int getSoundTimerCounter() {
        return soundTimerCounter;
    }
    public void setSoundTimerCounter(int value) {
        soundTimerCounter = value;
    }
    public void clearScreen() {
        // TODO fire event
    }
    public int getPixel(int x, int y) {
        try {
            return screen[y][x];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return 0;
        }
    }
    public void setPixel(int x, int y, int value) {
        try {
            screen[y][x] = value;
        } catch (ArrayIndexOutOfBoundsException ignored) {

        }
    }
    public void updateScreen() {
        if(view.isPresent()) {
            Events.ViewForModel events = view.get();
            for(int x=0; x < 64; ++x) {
                for(int y=0; y < 32; ++y) {
                    events.sendSetPixelRGBEvent(x, y, getPixel(x, y));
                }
            }
        }
    }
    public int[][] getScreen() {
        return null;
    }
    public void executeInstruction() throws VMException {
        short opcode = memoryGetShort(ip);
        //System.out.println(String.format("executing %04X\n", opcode));
        Optional<Instruction> decoded = disassembler.decodeInstruction(opcode);
        if(!decoded.isPresent()) {
            throw new VMException("Cannot decode instruction", this);
        }
        decoded.get().execute(this);
    }

    public void keyPressed(KeyEvent keyEvent) {
        keyboard.keyPressed(keyEvent);
    }

    public void keyReleased(KeyEvent keyEvent) {
        keyboard.keyReleased(keyEvent);
    }

    public Optional<Integer> getKey() {
        return keyboard.getKeyPressed();
    }

    public boolean isKeyPressed(int key) {
        return keyboard.isKeyPressed(key);
    }

    public int getSpriteAddress(int n) {
        if(n < 16) {
            return n * 5;
        } else {
            return 0;
        }
    }

    public static byte[] BuiltinSprites = {
            (byte)0xF0, (byte)0x90, (byte)0x90, (byte)0x90, (byte)0xF0, // 0
            (byte)0x20, (byte)0x60, (byte)0x20, (byte)0x20, (byte)0x70, // 1
            (byte)0xF0, (byte)0x10, (byte)0xF0, (byte)0x80, (byte)0xF0, // 2
            (byte)0xF0, (byte)0x10, (byte)0xF0, (byte)0x10, (byte)0xF0, // 3
            (byte)0x90, (byte)0x90, (byte)0xF0, (byte)0x10, (byte)0x10, // 4
            (byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x10, (byte)0xF0, // 5
            (byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x90, (byte)0xF0, // 6
            (byte)0xF0, (byte)0x10, (byte)0x20, (byte)0x40, (byte)0x40, // 7
            (byte)0xF0, (byte)0x90, (byte)0xF0, (byte)0x90, (byte)0xF0, // 8
            (byte)0xF0, (byte)0x90, (byte)0xF0, (byte)0x10, (byte)0xF0, // 9
            (byte)0xF0, (byte)0x90, (byte)0xF0, (byte)0x90, (byte)0x90, // 10
            (byte)0xE0, (byte)0x90, (byte)0xE0, (byte)0x90, (byte)0xE0, // 11
            (byte)0xF0, (byte)0x80, (byte)0x80, (byte)0x80, (byte)0xF0, // 12
            (byte)0xE0, (byte)0x90, (byte)0x90, (byte)0x90, (byte)0xE0, // 13
            (byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x80, (byte)0xF0, // 14
            (byte)0xF0, (byte)0x80, (byte)0xF0, (byte)0x80, (byte)0x80, // 15
    };
}
