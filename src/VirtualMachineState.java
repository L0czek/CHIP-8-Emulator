import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Stack;
import java.util.Timer;
import java.util.Vector;

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

    public VirtualMachineState(Assembler.Assembled code, Disassembler disassembler, Optional<Events.ViewForModel> view) {
        regs = new int[16];
        memory = new byte[0x1000];
        callStack = new Stack<Integer>();
        screen = new int[32][64];
        regI = 0;
        ip = 0x200;
        linens = code.getLineNumbers();
        System.arraycopy(code.getByteCode(), 0, memory, 0x200, code.getByteCode().length);
        this.disassembler = disassembler;
        this.view = view;
    }

    public int getReg(int n) {
        return regs[n];
    }
    public void setReg(int n, int value) {
        regs[n] = value;
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
        callStack.push(ip);
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
            delayTimerCounter--;
        } else {
            //TODO play sound
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
        System.out.println(String.format("executing %04X\n", opcode));
        Optional<Instruction> decoded = disassembler.decodeInstruction(opcode);
        if(!decoded.isPresent()) {
            throw new VMException("Cannot decode instruction", this);
        }
        decoded.get().execute(this);
    }
}
