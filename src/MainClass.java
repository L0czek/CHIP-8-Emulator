import java.lang.reflect.InvocationTargetException;

public class MainClass {
    public static void main(String[] argv) {
        InstructionSet.Call call = new InstructionSet.Call((short)0x123);


        try {
            InstructionFactory<InstructionSet.Jump> factory = new InstructionFactory<>(InstructionSet.Jump.class);
            var i = factory.fromAssembly(new String[]{"jmp", "123"});
            System.out.println(i.get().disassemble());
        }catch(Exception e ) {
            System.out.println(e.getMessage());
        }
        new Application(
            new EmulatorController(),
            new EmulatorModel(),
            new EmulatorView()
        );
    }
}
