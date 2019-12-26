import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * interface defining View part of MVC
 */
public interface ViewInterface {
    /**
     * get assembly from editor
     * @return assembly code
     */
    String getAssembly();
    /**
     * set assembly in editor
     * @param assembly assembly to be set
     */
    void setAssembly(String assembly);
    /**
     * set pixel value on screen
     * @param x x coordinate
     * @param y y coordinate
     * @param value RGB value
     */
    void setPixelRGB(int x, int y, int value);
    /**
     * get pixel value
     * @param x x coordinate
     * @param y y coordinate
     * @return RGB value
     */
    int getPixelRGB(int x, int y);
    /**
     * clear screen
     */
    void clearScreen();
    void setupEventHandlers(ControllerInterface controller);
    /**
     * show message box with error
     * @param msg msg to show
     */
    void reportError(String msg);
    /**
     * set line color in editor
     * @param linen line number to change color
     * @param color color to be set
     */
    void setLineColor(int linen, Color color);
    /**
     * clear all line colors
     */
    void clearLineColors();
    /**
     * set text of status bar
     * @param text text to be set
     */
    void setStatusText(String text);
    /**
     * set register value on debugger window
     * @param r register to be set
     * @param value new value
     */
    void setRegisterValue(Registers r, int value);
    /**
     * enable editor input
     */
    void enableAssemblerEditing();
    /**
     * disable editor input
     */
    void disableAssemblerEditing();
    /**
     * set screen image
     * @param screen image to be set on screen
     */
    void setScreen(BufferedImage screen);
}
