import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * interface used to define model part of MSV
 */
public interface ModelInterface {
    /**
     * load assembly from file
     * @param path path to assembly file
     * @return loaded assembler to display
     * @throws IOException throws on filesystem error
     */
    String loadAssemblyFromFile(String path) throws IOException;
    /**
     * load bytecode from file
     * @param path path to bytecode file
     * @return disassembled bytecode to display
     * @throws IOException throws on filesystem error
     */
    String loadByteCodeFromFile(String path) throws IOException;
    /**
     * save assembly to file
     * @param path path to file selected by user
     * @param assembly assembly from view
     * @throws IOException throws on filesystem error
     */
    void saveAssemblyToFile(String path, String assembly) throws IOException;
    /**
     * save assembled code to file
     * @param path path to file selected by user
     * @param assembly assembly from view
     * @throws IOException throws on filesystem error
     */
    void saveByteCodeToFile(String path, String assembly) throws Assembler.AssemblerException, IOException;
    /**
     * function tries to disassemble selected assembly line as valid instruction
     * @param linen line number to change
     * @param assembly assembly to recompile
     * @return recompiled assembly
     * @throws Assembler.AssemblerException thrown on assembly error
     */
    String recompileAsCode(int linen, String assembly) throws Assembler.AssemblerException;
    /**
     * function tries to disassemble selected assembly line as emit byte meta instruction
     * @param linen line number to change
     * @param assembly assembly to recompile
     * @return recompiled assembly
     * @throws Assembler.AssemblerException thrown on assembly error
     */
    String recompileAsData(int linen, String assembly) throws Assembler.AssemblerException;
    /**
     * handler user pressing run button
     * @param assembly
     * @throws Assembler.AssemblerException
     */
    void startEmulation(String assembly) throws Assembler.AssemblerException;
    /**
     * execute one instruction at current ip
     * @throws VirtualMachineState.VMException thrown by instruction
     */
    void executeOpcode() throws VirtualMachineState.VMException;
    /**
     * set selected register value
     * @param r selected register
     * @param value new value
     */
    void setRegisterValue(Registers r, int value);
    /**
     * get selected register value
     * @param r selected register
     * @return register value
     */
    int getRegisterValue(Registers r);
    /**
     * handles key press event
     * @param keyEvent event send
     */
    void keyPressed(KeyEvent keyEvent);
    /**
     * handles key released event
     * @param keyEvent event send
     */
    void keyReleased(KeyEvent keyEvent);
    /**
     * get assembly line of currently executing instruction
     * @return line number
     */
    int getCurrentExecutingLineNumber();
    /**
     * get the screen as 2 dimensional array
     * @return screen buffer as int array
     */
    int[][] getScreen();
    /**
     * get pixel value
     * @param x x coordinate
     * @param y y coordinate
     * @return RGB value
     */
    int getPixel(int x, int y);
    /**
     * setup event handlers for view
     * @param view view part of MVC
     */
    void setupEventHandlers(ViewInterface view);

}
