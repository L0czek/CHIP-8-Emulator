import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;

public final class EmulatorView extends JFrame implements ViewInterface  {
    private Menu menu = null;
    private AssemblyView assemblyView = null;
    private DebugerView debugView = null;

    interface FileEvent {
        void send(String text);
    }

    interface EmulationRunEvent {
        void send();
    }

    private Optional<FileEvent> loadAssemblyEvent;
    private Optional<FileEvent> loadByteCodeEvent;
    private Optional<FileEvent> saveAssemblyEvent;
    private Optional<FileEvent> saveByteCodeEvent;
    private Optional<EmulationRunEvent> emulationRunEvent;

    private static GridBagConstraints makeLayoutConstrains(int x, int y) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        return gc;
    }

    private static GridBagConstraints makeLayoutConstrains(int x, int y, int ex, int ey) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = x;
        gc.gridy = y;
        gc.weightx = ex;
        gc.weighty = ey;
        gc.fill = GridBagConstraints.BOTH;
        return gc;
    }

    public EmulatorView() {
        super("Chip-8 Emulator");
        setSize(800, 600);
        setLayout(new GridBagLayout());

        setupMenu();
        setupAssemblyView();
        setupDebuggerView();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        setVisible(true);
    }

    @Override
    public void setupEventHandlers(ControllerInterface controller) {
        loadAssemblyEvent = Optional.of(controller::loadAssemblyFromFile);
        loadByteCodeEvent = Optional.of(controller::loadByteCodeFromFile);
        saveAssemblyEvent = Optional.of(controller::saveAssemblyToFile);
        saveByteCodeEvent = Optional.of(controller::saveByteCodeToFile);
        emulationRunEvent = Optional.of(controller::runEmulation);
    }

    private void setupDebuggerView() {
        debugView = new DebugerView();
        add(debugView, makeLayoutConstrains(1, 1));
    }

    private void setupMenu() {
        menu = new Menu();
        add(menu, makeLayoutConstrains(0,0));
    }



    private void setupAssemblyView() {
        assemblyView = new AssemblyView();
        add(assemblyView, makeLayoutConstrains(0, 1, 1, 1));
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
        //screen.setPixel(x, y, value);
        repaint();
    }
    @Override
    public void clearScreen() {
        //screen.clear();
        repaint();
    }
    @Override
    public int getPixelRGB(int x, int y) {
        return 1;//screen.getPixel(x, y);
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
            add(viewPort, makeLayoutConstrains(0, 0, 1, 1));
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
            setSize(new Dimension(width, height));
            scaled.width = width;
            scaled.height = height;
        }

    }

    private class Menu extends JPanel {
        private JButton openByteCodeFileButton = null;
        private JButton openAssemblyFileButton = null;
        private JButton saveByteCodeFileButton = null;
        private JButton saveAssemblyFileButton = null;

        public Menu() {
            openByteCodeFileButton = new JButton("Open ByteCode file");
            openAssemblyFileButton = new JButton("Open Assembly file");
            saveAssemblyFileButton = new JButton("Save Assembly file");
            saveByteCodeFileButton = new JButton("Save ByteCode file");

            setupEventHandlers();

            setLayout(new FlowLayout());

            add(openAssemblyFileButton);
            add(openByteCodeFileButton);
            add(saveAssemblyFileButton);
            add(saveByteCodeFileButton);
        }

        private void setupEventHandlers() {
            openAssemblyFileButton.addActionListener(event -> openAssemblyFile());
            openByteCodeFileButton.addActionListener(event -> openByteCodeFile());
            saveByteCodeFileButton.addActionListener(event -> saveByteCodeFile());
            saveAssemblyFileButton.addActionListener(event -> saveAssemblyFile());
        }
        private void openAssemblyFile() {
            var filePath = getOpenFilePathDialog();
            filePath.ifPresent(path -> loadAssemblyEvent.ifPresent(event -> event.send(path)));
        }
        private void openByteCodeFile() {
            var filePath = getOpenFilePathDialog();
            filePath.ifPresent(path -> loadByteCodeEvent.ifPresent(event -> event.send(path)));
        }

        private void saveByteCodeFile() {
            var filePath = getSaveFilePathDialog();
            saveByteCodeEvent.ifPresent(event -> filePath.ifPresent(path -> event.send(path)));
        }

        private void saveAssemblyFile() {
            var filePath = getSaveFilePathDialog();
            saveAssemblyEvent.ifPresent(event -> filePath.ifPresent(path -> event.send(path)));
        }

        private Optional<String> getOpenFilePathDialog() {
            JFileChooser dialog = new JFileChooser();
            int result = dialog.showOpenDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                return Optional.of(dialog.getSelectedFile().getAbsolutePath());
            }
            return Optional.empty();
        }

        private Optional<String> getSaveFilePathDialog() {
            JFileChooser dialog = new JFileChooser();
            int result = dialog.showSaveDialog(this);
            if(result == JFileChooser.APPROVE_OPTION) {
                return Optional.of(dialog.getSelectedFile().getAbsolutePath());
            }
            return Optional.empty();
        }
    }
    private class DebugerView extends JPanel {
        private Screen screen = null;

        private JButton stepInButton = null;
        private JButton stepOverButton = null;
        private JButton continueButton = null;
        private JButton stopButton = null;
        private JButton runButton = null;

        ArrayList<JLabel> registerLabels = null;
        ArrayList<JLabel> registerValues = null;
        ArrayList<JTextField> registerValuesToSet = null;
        ArrayList<JButton> registerSetButton = null;
        ArrayList<JPanel> panels = null;

        public DebugerView() {
            setLayout(new GridBagLayout());

            screen = new Screen(64, 32);
            screen.scaleTo(64*10, 32* 10);

            stepInButton = new JButton("Step IN");
            stepOverButton = new JButton("Step OVER");
            continueButton = new JButton("Continue");
            stopButton = new JButton("STOP");
            runButton = new JButton("RUN");

            registerLabels = new ArrayList<>();
            registerValues = new ArrayList<>();
            registerValuesToSet = new ArrayList<>();
            registerSetButton = new ArrayList<>();
            panels = new ArrayList<>();

            JPanel controlPanel = new JPanel();
            controlPanel.setLayout(new FlowLayout());
            controlPanel.add(runButton);
            controlPanel.add(stepInButton);
            controlPanel.add(stepOverButton);
            controlPanel.add(continueButton);
            controlPanel.add(stopButton);

            add(screen, makeLayoutConstrains(0, 0));
            add(controlPanel, makeLayoutConstrains(0, 1));

            for(int i=0; i < 16; ++i) {
                var label = new JLabel(String.format("v%d", i));
                var value = new JLabel("0000");
                var valueToSet = new JTextField("0000");
                var button = new JButton("Set Value");

                registerLabels.add(label);
                registerValues.add(value);
                registerValuesToSet.add(valueToSet);
                registerSetButton.add(button);

                var panel = new JPanel();
                panel.setLayout(new FlowLayout());
                panel.add(label);
                panel.add(value);
                panel.add(valueToSet);
                panel.add(button);
                panels.add(panel);


                int finalI = i;
                button.addActionListener(new ActionListener() {
                    final int index = finalI;

                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        setRegisterValue(index);
                    }
                });
            }

            for(int i=0; i < panels.size()/2; ++i) {
                var panel = new JPanel();
                panel.setLayout(new FlowLayout());
                panel.add(panels.get(2 * i + 0));
                panel.add(panels.get(2 * i + 1));
                add(panel, makeLayoutConstrains(0, i+2));
            }

        }


        private void setRegisterValue(int index) {
            System.out.println(index);
        }
    }
}
