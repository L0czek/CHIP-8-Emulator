/**
 * class used to keep classes describing different types of instructions
 */
public class InstructionTypes {
    /**
     * parses number in string
     * @param str assembly arg
     * @return parsed int
     */
    public static int parseInt(String str) {
        try {
            if(str.startsWith("0x")) {
                return Integer.parseInt(str.substring(2), 16);
            } else {
                return Integer.parseInt(str);
            }
        }catch(NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }
    /**
     * used to parse register reference in assembly like `r2` which is reference to second register
     * @param str assembly arg
     * @return parsed register number
     */
    public static int parseRegister(String str) {
        if(!str.startsWith("v")) {
            throw new IllegalArgumentException();
        }
        try {
            return Integer.parseInt(str.substring(1));
        }catch(NumberFormatException e){
            throw new IllegalArgumentException();
        }
    }

    /**
     * class defining instruction with one 12 bits immediate argument
     */
    public static class Type_NNN {
        int value;

        /**
         * construct from opcode
         * @param opcode opcode from bytecode
         */
        public Type_NNN(short opcode) {
            value = (short)(opcode & 0xfff);
        }

        /**
         * construct from assembly args
         * @param assemblyArgs tokenized assembly
         */
        public Type_NNN(String[] assemblyArgs) {
            if(assemblyArgs.length != 2) {
                throw new IllegalArgumentException();
            }
            value = parseInt(assemblyArgs[1]);
            if(value > 0xfff) {
                throw new IllegalArgumentException();
            }
        }

        /**
         * get argument value
         * @return immediate argument value
         */
        public int getValueNNN() {
            return value;
        }

        /**
         * get opcode of this instruction
         * @param mask opcode mask (eg. 0xA000 with arg 0x123 make 0xA123 which is jump at address 0x123)
         * @return opcode compiled
         */
        public int getOpcode(int mask) {
            return mask | value;
        }

        /**
         * mask used to check against type of instructions
         * @return mask of this instruction type
         */
        public static int getOpcodeMask() {
            return 0xf000;
        }
    }

    /**
     * class describing instruction with one register argument and on 8 bits immediate
     */
    public static class Type_XNN {
        int valueX;
        int valueNN;
        /**
         * construct from opcode
         * @param opcode opcode from bytecode
         */
        public Type_XNN(short opcode) {
            valueX = (opcode >> 8) & 0xf;
            valueNN = (byte)(opcode & 0xff);
        }
        /**
         * construct from assembly args
         * @param assemblyArgs tokenized assembly
         */
        public Type_XNN(String[] assemblyArgs) {
            if(assemblyArgs.length != 3) {
                throw new IllegalArgumentException();
            }
            valueX = parseRegister(assemblyArgs[1]);
            int constant = parseInt(assemblyArgs[2]);
            if(valueX > 0xf || constant > 0xff) {
                throw new IllegalArgumentException();
            }
            valueNN = constant;
        }

        /**
         * get register number
         * @return
         */
        public int getValueX() {
            return valueX;
        }

        /**
         * get register argument value
         * @return argument value
         */
        public int getValueNN() {
            return valueNN;
        }
        /**
         * get opcode of this instruction
         * @param mask opcode mask (eg. 0xA000 with arg 0x123 make 0xA123 which is jump at address 0x123)
         * @return opcode compiled
         */
        public int getOpcode(int mask) {
            return mask | valueX << 8 | valueNN;
        }
        /**
         * mask used to check against type of instructions
         * @return mask of this instruction type
         */
        public static int getOpcodeMask() {
            return 0xF000;
        }
    }

    /**
     * class describing instruction with 2 register operands
     */
    public static class Type_XY {
        int valueX;
        int valueY;
        /**
         * construct from opcode
         * @param opcode opcode from bytecode
         */
        public Type_XY(short opcode) {
            valueX = (opcode >> 8) & 0xf;
            valueY = (opcode >> 4) & 0xf;
        }
        /**
         * construct from assembly args
         * @param assemblyArgs tokenized assembly
         */
        public Type_XY(String[] assemblyArgs) {
            if(assemblyArgs.length != 3) {
                throw new IllegalArgumentException();
            }
            valueX = parseRegister(assemblyArgs[1]);
            valueY = parseRegister(assemblyArgs[2]);
            if(valueX > 0xf || valueY > 0xf) {
                throw new IllegalArgumentException();
            }
        }
        /**
         * get register argument value
         * @return argument value
         */
        public int getValueX() {
            return valueX;
        }
        /**
         * get register argument value
         * @return argument value
         */
        public int getValueY() {
            return valueY;
        }
        /**
         * get opcode of this instruction
         * @param mask opcode mask (eg. 0xA000 with arg 0x123 make 0xA123 which is jump at address 0x123)
         * @return opcode compiled
         */
        public int getOpcode(int mask) {
            return mask | valueX << 8 | valueY << 4;
        }
        /**
         * mask used to check against type of instructions
         * @return mask of this instruction type
         */
        public static int getOpcodeMask() {
            return 0xF00F;
        }
    }

    /**
     * class describing instruction with single register argument
     */
    public static class Type_X {
        int valueX;
        /**
         * construct from opcode
         * @param opcode opcode from bytecode
         */
        public Type_X(short opcode) {
            valueX = (opcode >> 8) & 0xf;
        }
        /**
         * construct from assembly args
         * @param assemblyArgs tokenized assembly
         */
        public Type_X(String[] assemblyArgs) {
            valueX = parseRegister(assemblyArgs[1]);
            if(valueX > 0xf) {
                throw new IllegalArgumentException();
            }
        }
        /**
         * get register argument value
         * @return argument value
         */
        public int getValueX() {
            return valueX;
        }
        /**
         * get opcode of this instruction
         * @param mask opcode mask (eg. 0xA000 with arg 0x123 make 0xA123 which is jump at address 0x123)
         * @return opcode compiled
         */
        public int getOpcode(int mask) {
            return mask | valueX << 8;
        }
        /**
         * mask used to check against type of instructions
         * @return mask of this instruction type
         */
        public static int getOpcodeMask() {
            return 0xF0FF;
        }
    }

    /**
     * class describing instruction with noi arguments
     */
    public static class Type_NoArg {
        public int getOpcode(int mask) {
            return mask;
        }

        public static int getOpcodeMask() {
            return 0xFFFF;
        }
    }

    /**
     * class describing instruction with 2 register and 1 immediate argument
     */
    public static class Type_XYN {
        int valueX;
        int valueY;
        int valueN;
        /**
         * construct from opcode
         * @param opcode opcode from bytecode
         */
        public Type_XYN(short opcode) {
            valueX = (opcode >> 8) & 0xf;
            valueY = (opcode >> 4) & 0xf;
            valueN = opcode & 0xf;
        }
        /**
         * construct from assembly args
         * @param assemblyArgs tokenized assembly
         */
        public Type_XYN(String[] assemblyArgs) {
            if(assemblyArgs.length != 4) {
                throw new IllegalArgumentException();
            }
            valueX = parseRegister(assemblyArgs[1]);
            valueY = parseRegister(assemblyArgs[2]);
            valueN = parseInt(assemblyArgs[3]);
            if(valueX > 0xf | valueY > 0xf | valueN > 0xf) {
                throw  new IllegalArgumentException();
            }
        }
        /**
         * get register argument value
         * @return argument value
         */
        public int getValueX() {
            return valueX;
        }
        /**
         * get register argument value
         * @return argument value
         */
        public int getValueY() {
            return valueY;
        }
        /**
         * get immedaite argument value
         * @return immediate argument value
         */
        public int getValueN() {
            return valueN;
        }
        /**
         * get opcode of this instruction
         * @param mask opcode mask (eg. 0xA000 with arg 0x123 make 0xA123 which is jump at address 0x123)
         * @return opcode compiled
         */
        public int getOpcode(int mask) {
            return mask | valueX << 8 | valueY << 4 | valueN;
        }
/**
 * mask used to check against type of instructions
 * @return mask of this instruction type
 */
        public static int getOpcodeMask() {
            return 0xF000;
        }
    }
}
