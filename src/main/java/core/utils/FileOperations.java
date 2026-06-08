package core.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** Utility class for file operations related to reading Scheme source code. */
public class FileOperations {

    /**
     * Reads Scheme code from the specified file path.
     *
     * @param filePath path to the Scheme source file (absolute or relative)
     * @return the file content as a String
     * @throws IOException if the file cannot be read or does not exist
     */
    public static String readSchemeCodeFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        // Read all bytes and decode as UTF-8
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
