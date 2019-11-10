public class MainClass {

    public static void main(String[] argv) throws Exception {
        new Application(
            new EmulatorController(),
            new EmulatorModel(),
            new EmulatorView()
        );
    }
}
