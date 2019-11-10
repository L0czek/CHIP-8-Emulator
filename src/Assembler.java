import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Assembler {
    private HashMap<String, InstructionFactoryInterface> factories;

    public static class AssemblerResult {
        private Serializable instr;
        private int linen;

        public AssemblerResult(Serializable instr, int linen) {
            this.linen = linen;
            this.instr = instr;
        }
        public Serializable getInstruction() {
            return instr;
        }

        public int getLineNumber() {
            return linen;
        }
    }

    public static class Assembled {
        byte[] byteCode;
        int[] linens;

        public Assembled(byte[] byteCode, int[] linens) {
            this.byteCode = byteCode;
            this.linens = linens;
        }

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

        public byte[] getByteCode() {
            return byteCode;
        }

        public int[] getLineNumbers() {
            return linens;
        }
    }

    public Assembler(HashMap<String, InstructionFactoryInterface> factories_) {
        factories = factories_;
    }

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

    public byte[] generateByteCode(String assembly, int linen) throws AssemblerException {
        ArrayList<Byte> byteCode = new ArrayList<>();
        assemble(assembly, linen).forEach(result -> result.getInstruction().serialize(byteCode));
        byte[] result = new byte[byteCode.size()];
        for(int i=0; i < byteCode.size(); ++i) {
            result[i] = byteCode.get(i);
        }
        return result;
    }

    public Assembled generateOutput(String assembly, int linen) throws AssemblerException {
        return Assembled.fromResults(assemble(assembly, linen));
    }

    private String trimComments(String assembly) {
        return assembly.replaceAll("#[^#]+#", "");
    }

    private static class EmitBytesInstruction implements Serializable {
        ArrayList<Byte> data = new ArrayList<>();
        public EmitBytesInstruction(String[] assembleArgs) throws Exception {
            for(int i=1; i < assembleArgs.length; ++i) {
                int number = InstructionTypes.parseInt(assembleArgs[i]);
                if(number > 0xff) {
                    throw new Exception("Number passed overflows byte");
                }
                data.add((byte)number);
            }
        }
        @Override
        public void serialize(ArrayList<Byte> array) {
            array.addAll(data);
        }
    }

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

    public static class AssemblerException extends Exception {
        int linen;
        String msg;

        public AssemblerException(int linen_, String msg_) {
            linen = linen_;
            msg = msg_;
        }

        public int getLineNumber() {
            return linen;
        }

        public String getMsg() {
            return msg;
        }
    }
}
