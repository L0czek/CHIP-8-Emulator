import java.awt.*;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmulatorController implements ControllerInterface {

    private Optional<Events.ViewForController> viewEvents = Optional.empty();
    private Optional<Events.ModelForController> modelEvents = Optional.empty();
    private ExecutorService taskPool;

    public EmulatorController() {
        taskPool = Executors.newFixedThreadPool(1);
    }

    private void markAsCodeImpl(int linen) {
        if(modelEvents.isPresent() && viewEvents.isPresent()) {
            Events.ViewForController view = viewEvents.get();
            Events.ModelForController model = modelEvents.get();
            try {
                view.sendSetAssemblyEvent(model.sendRecompileAsCode(linen, view.sendGetAssemblyEvent()));
            } catch (Assembler.AssemblerException error) {
                view.sendSetStatusTextEvent(error.msg);
                view.sendSetLineColorEvent(error.linen, Color.RED);
            }
        }
    }

    @Override
    public void markAsCode(int linen) {
        taskPool.execute(() -> markAsCodeImpl(linen));
    }

    private void markAsDataImpl(int linen) {
        if(modelEvents.isPresent() && viewEvents.isPresent()) {
            Events.ViewForController view = viewEvents.get();
            Events.ModelForController model = modelEvents.get();
            try {
                view.sendSetAssemblyEvent(model.sendRecompileAsData(linen, view.sendGetAssemblyEvent()));
            } catch (Assembler.AssemblerException error) {
                view.sendSetStatusTextEvent(error.msg);
                view.sendSetLineColorEvent(error.linen, Color.RED);
            }
        }
    }

    @Override
    public void markAsData(int linen) {
        taskPool.execute(() -> markAsDataImpl(linen));
    }

    private void loadAssemblyFromFileImpl(String path) {
        if(modelEvents.isPresent()) {
            Events.ModelForController events = modelEvents.get();
            try {
                String assembly = events.sendLoadAssemblyFromFileEvent(path);
                viewEvents.ifPresent(e -> e.sendSetAssemblyEvent(assembly));
            } catch (IOException error) {
                viewEvents.ifPresent(e -> e.sendReportErrorEvent("Cannot read bytecode from file" + error.getMessage()));
            }
        }
    }

    @Override
    public void loadAssemblyFromFile(String path) {
        taskPool.execute(() -> loadAssemblyFromFileImpl(path));
    }

    private void loadBytecodeFromFileImpl(String path) {
        if(modelEvents.isPresent()) {
            Events.ModelForController events = modelEvents.get();
            try {
                String assembly = events.sendLoadByteCodeFromFileEvent(path);
                viewEvents.ifPresent(e -> e.sendSetAssemblyEvent(assembly));
            } catch (IOException error) {
                viewEvents.ifPresent(e -> e.sendReportErrorEvent("Cannot read bytecode from file" + error.getMessage()));
            }
        }
    }

    @Override
    public void loadByteCodeFromFile(String path) {
        taskPool.execute(() -> loadBytecodeFromFileImpl(path));
    }

    private void saveAssemblyToFileImpl(String path) {
        if(modelEvents.isPresent() && viewEvents.isPresent()) {
            Events.ModelForController model = modelEvents.get();
            Events.ViewForController view = viewEvents.get();
            try {
                model.sendSaveAssemblyToFileEvent(path, view.sendGetAssemblyEvent());
            } catch (IOException error) {
                view.sendReportErrorEvent("Cannot write to file: " + error.getMessage());
            }
        }
    }

    @Override
    public void saveAssemblyToFile(String path) {
        taskPool.execute(() -> saveByteCodeToFileImpl(path));
    }

    private void saveByteCodeToFileImpl(String path) {
        if(modelEvents.isPresent() && viewEvents.isPresent()) {
            Events.ModelForController model = modelEvents.get();
            Events.ViewForController view = viewEvents.get();
            try {
                view.sendClearLineColorsEvent();
                model.sendSaveByteCodeToFileEvent(path, view.sendGetAssemblyEvent());
            } catch (Assembler.AssemblerException error) {
                view.sendSetLineColorEvent(error.linen, Color.RED);
                view.sendSetStatusTextEvent(error.msg);
            } catch (IOException error) {
                view.sendReportErrorEvent("Cannot save to file: " + error.getMessage());
            }
        }
    }

    @Override
    public void saveByteCodeToFile(String path) {
        taskPool.execute(() -> saveByteCodeToFileImpl(path));
    }

    @Override
    public void cont() {

    }

    @Override
    public void stepIn() {

    }

    @Override
    public void stepOver() {

    }

    @Override
    public void stop() {
        viewEvents.ifPresent(events -> events.sendEnableAssemblerEditingEvent());
    }

    @Override
    public void setRegisterValue(Registers r, int value) {
        System.out.println(String.format("Set %s to %x", r.toString() ,value));
    }

    private void runEmulationImpl() {
        if(modelEvents.isPresent() && viewEvents.isPresent()) {
            Events.ModelForController model = modelEvents.get();
            Events.ViewForController view = viewEvents.get();
            view.sendClearLineColorsEvent();
            try {
                model.sendStartEmulationEvent(view.sendGetAssemblyEvent());
                view.sendDisableAssemblerEditingEvent();
            } catch (Assembler.AssemblerException error) {
                view.sendSetLineColorEvent(error.linen, Color.RED);
                view.sendSetStatusTextEvent(error.msg);
            }
        }
    }

    @Override
    public void runEmulation() {
        taskPool.execute(this::runEmulationImpl);
    }

    @Override
    public void setupEventHandlers(ViewInterface view) {
        viewEvents = Optional.of(new Events.ViewForController(view));
    }

    @Override
    public void setupEventHandlers(ModelInterface model) {
        modelEvents = Optional.of(new Events.ModelForController(model));
    }

}
