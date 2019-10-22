public interface ViewInterface {
    String getAssembly();
    void setAssembly(String assembly);

    void setPixelRGB(int x, int y, int value);
    int getPixelRGB(int x, int y);
    void clearScreen();
    void setupEventHandlers(ControllerInterface controller);
    void reportError(String msg);
}
