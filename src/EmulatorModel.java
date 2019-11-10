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
    private VirtualMachine virtualMachine;

    private Disassembler disassembler;
    private Assembler assembler;

    public EmulatorModel() throws Exception {
        byteCode = new byte[]{};
        assemblyLines = new String[]{};
        virtualMachine = new VirtualMachine();

        disassembler = new Disassembler(InstructionFactory.factoriesByIndex());
        assembler = new Assembler(InstructionFactory.factoriesByMnemonic());
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public BufferedImage getScreen() {
        return null;
    }

    @Override
    public int getRegisterValue(Registers r) {
        return 0;
    }

    @Override
    public void setRegisterValue(Registers r, int value) {

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
        byteCode = assembler.generateByteCode(assembly,0);

    }

    @Override
    public String recompileAsCode(int linen, String assembly) throws Assembler.AssemblerException {
        assemblyLines = assembly.split("\n");
        byte[] compiled = assembler.generateByteCode(assemblyLines[linen], linen);
        if(compiled.length == 0) {
            return assembly;
        }
        if(compiled.length == 1 && linen + 1 < assemblyLines.length) {
            byte[] addition = assembler.generateByteCode(assemblyLines[linen+1], linen+1);
            if(addition.length == 1) {
                compiled = new byte[]{ compiled[0], addition[0] };
                for(int i=linen+1; i + 1 < assemblyLines.length; ++i) {
                    assemblyLines[i] = assemblyLines[i+1];
                }
                assemblyLines[assemblyLines.length-1] = "";
            } else if(addition.length == 2){
                compiled = new byte[]{ compiled[0], addition[0], addition[1] };
            } else {
                return assembly;
            }
        }
        int i = linen;
        for (String line : disassembler.disassemble(compiled).split("\n")) {
            if(line.length() == 0) {
                continue;
            }
            assemblyLines[i++] = line;
        }
        return assembler.fixOffsets(String.join("\n", assemblyLines));

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
    public void executeOpcode() {

    }
}
