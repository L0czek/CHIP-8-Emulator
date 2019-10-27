import java.lang.reflect.InvocationTargetException;

public class MainClass {

    public static void main(String[] argv) {
        try {
            var asm = new Assembler(InstructionFactory.factoriesByMnemonic());
            var dis = new Disassembler(InstructionFactory.factoriesByIndex());
            System.out.println(dis.tryDisassemble(asm.generateByteCode(
                    "# 0: #jmp 0x123\n"+
                    "callptr 12\n"+
                            "db 0xff\n"+
                    "li v0, 1\n"
            )));
        }catch(Exception e ) {
            System.out.println("got exception");
            System.out.println(e.getMessage());
        }
        new Application(
            new EmulatorController(),
            new EmulatorModel(),
            new EmulatorView()
        );
    }
}
