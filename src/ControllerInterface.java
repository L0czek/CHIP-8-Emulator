import java.awt.event.KeyEvent;

/**
 * Interface used to create controllers in MVC
 */
public interface ControllerInterface {
    /**
     * handles load assembly from file button on view
     * @param path specified path by user
     */
    void loadAssemblyFromFile(String path);
    /**
     * handles load bytecode from file button on view
     * @param path specified path by user
     */
    void loadByteCodeFromFile(String path);
    /**
     * handles save assembly from file button on view
     * @param path specified path by user
     */
    void saveAssemblyToFile(String path);
    /**
     * handles save bytecode from file button on view
     * @param path specified path by user
     */
    void saveByteCodeToFile(String path);
    /**
     * handles user pressing run button
     */
    void runEmulation();
    /**
     * handles uer pressing step in button
     */
    void stepIn();
    /**
     * handles user pressing step over
     */
    void stepOver();
    /**
     * handles user pressing continue button
     */
    void cont();
    /**
     * handles user pressing stop button
     */
    void stop();
    /**
     * handles user pressing exit button
     */
    void exitEmulation();
    /**
     * setting up event handlers for view
     * @param view view event handlers
     */
    void setupEventHandlers(ViewInterface view);
    /**
     * setting up event handlers for model
     * @param model model event handlers
     */
    void setupEventHandlers(ModelInterface model);
    /**
     * converts assembly line of passed number to code (from emit byte instruction to valid instruction or the same emit byte on disassembly error)
     * @param linen
     */
    void markAsCode(int linen);
    /**
     * change assembly line of passed number to emit byte meta instruction
     * @param linen
     */
    void markAsData(int linen);
    /**
     * handles user setting new register values in view
     * @param r register to be changed
     * @param value new value
     */
    void setRegisterValue(Registers r, int value);
    /**
     * handling keyboard key press event
     * @param keyEvent key press event
     */
    void keyPressed(KeyEvent keyEvent);
    /**
     * handling keyboard key released event
     * @param keyEvent key released event send
     */
    void keyReleased(KeyEvent keyEvent);
}
