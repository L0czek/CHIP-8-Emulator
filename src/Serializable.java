import java.util.ArrayList;

/**
 * interface defining instruction serialization
 */
public interface Serializable {
    /**
     * serialize instruction
     * @param array bytecode array
     * @param opcode compiled opcode
     */
    default void serialize(ArrayList<Byte> array, short opcode) {
        array.add((byte)(opcode >> 8));
        array.add((byte)(opcode & 0xff));
    }

    /**
     * assemble thus instruction to opcode
     * @return
     */
    default short assemble() { return 0; }

    /**
     * serialize this instruction
     * @param array bytecode array
     */
    default void serialize(ArrayList<Byte> array) {
        serialize(array, assemble());
    }
}
