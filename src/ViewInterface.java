import java.awt.*;
import java.awt.image.BufferedImage;

public interface ViewInterface {
    String getAssembly();
    void setAssembly(String assembly);
    void setPixelRGB(int x, int y, int value);
    int getPixelRGB(int x, int y);
    void clearScreen();
    void setupEventHandlers(ControllerInterface controller);
    void reportError(String msg);
    void setLineColor(int linen, Color color);
    void clearLineColors();
    void setStatusText(String text);
    void setRegisterValue(Registers r, int value);
    void enableAssemblerEditing();
    void disableAssemblerEditing();
    void setScreen(BufferedImage screen);
}
