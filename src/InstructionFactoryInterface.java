import java.util.Optional;

public interface InstructionFactoryInterface {
    Optional<Instruction> fromAssembly(String[] assembly);
    Optional<Instruction> fromOpcode(short opcode);

    String getMnemonic();
    short getOpcode();

    public int getOpcodeMaskAccuracy();
}
