public interface ControllerInterface {
    void loadAssemblyFromFile(String path);
    void loadByteCodeFromFile(String path);
    void runEmulation();
    void setupEventHandlers(ViewInterface view);
    void setupEventHandlers(ModelInterface model);
}
