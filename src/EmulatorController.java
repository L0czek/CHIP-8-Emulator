import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * controller in MVC
 */
public class EmulatorController implements ControllerInterface {

    private Optional<Events.ViewForController> viewEvents = Optional.empty();
    private Optional<Events.ModelForController> modelEvents = Optional.empty();
    private ExecutorService taskPool;

    private enum State {
        Ready,
        Running,
        Stop
    }

    private State state = State.Ready;

    /**
     * creates controller
     */
    public EmulatorController() {
        taskPool = Executors.newFixedThreadPool(4);
    }

    /**
     * sets current emulation state
     * @param s state to be set
     */
    private synchronized void setState(State s) {
        if(viewEvents.isPresent()) {
            Events.ViewForController view = viewEvents.get();
            switch(s) {
                case Ready: view.sendSetStatusTextEvent("Ready"); break;
                case Running: view.sendSetStatusTextEvent("Running"); break;
                case Stop: view.sendSetStatusTextEvent("Stop"); break;
            }
        }
        state = s;
    }

    /**
     * get current emulation state
     * @return current state
     */
    private synchronized State getState() { return state; }

    private void markAsCodeImpl(int linen) {
        if(getState() == State.Ready) {
            if (modelEvents.isPresent() && viewEvents.isPresent()) {
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
    }

    /**
     * converts assembly line of passed number to code (from emit byte instruction to valid instruction or the same emit byte on disassembly error)
     * @param linen
     */
    @Override
    public void markAsCode(int linen) {
        taskPool.execute(() -> markAsCodeImpl(linen));
    }

    private void markAsDataImpl(int linen) {
        if(getState() == State.Ready) {
            if (modelEvents.isPresent() && viewEvents.isPresent()) {
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
    }

    /**
     * change assembly line of passed number to emit byte meta instruction
     * @param linen
     */
    @Override
    public void markAsData(int linen) {
        taskPool.execute(() -> markAsDataImpl(linen));
    }

    private void loadAssemblyFromFileImpl(String path) {
        if(getState() == State.Ready) {
            if (modelEvents.isPresent()) {
                Events.ModelForController events = modelEvents.get();
                try {
                    String assembly = events.sendLoadAssemblyFromFileEvent(path);
                    viewEvents.ifPresent(e -> e.sendSetAssemblyEvent(assembly));
                } catch (IOException error) {
                    viewEvents.ifPresent(e -> e.sendReportErrorEvent("Cannot read bytecode from file" + error.getMessage()));
                }
            }
        }
    }

    /**
     * handles load assembly from file button on view
     * @param path specified path by user
     */
    @Override
    public void loadAssemblyFromFile(String path) {
        taskPool.execute(() -> loadAssemblyFromFileImpl(path));
    }

    private void loadBytecodeFromFileImpl(String path) {
        if(getState() == State.Ready) {
            if (modelEvents.isPresent()) {
                Events.ModelForController events = modelEvents.get();
                try {
                    String assembly = events.sendLoadByteCodeFromFileEvent(path);
                    viewEvents.ifPresent(e -> e.sendSetAssemblyEvent(assembly));
                } catch (IOException error) {
                    viewEvents.ifPresent(e -> e.sendReportErrorEvent("Cannot read bytecode from file" + error.getMessage()));
                }
            }
        }
    }
    /**
     * handles load bytecode from file button on view
     * @param path specified path by user
     */
    @Override
    public void loadByteCodeFromFile(String path) {
        taskPool.execute(() -> loadBytecodeFromFileImpl(path));
    }

    private void saveAssemblyToFileImpl(String path) {
        if(getState() == State.Ready) {
            if (modelEvents.isPresent() && viewEvents.isPresent()) {
                Events.ModelForController model = modelEvents.get();
                Events.ViewForController view = viewEvents.get();
                try {
                    model.sendSaveAssemblyToFileEvent(path, view.sendGetAssemblyEvent());
                } catch (IOException error) {
                    view.sendReportErrorEvent("Cannot write to file: " + error.getMessage());
                }
            }
        }
    }
    /**
     * handles save assembly from file button on view
     * @param path specified path by user
     */
    @Override
    public void saveAssemblyToFile(String path) {
        taskPool.execute(() -> saveAssemblyToFileImpl(path));
    }

    private void saveByteCodeToFileImpl(String path) {
        if(getState() == State.Ready) {
            if (modelEvents.isPresent() && viewEvents.isPresent()) {
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
    }
    /**
     * handles save bytecode from file button on view
     * @param path specified path by user
     */
    @Override
    public void saveByteCodeToFile(String path) {
        taskPool.execute(() -> saveByteCodeToFileImpl(path));
    }

    private void contImpl() {
        if(getState() == State.Stop) {
            if (modelEvents.isPresent()) {
                Events.ModelForController model = modelEvents.get();
                setState(State.Running);
                do {
                    try {
                        model.sendExecuteOpcodeEvent();
                    } catch (VirtualMachineState.VMException e) {

                    }
                } while (getState() == State.Running);
                viewEvents.ifPresent(view -> updateUI(model, view));
            }
        }
    }

    /**
     * handles user pressing continue button
     */
    @Override
    public void cont() {
        taskPool.execute(() -> contImpl());
    }

    private void updateUI(Events.ModelForController model, Events.ViewForController view) {
        view.sendClearLineColorsEvent();
        view.sendSetLineColorEvent(model.sendGetCurrentExecutingLineEvent(), Color.GREEN);
        Registers.iterate().forEach(r ->
                view.sendSetRegisterValueEvent(r,
                        model.sendGetRegisterValueEvent(r)
                )
        );

    }

    private void stepInImpl() {
        if(getState() == State.Stop) {
            if (modelEvents.isPresent() && viewEvents.isPresent()) {
                Events.ModelForController model = modelEvents.get();
                Events.ViewForController view = viewEvents.get();
                try {
                    model.sendExecuteOpcodeEvent();
                } catch (VirtualMachineState.VMException e) {

                }
                updateUI(model, view);
            }
        }
    }

    /**
     * handles uer pressing step in button
     */
    @Override
    public void stepIn() {
        taskPool.execute(() -> stepInImpl());
    }

    private void stepOverImpl() {
        if(getState() == State.Stop) {
            if (modelEvents.isPresent()) {
                Events.ModelForController model = modelEvents.get();
                int endIp = model.sendGetRegisterValueEvent(Registers.ip) + 2;
                setState(State.Running);
                do {
                    try {
                        model.sendExecuteOpcodeEvent();
                    } catch (VirtualMachineState.VMException ignored) {

                    }
                } while (model.sendGetRegisterValueEvent(Registers.ip) != endIp && getState() == State.Running);
                viewEvents.ifPresent(viewForController -> updateUI(model, viewForController));
                setState(State.Stop);
            }
        }
    }

    /**
     * handles user pressing step over
     */
    @Override
    public void stepOver() {
        taskPool.execute(this::stepOverImpl);
    }


    private void stopImpl() {
        setState(State.Stop);
    }
    /**
     * handles user pressing stop button
     */
    @Override
    public void stop() {
        taskPool.execute(this::stopImpl);
    }

    private void setRegisterValueImpl(Registers r, int value) {
        if(getState() == State.Stop) {
            modelEvents.ifPresent(model -> model.sendSetRegisterValueEvent(r, value));
        }
    }

    /**
     * handles user setting new register values in view
     * @param r register to be changed
     * @param value new value
     */
    @Override
    public void setRegisterValue(Registers r, int value) {
        taskPool.execute(() -> setRegisterValueImpl(r, value));
    }

    private void runEmulationImpl() {
        if(getState() == State.Ready) {
            if (modelEvents.isPresent() && viewEvents.isPresent()) {
                Events.ModelForController model = modelEvents.get();
                Events.ViewForController view = viewEvents.get();
                view.sendClearLineColorsEvent();
                try {
                    model.sendStartEmulationEvent(view.sendGetAssemblyEvent());
                    view.sendDisableAssemblerEditingEvent();
                    view.sendSetLineColorEvent(model.sendGetCurrentExecutingLineEvent(), Color.GREEN);
                    setState(State.Stop);
                } catch (Assembler.AssemblerException error) {
                    view.sendSetLineColorEvent(error.linen, Color.RED);
                    view.sendSetStatusTextEvent(error.msg);
                }
            }
        }
    }

    /**
     * handles user pressing run button
     */
    @Override
    public void runEmulation() {
        taskPool.execute(this::runEmulationImpl);
    }

    private void exitEmulationImpl() {
        if(getState() == State.Stop || getState() == State.Running) {
            setState(State.Ready);
            if(viewEvents.isPresent()) {
                Events.ViewForController view = viewEvents.get();
                view.sendEnableAssemblerEditingEvent();
                view.sendClearScreenEvent();
                view.sendClearLineColorsEvent();
            }

        }
    }

    /**
     * handles user pressing exit button
     */
    @Override
    public void exitEmulation() {
        taskPool.execute(this::exitEmulationImpl);
    }

    /**
     * setting up event handlers for view
     * @param view view event handlers
     */
    @Override
    public void setupEventHandlers(ViewInterface view) {
        viewEvents = Optional.of(new Events.ViewForController(view));
    }
    /**
     * setting up event handlers for model
     * @param model model event handlers
     */
    @Override
    public void setupEventHandlers(ModelInterface model) {
        modelEvents = Optional.of(new Events.ModelForController(model));
    }
    /**
     * handling keyboard key press event
     * @param keyEvent key press event
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        taskPool.execute(() -> modelEvents.ifPresent(model -> model.sendKeyPressedEvent(keyEvent)));
    }
    /**
     * handling keyboard key released event
     * @param keyEvent key released event send
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        taskPool.execute(() -> modelEvents.ifPresent(model -> model.sendKeyReleasedEvent(keyEvent)));
    }
}
