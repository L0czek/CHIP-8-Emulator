import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.Key;
import java.util.Optional;

public class Events {
    public static class ForView {
        private ControllerInterface controller;

        public ForView(ControllerInterface controller) {
            this.controller = controller;
        }
        public void sendLoadAssemblyEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.loadAssemblyFromFile(path));
        }
        public void sendLoadByteCodeEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.loadByteCodeFromFile(path));
        }
        public void sendSaveAssemblyEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.saveAssemblyToFile(path));
        }
        public void sendSaveByteCodeEvent(String path) {
            SwingUtilities.invokeLater(() -> controller.saveByteCodeToFile(path));
        }
        public void sendRunEvent() {
            SwingUtilities.invokeLater(() -> controller.runEmulation());
        }
        public void sendStopEvent() {
            SwingUtilities.invokeLater(() -> controller.stop());
        }
        public void sendContinueEvent() {
            SwingUtilities.invokeLater(() -> controller.cont());
        }
        public void sendStepInEvent() {
            SwingUtilities.invokeLater(() -> controller.stepIn());
        }
        public void sendStepOverEvent() {
            SwingUtilities.invokeLater(() -> controller.stepOver());
        }
        public void sendMarkAsCodeEvent(int linen) {
            SwingUtilities.invokeLater(() -> controller.markAsCode(linen));
        }
        public void sendMarkAsDataEvent(int linen) {
            SwingUtilities.invokeLater(() -> controller.markAsData(linen));
        }
        public void sendSetRegisterValueEvent(Registers r, int value) {
            SwingUtilities.invokeLater(() -> controller.setRegisterValue(r, value));
        }
        public void sendKeyPressedEvent(KeyEvent keyEvent) {
            SwingUtilities.invokeLater(() -> controller.keyPressed(keyEvent));
        }
        public void sendKeyReleasedEvent(KeyEvent keyEvent) {
            SwingUtilities.invokeLater(() -> controller.keyReleased(keyEvent));
        }
    }

    public static class ViewForController {
        private ViewInterface view = null;

        public ViewForController(ViewInterface view) {
            this.view = view;
        }

        public String sendGetAssemblyEvent() {
            return view.getAssembly();
        }

        public void sendSetAssemblyEvent(String assembly) {
            SwingUtilities.invokeLater(() -> view.setAssembly(assembly));
        }

        public void sendSetPixelRGBEvent(int x, int y, int value) {
            SwingUtilities.invokeLater(() -> view.setPixelRGB(x, y, value));
        }

        public int sendGetPixelRGBEvent(int x, int y) {
            return view.getPixelRGB(x, y);
        }

        public void sendClearScreenEvent() {
            SwingUtilities.invokeLater(() -> view.clearScreen());
        }

        public void sendReportErrorEvent(String msg) {
            SwingUtilities.invokeLater(() -> view.reportError(msg));
        }

        public void sendSetLineColorEvent(int linen, Color color) {
            SwingUtilities.invokeLater(() -> view.setLineColor(linen, color));
        }

        public void sendClearLineColorsEvent() {
            SwingUtilities.invokeLater(() -> view.clearLineColors());
        }

        public void sendSetStatusTextEvent(String text) {
            SwingUtilities.invokeLater(() -> view.setStatusText(text));
        }

        public void sendSetRegisterValueEvent(Registers r, int value) {
            SwingUtilities.invokeLater(() -> view.setRegisterValue(r, value));
        }
        public void sendEnableAssemblerEditingEvent() {
            SwingUtilities.invokeLater(() -> view.enableAssemblerEditing());
        }
        public void sendDisableAssemblerEditingEvent() {
            SwingUtilities.invokeLater(() -> view.disableAssemblerEditing());
        }
        public void sendSetScreenEvent(BufferedImage screen) {
            SwingUtilities.invokeLater(() -> view.setScreen(screen));
        }
    }

    public static class ModelForController {
        private ModelInterface model = null;

        public ModelForController(ModelInterface model) {
            this.model = model;
        }
        public String sendLoadAssemblyFromFileEvent(String path) throws IOException {
            return model.loadAssemblyFromFile(path);
        }
        public String sendLoadByteCodeFromFileEvent(String path) throws IOException {
            return model.loadByteCodeFromFile(path);
        }
        public void sendSaveAssemblyToFileEvent(String path, String assembly) throws IOException {
            model.saveAssemblyToFile(path, assembly);
        }
        public void sendSaveByteCodeToFileEvent(String path, String assembly) throws IOException, Assembler.AssemblerException {
            model.saveByteCodeToFile(path, assembly);
        }
        public void sendStartEmulationEvent(String assembly) throws Assembler.AssemblerException {
            model.startEmulation(assembly);
        }
        public String sendRecompileAsCode(int linen, String assembly) throws Assembler.AssemblerException {
            return model.recompileAsCode(linen, assembly);
        }
        public String sendRecompileAsData(int linen, String assembly) throws Assembler.AssemblerException {
            return model.recompileAsData(linen, assembly);
        }
        public void sendExecuteOpcodeEvent() throws VirtualMachineState.VMException {
            model.executeOpcode();
        }
        public int sendGetCurrentExecutingLineEvent() {
            return model.getCurrentExecutingLineNumber();
        }
        public void sendSetRegisterValueEvent(Registers r, int value) {
            model.setRegisterValue(r, value);
        }
        public int sendGetRegisterValueEvent(Registers r) {
            return model.getRegisterValue(r);
        }
        public void sendKeyPressedEvent(KeyEvent keyEvent) {
            model.keyPressed(keyEvent);
        }
        public void sendKeyReleasedEvent(KeyEvent keyEvent) {
            model.keyReleased(keyEvent);
        }
        public int[][] sendGetScreenEvent() {
            return model.getScreen();
        }
        public int sendGetPixelEvent(int x, int y) {
            return model.getPixel(x, y);
        }
    }

    public static class ViewForModel {
        private ViewInterface view = null;

        public ViewForModel(ViewInterface view) {
            this.view = view;
        }
        public void sendSetLineColorEvent(int linen, Color color) {
            SwingUtilities.invokeLater(() -> view.setLineColor(linen, color));
        }
        public void sendClearLineColorsEvent() {
            SwingUtilities.invokeLater(() -> view.clearLineColors());
        }
        public void sendSetPixelRGBEvent(int x, int y, int value) {
            SwingUtilities.invokeLater(() -> view.setPixelRGB(x, y, value));
        }
        public void sendClearScreenEvent() {
            SwingUtilities.invokeLater(() -> view.clearScreen());
        }

    }
}
