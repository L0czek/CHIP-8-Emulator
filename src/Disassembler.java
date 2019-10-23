import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Disassembler {
    ArrayList<InstructionFactoryInterface> instructionFactories = null;

    public Disassembler() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        instructionFactories = new ArrayList<InstructionFactoryInterface>();
        setupFactories();
    }

    private void setupFactories() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        instructionFactories.add(new InstructionFactory<>(InstructionSet.Jump.class));
        
    }
}
