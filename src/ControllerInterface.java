import java.awt.event.KeyEvent;

public interface ControllerInterface {
    void loadAssemblyFromFile(String path);
    void loadByteCodeFromFile(String path);
    void saveAssemblyToFile(String path);
    void saveByteCodeToFile(String path);
    void runEmulation();
    void stepIn();
    void stepOver();
    void cont();
    void stop();
    void setupEventHandlers(ViewInterface view);
    void setupEventHandlers(ModelInterface model);
    void markAsCode(int linen);
    void markAsData(int linen);
    void setRegisterValue(Registers r, int value);
    void keyPressed(KeyEvent keyEvent);
    void keyReleased(KeyEvent keyEvent);
}
