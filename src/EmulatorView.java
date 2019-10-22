import javafx.stage.FileChooser;

import javax.swing.*;
import javax.swing.text.html.Option;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Optional;

public final class EmulatorView extends JFrame implements ViewInterface  {
    private Menu menu = null;
    private AssemblyView assemblyView = null;
    private Screen screen = null;

    interface LoadFileEvent {
        void send(String text);
    }

    interface EmulationRunEvent {
        void send();
    }

    private Optional<LoadFileEvent> loadAssemblyEvent;
    private Optional<LoadFileEvent> loadByteCodeEvent;
    private Optional<EmulationRunEvent> emulationRunEvent;

    public EmulatorView() {
        super("Chip-8 Emulator");
        setSize(800, 600);
        setLayout(new GridBagLayout());

        setupMenu();
        setupAssemblyView();
        setupScreen();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        setVisible(true);
    }

    @Override
    public void setupEventHandlers(ControllerInterface controller) {
        loadAssemblyEvent = Optional.of(controller::loadAssemblyFromFile);
        loadByteCodeEvent = Optional.of(controller::loadByteCodeFromFile);
        emulationRunEvent = Optional.of(controller::runEmulation);
    }

    private void setupMenu() {
        menu = new Menu();
        final GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.gridx = 0;
        add(menu, gc);
    }

    private void setupScreen() {
        screen = new Screen(64, 32);
        final GridBagConstraints gc = new GridBagConstraints();
        gc.gridx=1;
        gc.gridy=1;
        screen.scaleTo(64*10, 32*10);
        add(screen, gc);
    }

    private void setupAssemblyView() {
        assemblyView = new AssemblyView();

        final GridBagConstraints gc = new GridBagConstraints();
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.gridy=1;
        gc.gridx = 0;
        add(assemblyView, gc);
    }

    @Override
    public String getAssembly() {
        return assemblyView.getText();
    }
    @Override
    public void setAssembly(String assembly) {
        assemblyView.setText(assembly);
    }
    @Override
    public void setPixelRGB(int x, int y, int value) {
        screen.setPixel(x, y, value);
        repaint();
    }
    @Override
    public void clearScreen() {
        screen.clear();
        repaint();
    }
    @Override
    public int getPixelRGB(int x, int y) {
        return screen.getPixel(x, y);
    }

    @Override
    public void reportError(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    private class AssemblyView extends JPanel {
        private JTextArea content;
        private JScrollPane viewPort;

        public AssemblyView() {
            content = new JTextArea(10, 10);
            viewPort = new JScrollPane(content);
            viewPort.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setLayout(new GridBagLayout());
            final GridBagConstraints gc = new GridBagConstraints();
            gc.fill = GridBagConstraints.BOTH;
            gc.weightx = 1;
            gc.weighty = 1;

            add(viewPort, gc);
        }

        public String getText() {
            return content.getText();
        }
        public void setText(String text) {
            content.setText(text);
        }
    }

    private class Screen extends JPanel {
        private BufferedImage img = null;
        private Dimension scaled = null;

        public Screen(int width, int height) {
            scaled = new Dimension(width, height);
            img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        @Override
        protected void paintComponent(Graphics gr) {
            super.paintComponent(gr);
            var scaledImg = img.getScaledInstance(scaled.width, scaled.height, Image.SCALE_FAST);
            gr.drawImage(scaledImg, 0, 0, this);
        }

        public void setPixel(int x, int y, int value) {
            img.setRGB(x, y, value);
        }
        public int getPixel(int x, int y) { return img.getRGB(x, y); }
        public void clear() {
            Graphics2D gr2d = img.createGraphics();
            gr2d.setColor(new Color(0, 0, 0));
            gr2d.clearRect(0, 0, img.getWidth(), img.getHeight());
        }
        public void scaleTo(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            scaled.width = width;
            scaled.height = height;
        }

    }

    private class Menu extends JPanel {
        private JButton openByteCodeFileButton = null;
        private JButton openAssemblyFileButton = null;
        private JButton runButton = null;

        public Menu() {
            openByteCodeFileButton = new JButton("Open ByteCode file");
            runButton = new JButton("RUN");
            openAssemblyFileButton = new JButton("Open Assembly file");

            setupEventHandlers();

            setLayout(new FlowLayout());

            add(openAssemblyFileButton);
            add(openByteCodeFileButton);
            add(runButton);
        }

        private void setupEventHandlers() {
            openAssemblyFileButton.addActionListener(event -> openAssemblyFile());
            openByteCodeFileButton.addActionListener(event -> openByteCodeFile());
            runButton.addActionListener(event -> runCode());
        }
        private void openAssemblyFile() {
            var filePath = getFilePathDialog();
            filePath.ifPresent(path -> loadAssemblyEvent.ifPresent(event -> event.send(path)));
        }
        private void openByteCodeFile() {
            var filePath = getFilePathDialog();
            filePath.ifPresent(path -> loadByteCodeEvent.ifPresent(event -> event.send(path)));
        }

        private void runCode() {
            emulationRunEvent.ifPresent(EmulationRunEvent::send);
        }

        private Optional<String> getFilePathDialog() {
            JFileChooser dialog = new JFileChooser();
            int result = dialog.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                return Optional.of(dialog.getSelectedFile().getAbsolutePath());
            }
            return Optional.empty();
        }
    }
}
