public class MainClass {
    public static void main(String[] argv) {
        new Application(
            new EmulatorController(),
            new EmulatorModel(),
            new EmulatorView()
        );
    }
}
