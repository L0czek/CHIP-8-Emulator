import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Optional;

public class EmulatorModel implements ModelInterface {
    private Optional<String> assembly = null;
    private Optional<byte[]> byteCode = null;

    private Disassembler dis;

    public EmulatorModel() {
        assembly = Optional.empty();
        byteCode = Optional.empty();
        try {
            dis = new Disassembler(InstructionFactory.factoriesByIndex());
        } catch (Exception e) {

        }
    }

    @Override
    public void loadByteCodeFromFile(String path) {
        try {
            var bytes = Files.readAllBytes(Paths.get(path));
            assembly = Optional.of(dis.tryDisassemble(bytes));
            byteCode = Optional.of(bytes);
        }catch(IOException e) {
            byteCode = Optional.empty();
        }
    }

    @Override
    public void loadAssemblyFromFile(String path) {
        try {
            assembly =  Optional.of(Files.readString(Paths.get(path)));
        }catch(IOException e) {
            assembly = Optional.empty();
        }
    }

    @Override
    public Optional<String> getAssembly() {
        return assembly;
    }

    @Override
    public void saveByteCodeToFile(String path) {

    }


    @Override
    public void saveAssemblyToFile(String path, String assembly) {
        try {
            Files.write(Paths.get(path), assembly.getBytes());
        } catch (Exception e) {

        }
    }
}
