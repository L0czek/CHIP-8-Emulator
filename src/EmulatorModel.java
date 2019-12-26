import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * class representing model in MVC
 */
public class EmulatorModel implements ModelInterface {
    private byte[] byteCode;
    private String[] assemblyLines;
    private VirtualMachineState vmState;
    private Assembler.Assembled vmCode;

    private Disassembler disassembler;
    private Assembler assembler;

    Optional<Events.ViewForModel> events = Optional.empty();

    /**
     * creates model
     * @throws Exception throws when provided invalid instruction factory list
     */
    public EmulatorModel() throws Exception {
        byteCode = new byte[]{};
        assemblyLines = new String[]{};

        disassembler = new Disassembler(InstructionFactory.factoriesByIndex());
        assembler = new Assembler(InstructionFactory.factoriesByMnemonic());

    }

    /**
     * handles key press event
     * @param keyEvent event send
     */
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(vmState != null) {
            vmState.keyPressed(keyEvent);
        }
    }

    /**
     * handles key released event
     * @param keyEvent event send
     */
    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(vmState != null) {
            vmState.keyReleased(keyEvent);
        }
    }

    /**
     * get the screen as 2 dimensional array
     * @return screen buffer as int array
     */
    @Override
    public int[][] getScreen() {
        return vmState.getScreen();
    }

    /**
     * get pixel value
     * @param x x coordinate
     * @param y y coordinate
     * @return RGB value
     */
    @Override
    public int getPixel(int x, int y) {
        return vmState.getPixel(x,y);
    }

    /**
     * get selected register value
     * @param r selected register
     * @return register value
     */
    @Override
    public int getRegisterValue(Registers r) {
        if(r == Registers.I) {
            return vmState.getRegI();
        } else if(r == Registers.ip) {
            return vmState.getIp();
        } else {
            return vmState.getReg(Registers.toInt(r));
        }
    }

    /**
     * set selected register value
     * @param r selected register
     * @param value new value
     */
    @Override
    public void setRegisterValue(Registers r, int value) {
        if(r != Registers.Invalid && vmState != null)
            vmState.setReg(Registers.toInt(r), value);
    }

    /**
     * load assembly from file
     * @param path path to assembly file
     * @return loaded assembler to display
     * @throws IOException throws on filesystem error
     */
    @Override
    public String loadAssemblyFromFile(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        assemblyLines = content.split("\n");
        return content;
    }
    /**
     * load bytecode from file
     * @param path path to bytecode file
     * @return disassembled bytecode to display
     * @throws IOException throws on filesystem error
     */
    @Override
    public String loadByteCodeFromFile(String path) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(path));
        String disassembly = disassembler.disassemble(content);
        assemblyLines = disassembly.split("\n");
        return disassembly;
    }

    /**
     * save assembly to file
     * @param path path to file selected by user
     * @param assembly assembly from view
     * @throws IOException throws on filesystem error
     */
    @Override
    public void saveAssemblyToFile(String path, String assembly) throws IOException {
        Files.write(Paths.get(path), Collections.singleton(assembly));
    }
    /**
     * save assembled code to file
     * @param path path to file selected by user
     * @param assembly assembly from view
     * @throws IOException throws on filesystem error
     */
    @Override
    public void saveByteCodeToFile(String path, String assembly) throws Assembler.AssemblerException, IOException {
        byteCode = assembler.generateByteCode(assembly, 0);
        Files.write(Paths.get(path), byteCode);
    }

    /**
     * handler user pressing run button
     * @param assembly
     * @throws Assembler.AssemblerException
     */
    @Override
    public void startEmulation(String assembly) throws Assembler.AssemblerException {
        vmCode = assembler.generateOutput(assembly, 0);
        vmState = new VirtualMachineState(vmCode, disassembler, events);
    }

    /**
     * function tries to disassemble selected assembly line as valid instruction
     * @param linen line number to change
     * @param assembly assembly to recompile
     * @return recompiled assembly
     * @throws Assembler.AssemblerException thrown on assembly error
     */
    @Override
    public String recompileAsCode(int linen, String assembly) throws Assembler.AssemblerException {
        assemblyLines = assembly.split("\n");
        StringBuilder builder = new StringBuilder();
        byte[] compiled = assembler.generateByteCode(assemblyLines[linen], linen);
        if(compiled.length == 0) {
            return assembly;
        }
        int index = 0;
        for(; index < linen; ++index) {
            builder.append(assemblyLines[index]).append("\n");
        }
        index = linen + 1;
        if(compiled.length == 1 && linen + 1 < assemblyLines.length) {
            byte[] addition = assembler.generateByteCode(assemblyLines[linen+1], linen+1);
            index++;
            if(addition.length == 1) {
                compiled = new byte[]{ compiled[0], addition[0] };
            } else if(addition.length == 2){
                compiled = new byte[]{ compiled[0], addition[0], addition[1] };
            } else {
                return assembly;
            }
        }
        for (String line : disassembler.disassemble(compiled).split("\n")) {
            builder.append(line).append("\n");
        }
        for(; index < assemblyLines.length; ++index) {
            builder.append(assemblyLines[index]).append("\n");
        }
        return assembler.fixOffsets(builder.toString());
    }
    /**
     * function tries to disassemble selected assembly line as emit byte meta instruction
     * @param linen line number to change
     * @param assembly assembly to recompile
     * @return recompiled assembly
     * @throws Assembler.AssemblerException thrown on assembly error
     */
    @Override
    public String recompileAsData(int linen, String assembly) throws Assembler.AssemblerException {
        assemblyLines = assembly.split("\n");
        byte[] compiled = assembler.generateByteCode(assemblyLines[linen], linen);
        if(compiled.length == 1) {
            return assembly;
        }
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < linen; ++i) { builder.append(assemblyLines[i]).append("\n"); }
        builder.append(disassembler.disassemble(compiled[0], 0));
        builder.append(disassembler.disassemble(compiled[1], 0));
        for(int i=linen+1; i < assemblyLines.length; ++i) { builder.append(assemblyLines[i]).append("\n"); }
        return assembler.fixOffsets(builder.toString());
    }

    /**
     * execute one instruction at current ip
     * @throws VirtualMachineState.VMException thrown by instruction
     */
    @Override
    public void executeOpcode() throws VirtualMachineState.VMException {
        vmState.executeInstruction();
    }

    /**
     * get assembly line of currently executing instruction
     * @return line number
     */
    @Override
    public int getCurrentExecutingLineNumber() {
        try {
            return vmCode.getLineNumbers()[vmState.getIp() - 0x200];
        } catch(ArrayIndexOutOfBoundsException e){
            return 0;
        }
    }

    /**
     * setup event handlers for view
     * @param view view part of MVC
     */
    @Override
    public void setupEventHandlers(ViewInterface view) {
        events = Optional.of(new Events.ViewForModel(view));
    }
}
