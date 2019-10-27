import java.util.ArrayList;

public interface Serializable {
    default void serialize(ArrayList<Byte> array, short opcode) {
        array.add((byte)(opcode >> 8));
        array.add((byte)(opcode & 0xff));
    }
    default short assemble() { return 0; }
    default void serialize(ArrayList<Byte> array) {
        serialize(array, assemble());
    }
}
