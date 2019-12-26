import java.util.ArrayList;

/**
 * interface defining object representing instruction in emulator
 */
public interface Instruction extends Serializable {
    /**
     * called to disassemble instruction
     * @return disassembly of instruction
     */
    String disassemble();

    /**
     * called to execute instruction
     * @param state current virtual machine state
     */
    void execute(VirtualMachineState state);

    /**
     * called to assemble instruction to bytecode
     * @return assembled opcode
     */
    short assemble();

    /**
     * called when applying visitors to instructions
     * @param visitor specialised visitor
     */
    void accept(InstructionVisitor.Visitor visitor);
}

