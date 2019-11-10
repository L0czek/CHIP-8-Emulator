public class InstructionTypes {

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

    public static class Type_NNN {
        int value;

        public Type_NNN(short opcode) {
            value = (short)(opcode & 0xfff);
        }

        public Type_NNN(String[] assemblyArgs) {
            if(assemblyArgs.length != 2) {
                throw new IllegalArgumentException();
            }
            value = parseInt(assemblyArgs[1]);
            if(value > 0xfff) {
                throw new IllegalArgumentException();
            }
        }

        public int getValueNNN() {
            return value;
        }

        public int getOpcode(int mask) {
            return mask | value;
        }

        public static int getOpcodeMask() {
            return 0xf000;
        }
    }

    public static class Type_XNN {
        int valueX;
        int valueNN;

        public Type_XNN(short opcode) {
            valueX = (opcode >> 8) % 0xf;
            valueNN = (byte)(opcode & 0xff);
        }

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

        public int getValueX() {
            return valueX;
        }

        public int getValueNN() {
            return valueNN;
        }

        public int getOpcode(int mask) {
            return mask | valueX << 8 | valueNN;
        }

        public static int getOpcodeMask() {
            return 0xF000;
        }
    }

    public static class Type_XY {
        int valueX;
        int valueY;

        public Type_XY(short opcode) {
            valueX = (opcode >> 8) & 0xf;
            valueY = (opcode >> 4) & 0xf;
        }

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

        public int getValueX() {
            return valueX;
        }

        public int getValueY() {
            return valueY;
        }

        public int getOpcode(int mask) {
            return mask | valueX << 8 | valueY << 4;
        }

        public static int getOpcodeMask() {
            return 0xF00F;
        }
    }

    public static class Type_X {
        int valueX;

        public Type_X(short opcode) {
            valueX = (opcode >> 8) & 0xf;
        }

        public Type_X(String[] assemblyArgs) {
            valueX = parseRegister(assemblyArgs[1]);
            if(valueX > 0xf) {
                throw new IllegalArgumentException();
            }
        }

        public int getValueX() {
            return valueX;
        }

        public int getOpcode(int mask) {
            return mask | valueX << 8;
        }

        public static int getOpcodeMask() {
            return 0xF0FF;
        }
    }

    public static class Type_NoArg {
        public int getOpcode(int mask) {
            return mask;
        }

        public static int getOpcodeMask() {
            return 0xFFFF;
        }
    }

    public static class Type_XYN {
        int valueX;
        int valueY;
        int valueN;

        public Type_XYN(short opcode) {
            valueX = (opcode >> 8) & 0xf;
            valueY = (opcode >> 4) & 0xf;
            valueN = opcode & 0xf;
        }

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

        public int getValueX() {
            return valueX;
        }

        public int getValueY() {
            return valueY;
        }

        public int getValueN() {
            return valueN;
        }

        public int getOpcode(int mask) {
            return mask | valueX << 8 | valueY << 4 | valueN;
        }

        public static int getOpcodeMask() {
            return 0xF000;
        }
    }
}
