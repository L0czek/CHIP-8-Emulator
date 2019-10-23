import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

public class InstructionFactory<Instr extends Instruction> implements InstructionFactoryInterface{
    private final Constructor<? extends Instr> assemblyCtor;
    private final Constructor<? extends Instr> opcodeCtor;

    private int opcodeMask;
    private int opcodeValue;
    private String mnemonic;

    public InstructionFactory(Class<? extends Instr> impl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        assemblyCtor = impl.getConstructor(String[].class);
        opcodeCtor = impl.getConstructor(short.class);
        opcodeMask = (int)impl.getMethod("getOpcodeMask").invoke(null, new Object[0]);
        opcodeValue = (int)impl.getMethod("getMask").invoke(null, new Object[0]);
        mnemonic = (String)impl.getMethod("getMnemonic").invoke(null, new Object[0]);
    }

    @Override
    public Optional<Instruction> fromAssembly(String[] assembly) {
        try {
            if(assembly[0].equals(mnemonic)) {
                return Optional.of(assemblyCtor.newInstance((Object) assembly));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Instruction> fromOpcode(short opcode) {
        try {
            if((opcode & opcodeMask) == opcodeValue) {
                return Optional.of(opcodeCtor.newInstance(opcode));
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public int getOpcodeMaskAccuracy() {
        return Integer.bitCount(opcodeMask);
    }
}
