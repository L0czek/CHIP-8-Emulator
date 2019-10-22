import java.util.Optional;

public interface ModelInterface {
    void loadAssemblyFromFile(String path);
    void loadByteCodeFromFile(String path);

    Optional<String> getAssembly();
}
