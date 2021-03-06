import java.util.Arrays;
import java.util.List;

/**
 * CHIP-8 register enumeration
 */
public enum Registers {
    v0,
    v1,
    v2,
    v3,
    v4,
    v5,
    v6,
    v7,
    v8,
    v9,
    v10,
    v11,
    v12,
    v13,
    v14,
    v15,
    I,
    ip,
    Invalid;

    /**
     * create register form register number
     * @param value register number
     * @return selected register
     */
    public static Registers fromInt(int value) {
        switch (value) {
            case 0: return Registers.v0;
            case 1: return Registers.v1;
            case 2: return Registers.v2;
            case 3: return Registers.v3;
            case 4: return Registers.v4;
            case 5: return Registers.v5;
            case 6: return Registers.v6;
            case 7: return Registers.v7;
            case 8: return Registers.v8;
            case 9: return Registers.v9;
            case 10: return Registers.v10;
            case 11: return Registers.v11;
            case 12: return Registers.v12;
            case 13: return Registers.v13;
            case 14: return Registers.v14;
            case 15: return Registers.v15;
            case 16: return Registers.I;
            case 17: return Registers.ip;
            default:
                return Registers.Invalid;
        }
    }

    /**
     * convert register to integer number
     * @param r selected register
     * @return register number
     */
    public static int toInt(Registers r) {
        return r.ordinal();
    }

    /**
     * iterate over all register numbers
     * @return array of all numbers
     */
    public static List<Registers> iterate() {
        return Arrays.asList(Registers.values()).subList(0, 18);
    }
}
