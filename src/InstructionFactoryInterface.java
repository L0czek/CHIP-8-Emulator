import java.util.Optional;

/**
 * interface defining instruction factory
 */
public interface InstructionFactoryInterface {
    /**
     * create instruction from assembly
     * @param assembly tokenized assembly
     * @return instruction or empty on error
     */
    Optional<Instruction> fromAssembly(String[] assembly);
    /**
     * create instruction from opcode
     * @param opcode opcode from bytecode
     * @return instruction or empty on error
     */
    Optional<Instruction> fromOpcode(short opcode);

    /**
     * get mnemonic of instruction
     * @return mnemonic as string
     */
    String getMnemonic();

    /**
     * get opcode of instruction
     * @return opcode as short
     */
    short getOpcode();
    /**
     * used to get number of bits in opcode mask which are set to determine the correct order in which opcode is checked against instructions
     * (before you try with mask 0xf000 you should try with 0xff00 because 0xf000 will match when 0xf000 matches
     * so 0xff00 should be checked first
     * @return number of bits set
     */
    public int getOpcodeMaskAccuracy();
}
