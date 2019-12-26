import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

/**
 * generic class defining factory of instruction
 * @param <Instr> concrete class defining instruction
 */
public class InstructionFactory<Instr extends Instruction> implements InstructionFactoryInterface{
    private final Constructor<? extends Instr> assemblyCtor;
    private final Constructor<? extends Instr> opcodeCtor;

    private int opcodeMask;
    private int opcodeValue;
    private String mnemonic;

    /**
     * create factory from class object of instruction class
     * @param impl class object of instruction class
     * @throws NoSuchMethodException thrown when instruction class does not implement getOpcodeMask, getMask, getMnemonic static functions
     * @throws IllegalAccessException thrown when instruction class does not implement getOpcodeMask, getMask, getMnemonic static functions
     * @throws InvocationTargetException thrown when instruction class does not implement getOpcodeMask, getMask, getMnemonic static functions
     */
    public InstructionFactory(Class<? extends Instr> impl) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        assemblyCtor = impl.getConstructor(String[].class);
        opcodeCtor = impl.getConstructor(short.class);
        opcodeMask = (int)impl.getMethod("getOpcodeMask").invoke(null, new Object[0]);
        opcodeValue = (int)impl.getMethod("getMask").invoke(null, new Object[0]);
        mnemonic = (String)impl.getMethod("getMnemonic").invoke(null, new Object[0]);
    }

    /**
     * create instruction given parsed assembly
     * @param assembly tokenized assembly
     * @return instruction object or empty on error
     */
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

    /**
     * create instruction given opcode from bytecode
     * @param opcode opcode from bytecode
     * @return instruction object or empty on error
     */
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

    /**
     * used to get number of bits in opcode mask which are set to determine the correct order in which opcode is checked against instructions
     * (before you try with mask 0xf000 you should try with 0xff00 because 0xf000 will match when 0xf000 matches
     * so 0xff00 should be checked first
     * @return number of bits set
     */
    @Override
    public int getOpcodeMaskAccuracy() {
        return Integer.bitCount(opcodeMask);
    }

    private static Optional<ArrayList<InstructionFactoryInterface>> factoriesByIndex = Optional.empty();
    private static Optional<HashMap<String, InstructionFactoryInterface>> factoriesByMnemonic = Optional.empty();

    /**
     * get factories by index (useful when disassembling)
     * @return factories as array
     * @throws Exception may be thrown when setting up factories
     */
    public static ArrayList<InstructionFactoryInterface> factoriesByIndex() throws Exception {
        if(factoriesByIndex.isPresent()) {
            return factoriesByIndex.get();
        }
        setupFactories();
        return factoriesByIndex.get();
    }

    /**
     * get factories by mnemonic (useful when assembling)
     * @return hashmap of factories (mnemonic -> factory)
     * @throws Exception may be thrown when setting up factories
     */
    public static HashMap<String, InstructionFactoryInterface> factoriesByMnemonic() throws Exception {
        if(factoriesByMnemonic.isPresent()) {
            return factoriesByMnemonic.get();
        }
        setupFactories();
        return factoriesByMnemonic.get();
    }

    /**
     * used to get mnemonic of this factory
     * @return mnemonic as string
     */
    @Override
    public String getMnemonic() {
        return mnemonic;
    }

    /**
     * get opcode of current factory
     * @return opcode as short
     */
    @Override
    public short getOpcode() {
        return (short)opcodeValue;
    }

    /**
     * sets up factories for all instructions
     * @throws Exception
     */
    private static void setupFactories() throws Exception {
        ArrayList<InstructionFactoryInterface> factoriesList = new ArrayList<InstructionFactoryInterface>() {
            {
                add(new InstructionFactory<>(InstructionSet.Call.class));
                add(new InstructionFactory<>(InstructionSet.DisplayClear.class));
                add(new InstructionFactory<>(InstructionSet.Return.class));
                add(new InstructionFactory<>(InstructionSet.Jump.class));
                add(new InstructionFactory<>(InstructionSet.CallWordPtr.class));
                add(new InstructionFactory<>(InstructionSet.SkipEqualImm.class));
                add(new InstructionFactory<>(InstructionSet.SkipNotEqualImm.class));
                add(new InstructionFactory<>(InstructionSet.SkipEqualReg.class));
                add(new InstructionFactory<>(InstructionSet.LoadImm.class));
                add(new InstructionFactory<>(InstructionSet.AddImm.class));
                add(new InstructionFactory<>(InstructionSet.Mov.class));
                add(new InstructionFactory<>(InstructionSet.Or.class));
                add(new InstructionFactory<>(InstructionSet.And.class));
                add(new InstructionFactory<>(InstructionSet.Xor.class));
                add(new InstructionFactory<>(InstructionSet.Add.class));
                add(new InstructionFactory<>(InstructionSet.Sub.class));
                add(new InstructionFactory<>(InstructionSet.RShift1.class));
                add(new InstructionFactory<>(InstructionSet.SubR.class));
                add(new InstructionFactory<>(InstructionSet.LShift1.class));
                add(new InstructionFactory<>(InstructionSet.SkipNotEqualReg.class));
                add(new InstructionFactory<>(InstructionSet.LoadRegI.class));
                add(new InstructionFactory<>(InstructionSet.BranchRelv0.class));
                add(new InstructionFactory<>(InstructionSet.Rand.class));
                add(new InstructionFactory<>(InstructionSet.DrawSprite.class));
                add(new InstructionFactory<>(InstructionSet.SkipEqualKey.class));
                add(new InstructionFactory<>(InstructionSet.SkipNotEqualKey.class));
                add(new InstructionFactory<>(InstructionSet.GetDelayTimerCounter.class));
                add(new InstructionFactory<>(InstructionSet.GetKey.class));
                add(new InstructionFactory<>(InstructionSet.SetDelayTimerCounter.class));
                add(new InstructionFactory<>(InstructionSet.SetSoundTimerCounter.class));
                add(new InstructionFactory<>(InstructionSet.AddRegI.class));
                add(new InstructionFactory<>(InstructionSet.GetSpriteAddress.class));
                add(new InstructionFactory<>(InstructionSet.StoreBCD.class));
                add(new InstructionFactory<>(InstructionSet.RegDump.class));
                add(new InstructionFactory<>(InstructionSet.RegLoad.class));
            }
        };
        HashMap<String, InstructionFactoryInterface> factoriesHashMap = new HashMap<>();
        factoriesList.forEach(
                factory -> factoriesHashMap.put(factory.getMnemonic(), factory)
        );
        factoriesList.sort((a, b) -> b.getOpcodeMaskAccuracy() - a.getOpcodeMaskAccuracy());
        factoriesByMnemonic = Optional.of(factoriesHashMap);
        factoriesByIndex = Optional.of(factoriesList);
    }
}
