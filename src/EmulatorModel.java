import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

public class EmulatorModel implements ModelInterface {
    private Optional<String> assembly = null;
    private Optional<byte[]> byteCode = null;

    public EmulatorModel() {
        assembly = Optional.empty();
        byteCode = Optional.empty();
    }

    @Override
    public void loadByteCodeFromFile(String path) {
        try {
            byteCode = Optional.of(Files.readAllBytes(Paths.get(path)));
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
}
