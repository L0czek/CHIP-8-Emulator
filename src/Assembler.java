import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class Assembler {
    private HashMap<String, InstructionFactoryInterface> factories;

    public Assembler(HashMap<String, InstructionFactoryInterface> factories_) {
        factories = factories_;
    }

    public ArrayList<Serializable> assembleInstructions(String assembly) throws AssemblerException {
        var trimmedAssembly = trimComments(assembly);
        ArrayList<Serializable> result = new ArrayList<>();
        int linen = 1;
        for(var line : trimmedAssembly.split("\n")) {
            var assemblyArgs = line.split("[\\s,]+");
            if(assemblyArgs.length == 0) {
                continue;
            }
            var mnemonic = assemblyArgs[0];
            if(mnemonic.equals("db")) {
                try{
                    result.add(new EmitBytesInstruction(assemblyArgs));
                } catch (Exception e) {
                    throw new AssemblerException(linen, e.getMessage());
                }
            } else {
                var assembled = factories.get(mnemonic).fromAssembly(assemblyArgs);
                if(assembled.isPresent()) {
                    result.add(assembled.get());
                } else {
                    throw new AssemblerException(linen, "failed to match arguments to instruction");
                }
            }
            ++linen;
        }
        return result;
    }

    public byte[] generateByteCode(String assembly) throws Exception {
        ArrayList<Byte> byteCode = new ArrayList<>();
        assembleInstructions(assembly).forEach(instruction -> instruction.serialize(byteCode));
        byte[] result = new byte[byteCode.size()];
        for(int i=0; i < byteCode.size(); ++i) {
            result[i] = byteCode.get(i);
        }
        return result;
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
