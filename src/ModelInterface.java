import java.util.Optional;

public interface ModelInterface {
    void loadAssemblyFromFile(String path);
    void loadByteCodeFromFile(String path);
    void saveAssemblyToFile(String path, String assembly);
    void saveByteCodeToFile(String path);

    Optional<String> getAssembly();
}
