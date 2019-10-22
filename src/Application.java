import javax.swing.*;

public class Application {
    private ControllerInterface controller = null;
    private ModelInterface model = null;
    private ViewInterface view = null;

    public Application(ControllerInterface controller_, ModelInterface model_, ViewInterface view_) {
        this.controller = controller_;
        this.model = model_;
        this.view = view_;

        view.setupEventHandlers(controller);
        controller.setupEventHandlers(view);
        controller.setupEventHandlers(model);
    }
}
