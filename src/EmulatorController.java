import java.util.Optional;

public class EmulatorController implements ControllerInterface {

    interface SendTextEvent {
        void send(String msg);
    }

    interface GetTextEvent {
        Optional<String> send();
    }

    private Optional<SendTextEvent> viewErrorEvent;
    private Optional<SendTextEvent> setAssemblyEvent;

    private Optional<SendTextEvent> modelLoadAssemblyEvent;
    private Optional<SendTextEvent> modelLoadByteCodeEvent;
    private Optional<GetTextEvent> modelGetAssemblyEvent;

    public EmulatorController() {
        viewErrorEvent = Optional.empty();
        setAssemblyEvent = Optional.empty();
        modelLoadAssemblyEvent = Optional.empty();
        modelLoadByteCodeEvent = Optional.empty();
        modelGetAssemblyEvent = Optional.empty();
    }

    @Override
    public void loadAssemblyFromFile(String path) {
        if(modelLoadAssemblyEvent.isPresent()) {
            modelLoadAssemblyEvent.get().send(path);
            var assembly = modelGetAssemblyEvent.get().send();
            if(assembly.isEmpty()) {
                viewErrorEvent.ifPresent(event -> event.send("Cannot read assembly file"));
            } else {
                setAssemblyEvent.ifPresent(event -> event.send(assembly.get()));
            }
        } else {
            viewErrorEvent.ifPresent(event -> event.send("No model was provided to controller"));
        }

    }

    @Override
    public void loadByteCodeFromFile(String path) {
        System.out.println(path);
    }

    @Override
    public void runEmulation() {
        System.out.println("run");
        viewErrorEvent.ifPresent(event -> event.send("running"));
    }

    @Override
    public void setupEventHandlers(ViewInterface view) {
        viewErrorEvent = Optional.of(view::reportError);
        setAssemblyEvent = Optional.of(view::setAssembly);
    }

    @Override
    public void setupEventHandlers(ModelInterface model) {
        modelLoadAssemblyEvent = Optional.of(model::loadAssemblyFromFile);
        modelLoadByteCodeEvent = Optional.of(model::loadByteCodeFromFile);
        modelGetAssemblyEvent = Optional.of(model::getAssembly);
    }
}
