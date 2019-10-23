public class InstructionSet {
    public static class Call extends InstructionTypes.Type_NNN implements Instruction {
        public Call(short opcode) {
            super(opcode);
        }

        public Call(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.callSubroutine(getValueNNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x0000;
        }

        public static String getMnemonic() {
            return "call";
        }
    }

    public static class Return extends InstructionTypes.Type_NoArg implements Instruction {
        @Override
        public void execute(VirtualMachineState state) {
            state.returnFromSubroutine();
        }

        @Override
        public String disassemble() {
            return String.format("%s\n", getMnemonic());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x00EE;
        }

        public static String getMnemonic() {
            return "ret";
        }
    }

    public static class Jump extends InstructionTypes.Type_NNN implements Instruction {
        public Jump(short opcode) {
            super(opcode);
        }

        public Jump(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.jump(getValueNNN());
        }

        @Override
        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x1000;
        }

        public static String getMnemonic() {
            return "jmp";
        }
    }

    public static class CallWordPtr extends InstructionTypes.Type_NNN implements Instruction {
        public CallWordPtr(short opcode) {
            super(opcode);
        }

        public CallWordPtr(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            var address = state.memoryGetShort(getValueNNN());
            state.callSubroutine(address);
        }

        @Override
        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x2000;
        }

        public static String getMnemonic() {
            return "callptr";
        }
    }

    public static class SkipEqualImm extends InstructionTypes.Type_XNN implements Instruction {
        public SkipEqualImm(short opcode) {
            super(opcode);
        }

        public SkipEqualImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            var reg = state.getReg(getValueX());
            if(reg == getValueNN()) {
                state.skipInstruction();
            }
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), getValueNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x3000;
        }

        public static String getMnemonic() {
            return "skeqi";
        }
    }

    public static class SkipNotEqualImm extends InstructionTypes.Type_XNN implements Instruction {
        public SkipNotEqualImm(short opcode) {
            super(opcode);
        }

        public SkipNotEqualImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            var reg = state.getReg(getValueX());
            if(reg != getValueNN()) {
                state.skipInstruction();
            }
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), getValueNN());
        }
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x4000;
        }

        public static String getMnemonic() {
            return "skneqi";
        }
    }

    public static class SkipEqualReg extends InstructionTypes.Type_XY implements Instruction {
        public SkipEqualReg(short opcode) {
            super(opcode);
        }

        public SkipEqualReg(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            int reg1 = state.getReg(getValueX());
            int reg2 = state.getReg(getValueY());
            if(reg1 == reg2) {
                state.skipInstruction();
            }
        }

        @Override
        public String disassemble() {
            return  String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x5000;
        }

        public static String getMnemonic() {
            return "skeqr";
        }
    }

    public static class LoadImm extends InstructionTypes.Type_XNN implements Instruction {
        public LoadImm(short opcode) {
            super(opcode);
        }

        public LoadImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), getValueNN());
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), getValueNN());
        }
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x6000;
        }

        public static String getMnemonic() {
            return "li";
        }
    }

    public static class AddImm extends InstructionTypes.Type_XNN implements Instruction {
        public AddImm(short opcode) {
            super(opcode);
        }

        public AddImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), (byte)(state.getReg(getValueX()) + getValueNN()));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), getValueNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x7000;
        }

        public static String getMnemonic() {
            return "addi";
        }
    }

    public static class Mov extends InstructionTypes.Type_XY implements Instruction {
        public Mov(short opcode) {
            super(opcode);
        }

        public Mov(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), state.getReg(getValueY()));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8000;
        }

        public static String getMnemonic() {
            return "mov";
        }
    }

    public static class Or extends InstructionTypes.Type_XY implements Instruction {
        public Or(short opcode) {
            super(opcode);
        }

        public Or(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), (byte)(state.getReg(getValueX()) | state.getReg(getValueY())));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8001;
        }

        public static String getMnemonic() {
            return "or";
        }
    }

    public static class And extends InstructionTypes.Type_XY implements Instruction {
        public And(short opcode) {
            super(opcode);
        }

        public And(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), (byte)(state.getReg(getValueX()) & state.getReg(getValueY())));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8002;
        }

        public static String getMnemonic() {
            return "and";
        }
    }

    public static class Xor extends InstructionTypes.Type_XY implements Instruction {
        public Xor(short opcode) {
            super(opcode);
        }

        public Xor(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), (state.getReg(getValueX()) ^ state.getReg(getValueY())));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8003;
        }

        public static String getMnemonic() {
            return "xor";
        }
    }

    public static class Add extends InstructionTypes.Type_XY implements Instruction {
        public Add(short opcode) {
            super(opcode);
        }

        public Add(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            int result = state.getReg(getValueX()) + state.getReg(getValueY());
            if(result > 0xff) {
                state.setReg(0x15, 1);
            }
            state.setReg(getValueX(), result & 0xff);
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8004;
        }

        public static String getMnemonic() {
            return "add";
        }

    }

    public static class Sub extends InstructionTypes.Type_XY implements Instruction {
        public Sub(short opcode) {
            super(opcode);
        }

        public Sub(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            int result = state.getReg(getValueX()) - state.getReg(getValueY());
            if(result < 0) {
                state.setReg(15, 1);
            }
            state.setReg(getValueX(), result & 0xff);
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8005;
        }

        public static String getMnemonic() {
            return "sub";
        }
    }

    public static class RShift1 extends InstructionTypes.Type_X implements Instruction {
        public RShift1(short opcode) {
            super(opcode);
        }

        public RShift1(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(15, state.getReg(getValueX()) & 1);
            state.setReg(getValueX(), state.getReg(getValueX()) >> 1);
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8006;
        }

        public static String getMnemonic() {
            return "shr1";
        }
    }

    public static class SubR extends InstructionTypes.Type_XY implements Instruction {
        public SubR(short opcode) {
            super(opcode);
        }

        public SubR(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            int result = state.getReg(getValueY()) - state.getReg(getValueX());
            if(result < 0) {
                state.setReg(15, 1);
            }
            state.setReg(getValueX(), result & 0xff);
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x8007;
        }

        public static String getMnemonic() {
            return "subr";
        }
    }

    public static class LShift1 extends InstructionTypes.Type_X implements Instruction {
        public LShift1(short opcode) {
            super(opcode);
        }

        public LShift1(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            int reg = state.getReg(getValueX());
            state.setReg(15, reg >> 7);
            state.setReg(getValueX(), (reg << 1) & 0xfe);
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x800E;
        }

        public static String getMnemonic() {
            return "shl1";
        }
    }

    public static class SkipNotEqualReg extends InstructionTypes.Type_XY implements Instruction {
        public SkipNotEqualReg(short opcode) {
            super(opcode);
        }

        public SkipNotEqualReg(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            if(state.getReg(getValueX()) != state.getReg(getValueY())) {
                state.skipInstruction();
            }
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }
        public static int getMask() {
            return 0x9000;
        }

        public static String getMnemonic() {
            return "skneqr";
        }
    }

    public static class LoadRegI extends InstructionTypes.Type_NNN implements Instruction {
        public LoadRegI(short opcode) {
            super(opcode);
        }

        public LoadRegI(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setRegI(getValueNNN());
        }

        @Override
        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xA000;
        }

        public static String getMnemonic() {
            return "loadI";
        }
    }

    public static class BreachRelv0 extends InstructionTypes.Type_NNN implements Instruction {
        public BreachRelv0(short opcode) {
            super(opcode);
        }

        public BreachRelv0(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.jump(state.getReg(0) + getValueNNN());
        }

        @Override
        public String disassemble() {
            return String.format("%s 0x%x\n", getMnemonic(), getValueNNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xB000;
        }

        public static String getMnemonic() {
            return "brelv0";
        }
    }

    public static class Rand extends InstructionTypes.Type_XNN implements Instruction {
        public Rand(short opcode) {
            super(opcode);
        }

        public Rand(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), (int)(Math.random() * 0x100) & getValueNN());
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), getValueNN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xC000;
        }

        public static String getMnemonic() {
            return "rand";
        }
    }

    public static class DisplayClear extends InstructionTypes.Type_NoArg implements Instruction {
        @Override
        public void execute(VirtualMachineState state) {
            state.clearScreen();
        }

        @Override
        public String disassemble() {
            return String.format("%s\n", getMnemonic());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0x00E0;
        }

        public static String getMnemonic() {
            return "clear";
        }
    }

    public static class GetDelayTimerCounter extends InstructionTypes.Type_X implements Instruction {
        public GetDelayTimerCounter(short opcode) {
            super(opcode);
        }

        public GetDelayTimerCounter(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), state.getDelayTimerCounter());
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xF007;
        }

        public static String getMnemonic() {
            return "gdtc";
        }
    }

    public static class SetDelayTimerCounter extends InstructionTypes.Type_X implements Instruction {
        public SetDelayTimerCounter(short opcode) {
            super(opcode);
        }

        public SetDelayTimerCounter(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setDelayTimerCounter(state.getReg(getValueX()));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xF015;
        }
        public static String getMnemonic() {
            return "sdtc";
        }
    }

    public static class SetSoundTimerCounter extends InstructionTypes.Type_X implements Instruction {
        public SetSoundTimerCounter(short opcode) {
            super(opcode);
        }

        public SetSoundTimerCounter(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setSoundTimerCounter(state.getReg(getValueX()));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xF018;
        }
        public static String getMnemonic() {
            return "sstc";
        }
    }

    public static class AddRegI extends InstructionTypes.Type_X implements Instruction {
        public AddRegI(short opcode) {
            super(opcode);
        }

        public AddRegI(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            state.setRegI(state.getRegI() + state.getReg(getValueX()));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xF01E;
        }
        public static String getMnemonic() {
            return "addI";
        }
    }

    public static class StoreBCD extends InstructionTypes.Type_X implements Instruction {
        public StoreBCD(short opcode) {
            super(opcode);
        }

        public StoreBCD(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            var value = state.getReg(getValueX());
            var address = state.getRegI();
            state.memorySetByte(address + 0, (byte)((value / 100) & 0xff));
            state.memorySetByte(address + 1, (byte)(((value / 10) % 10) & 0xff));
            state.memorySetByte(address + 2, (byte)((value % 10) & 0xff));
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xF033;
        }
        public static String getMnemonic() {
            return "sbcd";
        }
    }

    public static class RegDump extends InstructionTypes.Type_X implements Instruction {
        public RegDump(short opcode) {
            super(opcode);
        }

        public RegDump(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            var address = state.getRegI();
            var end = getValueX();
            for(int i=0; i < end; ++i) {
                state.memorySetByte(address + i, (byte)(0xff & state.getReg(i)));
            }
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xF055;
        }
        public static String getMnemonic() {
            return "regdump";
        }
    }

    public static class RegLoad extends InstructionTypes.Type_X implements Instruction {
        public RegLoad(short opcode) {
            super(opcode);
        }

        public RegLoad(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            var address = state.getRegI();
            var end = getValueX();
            for(int i=0; i < end; ++i) {
                state.setReg(i, state.memoryGetByte(address + i) & 0xff);
            }
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xF065;
        }
        public static String getMnemonic() {
            return "regload";
        }
    }

    public static class DrawSprite extends InstructionTypes.Type_XYN implements Instruction {
        public DrawSprite(short opcode) {
            super(opcode);
        }

        public DrawSprite(String[] assemblyArgs) {
            super(assemblyArgs);
        }

        @Override
        public void execute(VirtualMachineState state) {
            var x = state.getReg(getValueX());
            var y = state.getReg(getValueY());
            var n = getValueN();
            var address = state.getRegI();
            boolean flipped = false;
            for(int i=0; i < n; ++i) {
                var sprite = state.memoryGetByte(address + i);
                for(int j=0; j < 8; ++j) {
                    var bit = (sprite >> (7-j)) & 1;
                    if(bit != 0 && state.getPixel(x + j, y) == 0xffffff) {
                        flipped = true;
                    }
                    state.setPixel(x + j, y, state.getPixel(x + j, y) ^ 0xffffff);
                }
            }
            if(flipped) {
                state.setReg(15, 1);
            } else {
                state.setReg(15, 0);
            }
        }

        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d, 0x%X\n", getMnemonic(), getValueX(), getValueY(), getValueN());
        }

        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }

        public static int getMask() {
            return 0xD000;
        }
        public static String getMnemonic() {
            return "draw";
        }
    }
}
