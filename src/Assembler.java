import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * class implementing CHIP-8 assembler
 */
public class Assembler {
    private HashMap<String, InstructionFactoryInterface> factories;

    public static class AssemblerResult {
        private Serializable instr;
        private int linen;

        /**
         * @param instr object representing instruction
         * @param linen line number in assembly file with this instruction
         */
        public AssemblerResult(Serializable instr, int linen) {
            this.linen = linen;
            this.instr = instr;
        }

        /**
         * @return the instruction being held
         */
        public Serializable getInstruction() {
            return instr;
        }

        /**
         * @return the line number being held
         */
        public int getLineNumber() {
            return linen;
        }
    }

    public static class Assembled {
        byte[] byteCode;
        int[] linens;

        /**
         * @param byteCode compiled instructions
         * @param linens array mapping bytecode offsets to line numbers in assembly
         */
        public Assembled(byte[] byteCode, int[] linens) {
            this.byteCode = byteCode;
            this.linens = linens;
        }

        /**
         * creates this placeholder from raw assembler output
         * @param results raw assembler output ( high level objects to be converted to bytecode byte[])
         * @return this placeholder
         */
        public static Assembled fromResults(ArrayList<AssemblerResult> results) {
            int[] linens = new int[results.size()*2];
            ArrayList<Byte> bc = new ArrayList<>();
            for (AssemblerResult result : results) {
                linens[bc.size()] = result.getLineNumber();
                result.getInstruction().serialize(bc);
            }
            byte[] byteCode = new byte[bc.size()];
            for(int i=0; i < bc.size(); ++i) {
                byteCode[i] = bc.get(i);
            }
            return new Assembled(byteCode, linens);
        }

        /**
         * @return the assembled bytecode
         */
        public byte[] getByteCode() {
            return byteCode;
        }

        /**
         * @return the array mapping bytecode offsets to line numbers
         */
        public int[] getLineNumbers() {
            return linens;
        }
    }

    /**
     * creates Assembler object
     * @param factories_ instruction factories which take assembly instruction as string
     */
    public Assembler(HashMap<String, InstructionFactoryInterface> factories_) {
        factories = factories_;
    }

    /**
     * assembles one assembly line
     * @param assembly one line of assembly code
     * @param linen number of that line
     * @return list of assembly result
     * @throws AssemblerException is thrown on assembly error
     */
    public ArrayList<AssemblerResult> assemble(String assembly, int linen) throws AssemblerException {
        ArrayList<AssemblerResult> result = new ArrayList<>();
        for(String line : assembly.split("\n")) {
            line = trimComments(line).replaceAll("^[\\s]+", "");
            if(line.length() == 0) {
                continue;
            }
            String[] assemblyArgs = line.split("[\\s,]+");
            String mnemonic = assemblyArgs[0];
            if(mnemonic.equals("db")) {
                try{
                    result.add(new AssemblerResult(new EmitBytesInstruction(assemblyArgs), linen));
                } catch (Exception e) {
                    throw new AssemblerException(linen, e.getMessage());
                }
            } else {
                Optional<Instruction> assembled;
                if(!factories.containsKey(mnemonic)) {
                    throw new AssemblerException(linen, "Invalid mnemonic");
                }
                assembled = factories.get(mnemonic).fromAssembly(assemblyArgs);
                if(assembled.isPresent()) {
                    result.add(new AssemblerResult(assembled.get(), linen));
                } else {
                    throw new AssemblerException(linen, "Invalid arguments to instruction");
                }
            }
            ++linen;
        }
        return result;
    }

    /**
     * generate bytecode of one assembly line
     * @param assembly one assembly line
     * @param linen number of that line
     * @return bytecode for the assembly
     * @throws AssemblerException thrown on assembly error
     */
    public byte[] generateByteCode(String assembly, int linen) throws AssemblerException {
        ArrayList<Byte> byteCode = new ArrayList<>();
        assemble(assembly, linen).forEach(result -> result.getInstruction().serialize(byteCode));
        byte[] result = new byte[byteCode.size()];
        for(int i=0; i < byteCode.size(); ++i) {
            result[i] = byteCode.get(i);
        }
        return result;
    }

    /**
     * get assembler output (bytecode with line numbers) in a placeholder
     * @param assembly assembly line
     * @param linen line number
     * @return bytecode and corresponding line numbers mapping array
     * @throws AssemblerException
     */
    public Assembled generateOutput(String assembly, int linen) throws AssemblerException {
        return Assembled.fromResults(assemble(assembly, linen));
    }

    private String trimComments(String assembly) {
        return assembly.replaceAll("#[^#]+#", "");
    }

    /**
     * class used to allow emitting bytes meta instruction
     */
    private static class EmitBytesInstruction implements Serializable {
        ArrayList<Byte> data = new ArrayList<>();

        /**
         * creates instruction from assembly line
         * @param assembleArgs tokenized assembly line
         * @throws Exception thrown when the byte to be emitted overflows
         */
        public EmitBytesInstruction(String[] assembleArgs) throws Exception {
            for(int i=1; i < assembleArgs.length; ++i) {
                int number = InstructionTypes.parseInt(assembleArgs[i]);
                if(number > 0xff) {
                    throw new Exception("Number passed overflows byte");
                }
                data.add((byte)number);
            }
        }

        /**
         * writes generated bytecode to bytecode array during serialization
         * @param array complete bytecode array
         */
        @Override
        public void serialize(ArrayList<Byte> array) {
            array.addAll(data);
        }
    }

    /**
     * function used for fixing offsets when applying operation on assembly code
     * @param assembly assembly code from view
     * @return corrected assembly code
     * @throws AssemblerException thrown on assembly error
     */
    public String fixOffsets(String assembly) throws AssemblerException {
        StringBuilder builder = new StringBuilder();
        int index = 0;
        int linen = 0;
        for (String line : assembly.split("\n")) {
            line = line.replaceAll("^#@[^#]+#","");
            byte[] compiled = generateByteCode(line, linen++);
            if(compiled.length == 2) {
                builder.append(String.format("#@ %04X %04X : %02X %02X #", index, index+0x200, compiled[0], compiled[1]));
                index += 2;
            } else if(compiled.length == 1){
                builder.append(String.format("#@ %04X %04X : %02X    #", index, index+0x200, compiled[0]));
                index += 1;
            }
            builder.append(line).append("\n");
        }
        return builder.toString();
    }

    /**
     * class used to throw assembler exceptions
     */
    public static class AssemblerException extends Exception {
        int linen;
        String msg;

        /**
         * create exception from line number end error message
         * @param linen_ line number of error
         * @param msg_ error message
         */
        public AssemblerException(int linen_, String msg_) {
            linen = linen_;
            msg = msg_;
        }

        /**
         * get line number of error
         * @return line number of error
         */
        public int getLineNumber() {
            return linen;
        }

        /**
         * get error message
         * @return error message
         */
        public String getMsg() {
            return msg;
        }
    }
}
