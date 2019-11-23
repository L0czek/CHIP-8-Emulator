import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ModelInterface {
    String loadAssemblyFromFile(String path) throws IOException;
    String loadByteCodeFromFile(String path) throws IOException;
    void saveAssemblyToFile(String path, String assembly) throws IOException;
    void saveByteCodeToFile(String path, String assembly) throws Assembler.AssemblerException, IOException;

    String recompileAsCode(int linen, String assembly) throws Assembler.AssemblerException;
    String recompileAsData(int linen, String assembly) throws Assembler.AssemblerException;
    void startEmulation(String assembly) throws Assembler.AssemblerException;
    void executeOpcode() throws VirtualMachineState.VMException;
    void setRegisterValue(Registers r, int value);
    int getRegisterValue(Registers r);
    void keyPressed(KeyEvent keyEvent);
    void keyReleased(KeyEvent keyEvent);
    int getCurrentExecutingLineNumber();
    int[][] getScreen();
    int getPixel(int x, int y);
    void setupEventHandlers(ViewInterface view);

}
