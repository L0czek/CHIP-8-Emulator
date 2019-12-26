import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Key;
import java.util.Optional;

/**
 * class holds event passing classes
 */
public class Events {
    /**
     * class which passes events from view to controller
     */
    public static class ForView {
        private ControllerInterface controller;

        /**
         * bind events
         * @param controller controller part of MVC
         */
        public ForView(ControllerInterface controller) {
            this.controller = controller;
        }

        /**
         * send load assembly event
         * @param path path to file
         */
        public void sendLoadAssemblyEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.loadAssemblyFromFile(path));
        }

        /**
         * send load bytecode event
         * @param path path to file
         */
        public void sendLoadByteCodeEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.loadByteCodeFromFile(path));
        }

        /**
         * send save assembly event
         * @param path path to file
         */
        public void sendSaveAssemblyEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.saveAssemblyToFile(path));
        }

        /**
         * send save bytecode event
         * @param path path to file
         */
        public void sendSaveByteCodeEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.saveByteCodeToFile(path));
        }

        /**
         * send run emulation event
         */
        public void sendRunEvent() {
            SwingUtilities.invokeLater(() -> controller.runEmulation());
        }

        /**
         * send stop emulation event
         */
        public void sendStopEvent() {
            SwingUtilities.invokeLater(() -> controller.stop());
        }

        /**
         * send continue emulation event
         */
        public void sendContinueEvent() {
            SwingUtilities.invokeLater(() -> controller.cont());
        }

        /**
         * send step in emulation event
         */
        public void sendStepInEvent() {
            SwingUtilities.invokeLater(() -> controller.stepIn());
        }

        /**
         * send step over emulation event
         */
        public void sendStepOverEvent() {
            SwingUtilities.invokeLater(() -> controller.stepOver());
        }

        /**
         * send mark as code event
         * @param linen line number to be marked as code
         */
        public void sendMarkAsCodeEvent(int linen) {
            SwingUtilities.invokeLater(() -> controller.markAsCode(linen));
        }

        /**
         * send mark as data event
         * @param linen line number to be marked as data
         */
        public void sendMarkAsDataEvent(int linen) {
            SwingUtilities.invokeLater(() -> controller.markAsData(linen));
        }

        /**
         * send set register value event
         * @param r register selected by user
         * @param value new value
         */
        public void sendSetRegisterValueEvent(Registers r, int value) {
            SwingUtilities.invokeLater(() -> controller.setRegisterValue(r, value));
        }

        /**
         * send key press event
         * @param keyEvent keyboard event
         */
        public void sendKeyPressedEvent(KeyEvent keyEvent) {
            SwingUtilities.invokeLater(() -> controller.keyPressed(keyEvent));
        }

        /**
         * send key released event
         * @param keyEvent keyboard event
         */
        public void sendKeyReleasedEvent(KeyEvent keyEvent) {
            SwingUtilities.invokeLater(() -> controller.keyReleased(keyEvent));
        }

        /**
         * send exit emulation event
         */
        public void sendExitEmulationEvent() {
            SwingUtilities.invokeLater(() -> controller.exitEmulation());
        }
    }

    /**
     * class passes events from controller to view
     */
    public static class ViewForController {
        private ViewInterface view = null;

        /**
         * bind events
         * @param view view part of MVC
         */
        public ViewForController(ViewInterface view) {
            this.view = view;
        }

        /**
         * send get assembly from assembly editor event
         * @return assembly from editor
         */
        public String sendGetAssemblyEvent() {
            return view.getAssembly();
        }

        /**
         * send set assembly event
         * @param assembly assembly to be set in editor
         */
        public void sendSetAssemblyEvent(String assembly) {
            SwingUtilities.invokeLater(() -> view.setAssembly(assembly));
        }

        /**
         * send set pixel event
         * @param x x coordinate
         * @param y y coordinate
         * @param value RGB value
         */
        public void sendSetPixelRGBEvent(int x, int y, int value) {
            SwingUtilities.invokeLater(() -> view.setPixelRGB(x, y, value));
        }

        /**
         * send get pixel value
         * @param x x coordinate
         * @param y y coordinate
         * @return RGB value
         */
        public int sendGetPixelRGBEvent(int x, int y) {
            return view.getPixelRGB(x, y);
        }

        /**
         * send clear screen event
         */
        public void sendClearScreenEvent() {
            SwingUtilities.invokeLater(() -> view.clearScreen());
        }

        /**
         * send report error event
         * @param msg error message
         */
        public void sendReportErrorEvent(String msg) {
            SwingUtilities.invokeLater(() -> view.reportError(msg));
        }

        /**
         * send set line color event
         * @param linen line number to be set
         * @param color color to set
         */
        public void sendSetLineColorEvent(int linen, Color color) {
            SwingUtilities.invokeLater(() -> view.setLineColor(linen, color));
        }

        /**
         * send clear line color event
         */
        public void sendClearLineColorsEvent() {
            SwingUtilities.invokeLater(() -> view.clearLineColors());
        }

        /**
         * send set status text event
         * @param text status text
         */
        public void sendSetStatusTextEvent(String text) {
            SwingUtilities.invokeLater(() -> view.setStatusText(text));
        }

        /**
         * send set register value event
         * @param r selected register
         * @param value new value
         */
        public void sendSetRegisterValueEvent(Registers r, int value) {
            SwingUtilities.invokeLater(() -> view.setRegisterValue(r, value));
        }

        /**
         * send enable assembly editing event
         */
        public void sendEnableAssemblerEditingEvent() {
            SwingUtilities.invokeLater(() -> view.enableAssemblerEditing());
        }
        /**
         * send disable assembly editing event
        */
        public void sendDisableAssemblerEditingEvent() {
            SwingUtilities.invokeLater(() -> view.disableAssemblerEditing());
        }

        /**
         * send set screen event
         * @param screen image to set on screen
         */
        public void sendSetScreenEvent(BufferedImage screen) {
            SwingUtilities.invokeLater(() -> view.setScreen(screen));
        }
    }
    /**
     * class passes events from controller to model
     */
    public static class ModelForController {
        private ModelInterface model = null;

        /**
         * bind events
         * @param model model part of MVC
         */
        public ModelForController(ModelInterface model) {
            this.model = model;
        }

        /**
         * send load assembly from file event
         * @param path selected path
         * @return loaded assembly
         * @throws IOException thrown on IO error when reading file
         */
        public String sendLoadAssemblyFromFileEvent(String path) throws IOException {
            return model.loadAssemblyFromFile(path);
        }

        /**
         * send load byte code from file event
         * @param path selected path
         * @return disassembled assembly
         * @throws IOException thrown on IO error when reading file
         */
        public String sendLoadByteCodeFromFileEvent(String path) throws IOException {
            return model.loadByteCodeFromFile(path);
        }

        /**
         * send save assembly to file event
         * @param path selected file
         * @param assembly assembly from editor to save
         * @throws IOException thrown on IO error when reading file
         */
        public void sendSaveAssemblyToFileEvent(String path, String assembly) throws IOException {
            model.saveAssemblyToFile(path, assembly);
        }

        /**
         * send save bytecode to file event
         * @param path selected file
         * @param assembly assembly from editor to be compiled
         * @throws IOException thrown when cannot save to file
         * @throws Assembler.AssemblerException thrown by assembler on compilation error
         */
        public void sendSaveByteCodeToFileEvent(String path, String assembly) throws IOException, Assembler.AssemblerException {
            model.saveByteCodeToFile(path, assembly);
        }

        /**
         * send start emulation event
         * @param assembly assembly from editor to run emulator on
         * @throws Assembler.AssemblerException thrown on assembler error
         */
        public void sendStartEmulationEvent(String assembly) throws Assembler.AssemblerException {
            model.startEmulation(assembly);
        }

        /**
         * send recompile as code event
         * @param linen line number to be recompiled
         * @param assembly assembly from editor
         * @return fixed assembly
         * @throws Assembler.AssemblerException when compilation error occurs
         */
        public String sendRecompileAsCode(int linen, String assembly) throws Assembler.AssemblerException {
            return model.recompileAsCode(linen, assembly);
        }

        /**
         * send recompile as data event
         * @param linen line number to be recompiled
         * @param assembly assembly from editor
         * @return fixed assembly
         * @throws Assembler.AssemblerException when compilation error occurs
         */
        public String sendRecompileAsData(int linen, String assembly) throws Assembler.AssemblerException {
            return model.recompileAsData(linen, assembly);
        }

        /**
         * send execute opcode event
         * @throws VirtualMachineState.VMException thrown by executing instruction on error
         */
        public void sendExecuteOpcodeEvent() throws VirtualMachineState.VMException {
            model.executeOpcode();
        }

        /**
         * send get currently executing line event
         * @return currently executing line
         */
        public int sendGetCurrentExecutingLineEvent() {
            return model.getCurrentExecutingLineNumber();
        }

        /**
         * send set register value event
         * @param r selected register
         * @param value new value
         */
        public void sendSetRegisterValueEvent(Registers r, int value) {
            model.setRegisterValue(r, value);
        }

        /**
         * send get register value
         * @param r selected register
         * @return value of register
         */
        public int sendGetRegisterValueEvent(Registers r) {
            return model.getRegisterValue(r);
        }

        /**
         * send key pressed event
         * @param keyEvent keyboard event
         */
        public void sendKeyPressedEvent(KeyEvent keyEvent) {
            model.keyPressed(keyEvent);
        }

        /**
         * send key released event
         * @param keyEvent keyboard event
         */
        public void sendKeyReleasedEvent(KeyEvent keyEvent) {
            model.keyReleased(keyEvent);
        }

        /**
         * send get screen event
         * @return screen buffer as 2 dimensional int array
         */
        public int[][] sendGetScreenEvent() {
            return model.getScreen();
        }

        /**
         * send get pixel value event
         * @param x x coordinate
         * @param y y coordinate
         * @return RGB value of pixel
         */
        public int sendGetPixelEvent(int x, int y) {
            return model.getPixel(x, y);
        }
    }

    /**
     * class passes events from model to view
     */
    public static class ViewForModel {
        private ViewInterface view = null;

        /**
         * bind events
         * @param view view part of MVC
         */
        public ViewForModel(ViewInterface view) {
            this.view = view;
        }

        /**
         * send set line color event
         * @param linen line number to be colored
         * @param color selected color
         */
        public void sendSetLineColorEvent(int linen, Color color) {
            SwingUtilities.invokeLater(() -> view.setLineColor(linen, color));
        }

        /**
         * send clear line colors event
         */
        public void sendClearLineColorsEvent() {
            SwingUtilities.invokeLater(() -> view.clearLineColors());
        }

        /**
         * send set pixel event
         * @param x x coordinate
         * @param y y coordinate
         * @param value RGB value
         */
        public void sendSetPixelRGBEvent(int x, int y, int value) {
            SwingUtilities.invokeLater(() -> view.setPixelRGB(x, y, value));
        }

        /**
         * send clear screen event
         */
        public void sendClearScreenEvent() {
            SwingUtilities.invokeLater(() -> view.clearScreen());
        }

    }
}
