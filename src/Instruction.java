public interface Instruction {
    String disassemble();
    void execute(VirtualMachineState state);
    short assemble();
}
