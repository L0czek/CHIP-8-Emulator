public class MainClass {
    /**
     * main class of this application
     * @param argv not used
     * @throws Exception when sth goes wrong
     */
    public static void main(String[] argv) throws Exception {
        new Application(
            new EmulatorController(),
            new EmulatorModel(),
            new EmulatorView()
        );
    }
}
