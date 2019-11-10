import java.util.ArrayList;

public interface Instruction extends Serializable {
    String disassemble();
    void execute(VirtualMachineState state);
    short assemble();

    void accept(InstructionVisitor.Visitor visitor);
}

