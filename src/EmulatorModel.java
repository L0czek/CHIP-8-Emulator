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

public class EmulatorModel implements ModelInterface {
    private byte[] byteCode;
    private String[] assemblyLines;
    private VirtualMachineState vmState;
    private Assembler.Assembled vmCode;

    private Disassembler disassembler;
    private Assembler assembler;

    Optional<Events.ViewForModel> events = Optional.empty();

    public EmulatorModel() throws Exception {
        byteCode = new byte[]{};
        assemblyLines = new String[]{};

        disassembler = new Disassembler(InstructionFactory.factoriesByIndex());
        assembler = new Assembler(InstructionFactory.factoriesByMnemonic());

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if(vmState != null) {
            vmState.keyPressed(keyEvent);
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        if(vmState != null) {
            vmState.keyReleased(keyEvent);
        }
    }

    @Override
    public int[][] getScreen() {
        return vmState.getScreen();
    }

    @Override
    public int getPixel(int x, int y) {
        return vmState.getPixel(x,y);
    }

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

    @Override
    public void setRegisterValue(Registers r, int value) {
        if(r != Registers.Invalid && vmState != null)
            vmState.setReg(Registers.toInt(r), value);
    }

    @Override
    public String loadAssemblyFromFile(String path) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(path)));
        assemblyLines = content.split("\n");
        return content;
    }

    @Override
    public String loadByteCodeFromFile(String path) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(path));
        String disassembly = disassembler.disassemble(content);
        assemblyLines = disassembly.split("\n");
        return disassembly;
    }

    @Override
    public void saveAssemblyToFile(String path, String assembly) throws IOException {
        Files.write(Paths.get(path), Collections.singleton(assembly));
    }

    @Override
    public void saveByteCodeToFile(String path, String assembly) throws Assembler.AssemblerException, IOException {
        byteCode = assembler.generateByteCode(assembly, 0);
        Files.write(Paths.get(path), byteCode);
    }

    @Override
    public void startEmulation(String assembly) throws Assembler.AssemblerException {
        vmCode = assembler.generateOutput(assembly, 0);
        vmState = new VirtualMachineState(vmCode, disassembler, events);
    }

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

    @Override
    public void executeOpcode() throws VirtualMachineState.VMException {
        vmState.executeInstruction();
    }

    @Override
    public int getCurrentExecutingLineNumber() {
        try {
            return vmCode.getLineNumbers()[vmState.getIp() - 0x200];
        } catch(ArrayIndexOutOfBoundsException e){
            return 0;
        }
    }

    @Override
    public void setupEventHandlers(ViewInterface view) {
        events = Optional.of(new Events.ViewForModel(view));
    }
}
