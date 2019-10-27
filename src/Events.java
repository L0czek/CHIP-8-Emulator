import java.awt.event.TextEvent;

public class Events {
    interface TextSendEvent {
        void send(String arg);
    }

    public static class ViewEvents {
        private TextSendEvent loadAssemblyEvent;
        private TextSendEvent loadByteCodeEvent;
        private TextSendEvent saveAssemblyEvent;
        private TextSendEvent saveByteCodeEvent;

        public ViewEvents(ControllerInterface controller) {
            loadAssemblyEvent = controller::loadAssemblyFromFile;
            loadByteCodeEvent = controller::loadByteCodeFromFile;
            saveAssemblyEvent = controller::saveAssemblyToFile;
            saveByteCodeEvent = controller::saveByteCodeToFile;
        }


    }
}
