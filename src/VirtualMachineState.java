import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.Timer;
import java.util.Vector;

public class VirtualMachineState {
    private int[] regs = null;
    private byte[] memory = null;

    private int delayTimerCounter = 0;
    private int soundTimerCounter = 0;

    private Stack<Integer> callStack = null;
    private int regI;
    private int ip;

    private BufferedImage screen = null;

    public VirtualMachineState() {
        regs = new int[16];
        memory = new byte[0x1000];
        callStack = new Stack<Integer>();
        screen = new BufferedImage(64, 32, BufferedImage.TYPE_INT_RGB);
        regI = 0;
        ip = 0;
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
        return (short)(memory[address]<<8 | memory[address+1]);
    }

    public void memorySetShort(int address, short value) {
        memory[address] = (byte)(value >> 8);
        memory[address+1] = (byte)value;
    }

    public int getRegI() {
        return regI;
    }

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
        ip += 2;
    }
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
        return screen.getRGB(x, y);
    }
    public void setPixel(int x, int y, int value) {
        screen.setRGB(x, y, value);
    }
    public void updateScreen() {

    }
}
