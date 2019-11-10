import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Optional;

public final class EmulatorView extends JFrame implements ViewInterface  {
    private Menu menu = null;
    private JLabel status = null;
    private AssemblyView assemblyView = null;
    private DebuggerView debugView = null;
    private Optional<Events.ForView> events = Optional.empty();

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
        setFocusable(true);

        setupMenu();
        setupStatus();
        setupAssemblyView();
        setupDebuggerView();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                System.out.println(keyEvent);
                onKeyPress(keyEvent);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setStatusText("READY");
        setVisible(true);
    }

    private void setupStatus() {
        status = new JLabel("");
        add(status, makeLayoutConstrains(1, 0));
    }

    private void onKeyPress(KeyEvent keyEvent) {
        int key = keyEvent.getKeyCode();
        System.out.println(key);
        switch (key) {
            case KeyEvent.VK_F2:
                events.ifPresent(events -> events.sendMarkAsDataEvent(assemblyView.getSelectedLine()));
                break;
            case KeyEvent.VK_F3:
                events.ifPresent(events -> events.sendMarkAsCodeEvent(assemblyView.getSelectedLine()));
                break;
            case KeyEvent.VK_F5:
                events.ifPresent(events -> events.sendContinueEvent());
                break;
            case KeyEvent.VK_F7:
                events.ifPresent(events -> events.sendStepInEvent());
                break;
            case KeyEvent.VK_F8:
                events.ifPresent(events -> events.sendStepOverEvent());
                break;
        }
    }

    @Override
    public void setupEventHandlers(ControllerInterface controller) {
        events = Optional.of(new Events.ForView(controller));
    }

    private void setupDebuggerView() {
        debugView = new DebuggerView();
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

    // =========================== EVENTS =================================
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
        debugView.setPixel(x, y, value);
        repaint();
    }
    @Override
    public void clearScreen() {
        debugView.clearScreen();
        repaint();
    }

    @Override
    public void setScreen(BufferedImage screen) {
        debugView.setScreen(screen);
    }

    @Override
    public int getPixelRGB(int x, int y) {
        return debugView.getPixel(x, y);
    }

    @Override
    public void reportError(String msg) {
        JOptionPane.showMessageDialog(null, msg);
        System.out.println("end");
    }

    @Override
    public void setLineColor(int linen, Color color) {
        assemblyView.setLineColor(linen, color);
    }

    @Override
    public void clearLineColors() {
        assemblyView.clearLinesColor();
    }

    @Override
    public void setStatusText(String text) {
        status.setText(text);
    }

    @Override
    public void setRegisterValue(Registers r, int value) {
        debugView.updateRegisterValue(r, value);
    }

    @Override
    public void enableAssemblerEditing() {
        assemblyView.enableInput();
    }

    @Override
    public void disableAssemblerEditing() {
        assemblyView.disableInput();
    }
    // ===============================================================

    private class AssemblyView extends JPanel {
        private JTextArea content;
        private JScrollPane viewPort;
        private boolean inputDisabled = false;

        public AssemblyView() {
            content = new JTextArea(10, 10);
            viewPort = new JScrollPane(content);
            viewPort.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            setLayout(new GridBagLayout());
            add(viewPort, makeLayoutConstrains(0, 0, 1, 1));
            content.addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent keyEvent) {
                    if(inputDisabled) {
                        keyEvent.consume();
                    }
                }

                @Override
                public void keyPressed(KeyEvent keyEvent) {
                    EmulatorView.this.onKeyPress(keyEvent);
                    if(inputDisabled) {
                        keyEvent.consume();
                    }
                }

                @Override
                public void keyReleased(KeyEvent keyEvent) {
                    if(inputDisabled) {
                        keyEvent.consume();
                    }
                }
            });
        }

        public String getText() {
            return content.getText();
        }
        public void setText(String text) {
            int savedPosition = content.getCaretPosition();
            content.setText(text);
            content.setCaretPosition(savedPosition);
        }
        public void setLineColor(int linen, Color color) {
            DefaultHighlighter.DefaultHighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(color);
            try {
                int start = content.getLineStartOffset(linen);
                int end = content.getLineEndOffset(linen);
                content.getHighlighter().addHighlight(start, end, painter);
            } catch (BadLocationException ignored) {

            }
        }
        public void clearLinesColor() {
            content.getHighlighter().removeAllHighlights();
        }
        public int getSelectedLine() {
            try {
                return content.getLineOfOffset(content.getCaretPosition());
            }catch(BadLocationException e) {
                return 0;
            }
        }
        public void enableInput() {
            content.setEditable(true);
            inputDisabled = false;
        }
        public void disableInput() {
            content.setEditable(false);
            inputDisabled = true;
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
            Image scaledImg = img.getScaledInstance(scaled.width, scaled.height, Image.SCALE_FAST);
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
        public void setImage(BufferedImage img) {
            this.img = img;
            repaint();
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
            Optional<String> filePath = getOpenFilePathDialog();
            events.ifPresent(events -> filePath.ifPresent(events::sendLoadAssemblyEvent));
        }
        private void openByteCodeFile() {
            Optional<String> filePath = getOpenFilePathDialog();
            events.ifPresent(events -> filePath.ifPresent(events::sendLoadByteCodeEvent));
        }

        private void saveByteCodeFile() {
            Optional<String> filePath = getSaveFilePathDialog();
            events.ifPresent(events -> filePath.ifPresent(events::sendSaveByteCodeEvent));
        }

        private void saveAssemblyFile() {
            Optional<String> filePath = getSaveFilePathDialog();
            events.ifPresent(events -> filePath.ifPresent(events::sendSaveAssemblyEvent));
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
    private class DebuggerView extends JPanel {
        private Screen screen = null;

        private JButton stepInButton = null;
        private JButton stepOverButton = null;
        private JButton continueButton = null;
        private JButton stopButton = null;
        private JButton runButton = null;

        ArrayList<JLabel> registerLabels = null;
        ArrayList<JLabel> registerValues = null;
        ArrayList<LimitedTextField> registerValuesToSet = null;
        ArrayList<JButton> registerSetButton = null;
        ArrayList<JPanel> panels = null;

        private class LimitedTextField extends JTextField {
            private int limit;

            public LimitedTextField(int limit) {
                this.limit = limit;
            }

            @Override
            protected Document createDefaultModel() {
                return new LimitedDocument();
            }

            private class LimitedDocument extends PlainDocument {
                @Override
                public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                    if(getLength() + str.length() <= limit) {
                        super.insertString(offs, str, a);
                    }
                }
            }
        }

        public DebuggerView() {
            setLayout(new GridBagLayout());

            screen = new Screen(64, 32);
            screen.scaleTo(64*10, 32* 10);

            stepInButton = new JButton("Step IN");
            stepOverButton = new JButton("Step OVER");
            continueButton = new JButton("Continue");
            stopButton = new JButton("STOP");
            runButton = new JButton("RUN");

            stepInButton.addActionListener(event -> stepIn());
            stepOverButton.addActionListener(event -> stepOver());
            continueButton.addActionListener(event -> cont());
            stopButton.addActionListener(event -> stop());
            runButton.addActionListener(event -> run());

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

            for(int i=0; i < 18; ++i) {
                JLabel label;
                switch(i) {
                    case 16: label = new JLabel("I"); break;
                    case 17: label = new JLabel("ip"); break;
                    default:
                        label = new JLabel(String.format("v%d", i));
                }
                JLabel value = new JLabel("0000");
                LimitedTextField valueToSet = new LimitedTextField(4);
                JButton button = new JButton("Set Value");

                registerLabels.add(label);
                registerValues.add(value);
                registerValuesToSet.add(valueToSet);
                registerSetButton.add(button);

                valueToSet.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent mouseEvent) {
                        System.out.println(mouseEvent);
                        valueToSet.setText("");
                    }

                    @Override
                    public void mousePressed(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent mouseEvent) {

                    }

                    @Override
                    public void mouseExited(MouseEvent mouseEvent) {

                    }
                });
                valueToSet.setText("0000");
                valueToSet.setColumns(4);

                JPanel panel = new JPanel();
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
                JPanel panel = new JPanel();
                panel.setLayout(new FlowLayout());
                panel.add(panels.get(2 * i + 0));
                panel.add(panels.get(2 * i + 1));
                add(panel, makeLayoutConstrains(0, i+2));
            }

        }

        private void stepIn() {
            events.ifPresent(Events.ForView::sendStepInEvent);
        }

        private void stepOver() {
            events.ifPresent(Events.ForView::sendStepOverEvent);
        }

        private void run() {
            events.ifPresent(Events.ForView::sendRunEvent);
        }

        private void cont() {
            events.ifPresent(Events.ForView::sendContinueEvent);
        }

        private void stop() {
            events.ifPresent(Events.ForView::sendStopEvent);
        }

        private void setReg(int n, int value) {
            String newValue = String.format("%X", value);
            registerValues.get(n).setText(newValue);
            registerValuesToSet.get(n).setText(newValue);
        }

        private void setRegisterValue(int index) {
            String text = registerValuesToSet.get(index).getText();
            try {
                events.ifPresent(
                        events -> events.sendSetRegisterValueEvent(
                                Registers.fromInt(index),
                                Integer.parseInt(text, 16)
                        )
                );
            } catch (NumberFormatException e) {
                registerValuesToSet.get(index).setText("");
                //setStatusText("Cannot convert to number");
                reportError("conversion error");
            }
        }

        public void updateRegisterValue(Registers r, int value) {
            registerValues.get(Registers.toInt(r)).setText(String.format("%04X", value));
        }

        public void setPixel(int x, int y, int value) {
            screen.setPixel(x, y, value);
        }

        public int getPixel(int x, int y) {
            return screen.getPixel(x, y);
        }

        public void clearScreen() {
            screen.clear();
        }
        public void setScreen(BufferedImage img) {
            screen.setImage(img);
        }
    }
}
