import java.util.Optional;

/**
 * class being placeholder for class defining instruction
 */
public class InstructionSet {


    /**
     * class defining Call instruction
     */
    public static class Call extends InstructionTypes.Type_NNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Call(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Call(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.callSubroutine(getValueNNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x0000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "call";
        }

        public int nextIp() {
            return getValueNNN();
        }
    }


    /**
     * class defining DisplayClear instruction
     */
    public static class DisplayClear extends InstructionTypes.Type_NoArg implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public DisplayClear(short opcode) {

        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public DisplayClear(String[] assemblyArgs) {

        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.clearScreen();
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s\n", getMnemonic());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x00E0;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "clear";
        }
    }



    /**
     * class defining Return instruction
     */
    public static class Return extends InstructionTypes.Type_NoArg implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Return(short opcode) {

        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Return(String[] assemblyArgs) {

        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.returnFromSubroutine();
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s\n", getMnemonic());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x00EE;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "ret";
        }
    }


    /**
     * class defining Jump instruction
     */
    public static class Jump extends InstructionTypes.Type_NNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Jump(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Jump(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.jump(getValueNNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x1000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "jmp";
        }
    }


    /**
     * class defining CallWordPtr instruction
     */
    public static class CallWordPtr extends InstructionTypes.Type_NNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public CallWordPtr(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public CallWordPtr(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.callSubroutine(getValueNNN() & 0xfff);
        }

        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */

        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x2000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "callptr";
        }
    }


    /**
     * class defining SkipEqualImm instruction
     */
    public static class SkipEqualImm extends InstructionTypes.Type_XNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SkipEqualImm(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SkipEqualImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int reg = state.getReg(getValueX());
            if (reg == (getValueNN() & 0xff)) {
                state.skipInstruction();
            } else {
                state.nextInstruction();
            }
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), 0xff & getValueNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x3000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "skeqi";
        }
    }


    /**
     * class defining SkipNotEqualImm instruction
     */
    public static class SkipNotEqualImm extends InstructionTypes.Type_XNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SkipNotEqualImm(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SkipNotEqualImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int reg = state.getReg(getValueX());
            if (reg != (getValueNN() & 0xff)) {
                state.skipInstruction();
            } else {
                state.nextInstruction();
            }
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), 0xff & getValueNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x4000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "skneqi";
        }
    }


    /**
     * class defining SkipEqualReg instruction
     */
    public static class SkipEqualReg extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SkipEqualReg(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SkipEqualReg(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int reg1 = state.getReg(getValueX());
            int reg2 = state.getReg(getValueY());
            if (reg1 == reg2) {
                state.skipInstruction();
            } else {
                state.nextInstruction();
            }
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x5000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "skeqr";
        }
    }


    /**
     * class defining LoadImm instruction
     */
    public static class LoadImm extends InstructionTypes.Type_XNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public LoadImm(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public LoadImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), 0xff & getValueNN());
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), 0xff & getValueNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x6000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "li";
        }
    }


    /**
     * class defining AddImm instruction
     */
    public static class AddImm extends InstructionTypes.Type_XNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public AddImm(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public AddImm(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), 0xff & (state.getReg(getValueX()) + getValueNN()));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX(), 0xff & getValueNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x7000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "addi";
        }
    }


    /**
     * class defining Mov instruction
     */
    public static class Mov extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Mov(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Mov(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), state.getReg(getValueY()));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "mov";
        }
    }


    /**
     * class defining Or instruction
     */
    public static class Or extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Or(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Or(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), 0xFF & (state.getReg(getValueX()) | state.getReg(getValueY())));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8001;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "or";
        }
    }


    /**
     * class defining And instruction
     */
    public static class And extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public And(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public And(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), 0xff & (state.getReg(getValueX()) & state.getReg(getValueY())));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8002;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "and";
        }
    }


    /**
     * class defining Xor instruction
     */
    public static class Xor extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Xor(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Xor(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), 0xff & (state.getReg(getValueX()) ^ state.getReg(getValueY())));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8003;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "xor";
        }
    }


    /**
     * class defining Add instruction
     */
    public static class Add extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Add(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Add(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int result = state.getReg(getValueX()) + state.getReg(getValueY());
            if (result > 0xff) {
                state.setReg(15, 1);
            } else {
                state.setReg(15, 0);
            }
            state.setReg(getValueX(), result & 0xff);
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8004;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "add";
        }

    }


    /**
     * class defining Sub instruction
     */
    public static class Sub extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Sub(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Sub(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int result = state.getReg(getValueX()) - state.getReg(getValueY());
            if (result < 0) {
                state.setReg(15, 1);
            } else {
                state.setReg(15, 0);
            }
            state.setReg(getValueX(), result & 0xff);
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8005;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "sub";
        }
    }


    /**
     * class defining RShift1 instruction
     */
    public static class RShift1 extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public RShift1(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public RShift1(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(15, state.getReg(getValueX()) & 1);
            state.setReg(getValueX(), state.getReg(getValueX()) >>> 1);
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8006;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "shr1";
        }
    }


    /**
     * class defining SubR instruction
     */
    public static class SubR extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SubR(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SubR(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int result = state.getReg(getValueY()) - state.getReg(getValueX());
            if (result < 0) {
                state.setReg(15, 1);
            } else {
                state.setReg(15, 0);
            }
            state.setReg(getValueX(), result & 0xff);
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x8007;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "subr";
        }
    }


    /**
     * class defining LShift1 instruction
     */
    public static class LShift1 extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public LShift1(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public LShift1(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int reg = state.getReg(getValueX());
            state.setReg(15, reg >> 7);
            state.setReg(getValueX(), (reg << 1) & 0xfe);
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x800E;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "shl1";
        }
    }


    /**
     * class defining SkipNotEqualReg instruction
     */
    public static class SkipNotEqualReg extends InstructionTypes.Type_XY implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SkipNotEqualReg(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SkipNotEqualReg(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            if (state.getReg(getValueX()) != state.getReg(getValueY())) {
                state.skipInstruction();
            } else {
                state.nextInstruction();
            }
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d\n", getMnemonic(), getValueX(), getValueY());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0x9000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "skneqr";
        }
    }


    /**
     * class defining LoadRegI instruction
     */
    public static class LoadRegI extends InstructionTypes.Type_NNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public LoadRegI(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public LoadRegI(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setRegI(getValueNNN());
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s 0x%X\n", getMnemonic(), getValueNNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xA000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "loadI";
        }
    }


    /**
     * class defining BranchRelv0 instruction
     */
    public static class BranchRelv0 extends InstructionTypes.Type_NNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public BranchRelv0(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public BranchRelv0(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.jump(state.getReg(0) + getValueNNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s 0x%x\n", getMnemonic(), getValueNNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xB000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "brelv0";
        }
    }


    /**
     * class defining Rand instruction
     */
    public static class Rand extends InstructionTypes.Type_XNN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public Rand(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public Rand(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), (int) (Math.random() * 0x100) & 0xff & getValueNN());
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, 0x%X\n", getMnemonic(), getValueX() & 0xff, 0xff & getValueNN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xC000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "rand";
        }
    }


    /**
     * class defining DrawSprite instruction
     */
    public static class DrawSprite extends InstructionTypes.Type_XYN implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public DrawSprite(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public DrawSprite(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int x = state.getReg(getValueX());
            int y = state.getReg(getValueY());
            int n = getValueN();
            int address = state.getRegI();
            boolean flipped = false;
            for (int i = 0; i < n; ++i) {
                int sprite = state.memoryGetByte(address + i) & 0xff;
                for (int j = 0; j < 8; ++j) {
                    int bit = (sprite >> (7 - j)) & 1;
                    if (bit != 0 && state.getPixel(x + j, y) == 0xffffff) {
                        flipped = true;
                    }
                    int cordX = x + j;
                    int cordY = y + i;
                    int currentValue = state.getPixel(cordX, cordY) & 0x01;
                    int newValue = 0xffffffff * (currentValue ^ bit);
                    state.setPixel(cordX, cordY, newValue);
                }
            }
            if (flipped) {
                state.setReg(15, 1);
            } else {
                state.setReg(15, 0);
            }
            state.updateScreen();
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d, v%d, 0x%X\n", getMnemonic(), getValueX(), getValueY(), getValueN());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xD000;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "draw";
        }
    }


    /**
     * class defining SkipEqualKey instruction
     */
    public static class SkipEqualKey extends InstructionTypes.Type_X implements Instruction {


        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SkipEqualKey(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SkipEqualKey(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int value = state.getReg(getValueX());
            if(state.isKeyPressed(value)) {
                state.skipInstruction();
            } else {
                state.nextInstruction();
            }
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "skeqkey";
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xE09E;
        }
    }


    /**
     * class defining SkipNotEqualKey instruction
     */
    public static class SkipNotEqualKey extends InstructionTypes.Type_X implements Instruction {


        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SkipNotEqualKey(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SkipNotEqualKey(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int value = state.getReg(getValueX());
            if(!state.isKeyPressed(value)) {
                state.skipInstruction();
            } else {
                state.nextInstruction();
            }
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xE0A1;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "skneqkey";
        }
    }


    /**
     * class defining GetDelayTimerCounter instruction
     */
    public static class GetDelayTimerCounter extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public GetDelayTimerCounter(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public GetDelayTimerCounter(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setReg(getValueX(), state.getDelayTimerCounter());
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF007;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "gdtc";
        }
    }


    /**
     * class defining GetKey instruction
     */
    public static class GetKey extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public GetKey(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public GetKey(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF00A;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "gkey";
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            Optional<Integer> key = state.getKey();
            if(key.isPresent()) {
                state.setReg(getValueX(), key.get());
                state.nextInstruction();
            } else {

            }
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }
    }


    /**
     * class defining SetDelayTimerCounter instruction
     */
    public static class SetDelayTimerCounter extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SetDelayTimerCounter(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SetDelayTimerCounter(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setDelayTimerCounter(state.getReg(getValueX()));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF015;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "sdtc";
        }
    }


    /**
     * class defining SetSoundTimerCounter instruction
     */
    public static class SetSoundTimerCounter extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public SetSoundTimerCounter(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public SetSoundTimerCounter(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setSoundTimerCounter(state.getReg(getValueX()));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF018;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "sstc";
        }
    }


    /**
     * class defining AddRegI instruction
     */
    public static class AddRegI extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public AddRegI(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public AddRegI(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int value = state.getRegI() + state.getReg(getValueX());
            if(value > 0xfff) {
                state.setReg(15 ,1);
            } else {
                state.setReg(15 ,0);
            }
            state.setRegI(value);
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF01E;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "addI";
        }
    }


    /**
     * class defining GetSpriteAddress instruction
     */
    public static class GetSpriteAddress extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public GetSpriteAddress(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public GetSpriteAddress(String[] assemblyArgs) {
            super(assemblyArgs);
        }



        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            state.setRegI(state.getSpriteAddress(getValueX()));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short)getOpcode(getMask());
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF029;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "gsprite";
        }
    }


    /**
     * class defining StoreBCD instruction
     */
    public static class StoreBCD extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public StoreBCD(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public StoreBCD(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int value = state.getReg(getValueX());
            int address = state.getRegI();
            state.memorySetByte(address + 0, (byte) ((value / 100) & 0xff));
            state.memorySetByte(address + 1, (byte) (((value / 10) % 10) & 0xff));
            state.memorySetByte(address + 2, (byte) ((value % 10) & 0xff));
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF033;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "sbcd";
        }
    }


    /**
     * class defining RegDump instruction
     */
    public static class RegDump extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public RegDump(short opcode) {
            super(opcode);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public RegDump(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int address = state.getRegI();
            int end = getValueX();
            for (int i = 0; i < end; ++i) {
                state.memorySetByte(address + i, (byte) (0xff & state.getReg(i)));
            }
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF055;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "regdump";
        }
    }


    /**
     * class defining RegLoad instruction
     */
    public static class RegLoad extends InstructionTypes.Type_X implements Instruction {

        /**
         * construct insturction from opcode
         * @param opcode opcode from bytecode
         */
        public RegLoad(short opcode) {
            super(opcode);
        }


        /**
         * accept concrete visitor
         * @param visitor concrete visitor
         */
        @Override
        public void accept(InstructionVisitor.Visitor visitor) {
            visitor.visit(this);
        }


        /**
         * construct instruction from assembly
         * @param assemblyArgs tokenized assembly line
         */
        public RegLoad(String[] assemblyArgs) {
            super(assemblyArgs);
        }


        /**
         * execute instruction
         * @param state virtual machine state
         */
        @Override
        public void execute(VirtualMachineState state) {
            int address = state.getRegI();
            int end = getValueX();
            for (int i = 0; i < end; ++i) {
                state.setReg(i, state.memoryGetByte(address + i) & 0xff);
            }
            state.nextInstruction();
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public String disassemble() {
            return String.format("%s v%d\n", getMnemonic(), getValueX());
        }


        /**
         * assemble instruction to bytecode
         * @return compiled opcode as short
         */
        @Override
        public short assemble() {
            return (short) getOpcode(getMask());
        }


        /**
         * get instruction mask
         * @return instruction mask as int
         */
        public static int getMask() {
            return 0xF065;
        }


        /**
         * get instruction mnemonic
         * @return instruction mnemonic as String
         */
        public static String getMnemonic() {
            return "regload";
        }
    }

}
