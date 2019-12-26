import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * virtual machine state (registers, memory, stack etc.)
 */
public class VirtualMachineState {
    /**
     * exception throw on instruction execution error
     */
    public static class VMException extends Exception {
        public String reason;
        VirtualMachineState stateAtFault;

        /**
         * construct from reason and state at fault
         * @param reason error string
         * @param stateAtFault vm state at foult
         */
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

    /**
     * constructor from code, disassembler to decode instruction and event handler for view
     * @param code assembled code from editor
     * @param disassembler disassembler object from model
     * @param view event handlers from view to update screen and ui
     */
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

    /**
     * get register value
     * @param n register number
     * @return selected register value
     */
    public int getReg(int n) {
        switch(n) {
            case 16: return getRegI();
            case 17: return getIp();
            default:
                return regs[n];
        }
    }

    /**
     * set register value
     * @param n selected register
     * @param value new value
     */
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

    /**
     * set new ip value
     * @param value new ip value
     */
    public void setIp(int value) {
        ip = value;
    }

    /**
     * get byte from memory at address
     * @param address address of byte
     * @return byte from memory
     */
    public byte memoryGetByte(int address) {
        return memory[address];
    }

    /**
     * set byte in memory at address
     * @param address address of byte
     * @param value new byte value
     */
    public void memorySetByte(int address, byte value) {
        memory[address] = value;
    }

    /**
     * get short from memory at address
     * @param address address of short
     * @return short from memory
     */
    public short memoryGetShort(int address) {
        return (short)(((memory[address]<<8) & 0xff00) | (memory[address+1] & 0xff));
    }
    /**
     * set short in memory at address
     * @param address address of short
     * @param value new short value
     */
    public void memorySetShort(int address, short value) {
        memory[address] = (byte)(value >> 8);
        memory[address+1] = (byte)value;
    }

    /**
     * get reg I
     * @return I reg value
     */
    public int getRegI() {
        return regI;
    }

    /**
     * get reg ip
     * @return ip value
     */
    public int getIp() { return ip; }

    /**
     * set reg I
     * @param value new reg I value
     */
    public void setRegI(int value) {
        regI = value;
    }

    /**
     * call subroutine at address `address`
     * @param address address to be called
     */
    public void callSubroutine(int address) {
        callStack.push(ip + 2);
        ip = address;
    }

    /**
     * return from subroutine
     */
    public void returnFromSubroutine() {
        if(!callStack.isEmpty()) {
            ip = callStack.peek();
            callStack.pop();
        }
    }

    /**
     * jump at address
     * @param address address to jump
     */
    public void jump(int address) {
        ip = address;
    }

    /**
     * skip next instruction
     */
    public void skipInstruction() {
        ip += 4;
    }

    /**
     * go to next instruction
     */
    public void nextInstruction() { ip += 2;}

    /**
     * timer interrupt
     */
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

    /**
     * get delay timer value
     * @return delay timer counter value
     */
    public int getDelayTimerCounter() {
        return delayTimerCounter;
    }

    /**
     * set delay timer value
     * @param value value for delay timer to set
     */
    public void setDelayTimerCounter(int value) {
        delayTimerCounter = value;
    }

    /**
     * get sound timer value
     * @return sound timer counter value
     */
    public int getSoundTimerCounter() {
        return soundTimerCounter;
    }

    /**
     * set sound timer counter value
     * @param value value to be set as counter value
     */
    public void setSoundTimerCounter(int value) {
        soundTimerCounter = value;
    }

    /**
     * unused to be removed
     */
    public void clearScreen() {
        // TODO fire event
    }

    /**
     * get pixel value
     * @param x x coordinate
     * @param y y coordinate
     * @return RGB value of selected pixel
     */
    public int getPixel(int x, int y) {
        try {
            return screen[y][x];
        } catch (ArrayIndexOutOfBoundsException ignored) {
            return 0;
        }
    }

    /**
     * set pixel value
     * @param x x coordinate
     * @param y y coordinate
     * @param value new RGB value
     */
    public void setPixel(int x, int y, int value) {
        try {
            screen[y][x] = value;
        } catch (ArrayIndexOutOfBoundsException ignored) {

        }
    }

    /**
     * redraw screen
     */
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

    /**
     * unused [deprecated]
     * @return
     */
    public int[][] getScreen() {
        return null;
    }

    /**
     * execute instruction at ip
     * @throws VMException thrown on executing error
     */
    public void executeInstruction() throws VMException {
        short opcode = memoryGetShort(ip);
        //System.out.println(String.format("executing %04X\n", opcode));
        Optional<Instruction> decoded = disassembler.decodeInstruction(opcode);
        if(!decoded.isPresent()) {
            throw new VMException("Cannot decode instruction", this);
        }
        decoded.get().execute(this);
    }

    /**
     * key pressed event
     * @param keyEvent key from key event dispatcher
     */
    public void keyPressed(KeyEvent keyEvent) {
        keyboard.keyPressed(keyEvent);
    }
    /**
     * key released event
     * @param keyEvent key from key event dispatcher
     */
    public void keyReleased(KeyEvent keyEvent) {
        keyboard.keyReleased(keyEvent);
    }

    /**
     * get pressed key from keyboard
     * @return pressed key or empty
     */
    public Optional<Integer> getKey() {
        return keyboard.getKeyPressed();
    }

    /**
     * check if key is pressed
     * @param key key to be checked
     * @return true if key is pressed, false otherwise
     */
    public boolean isKeyPressed(int key) {
        return keyboard.isKeyPressed(key);
    }

    /**
     * get sprite address from memory
     * @param n spire number
     * @return address of sprite
     */
    public int getSpriteAddress(int n) {
        if(n < 16) {
            return n * 5;
        } else {
            return 0;
        }
    }

    /**
     * builtin sprites
     */
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
