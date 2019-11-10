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
    void executeOpcode();
    void setRegisterValue(Registers r, int value);
    int getRegisterValue(Registers r);
    void keyPressed(KeyEvent keyEvent);
    BufferedImage getScreen();

}
