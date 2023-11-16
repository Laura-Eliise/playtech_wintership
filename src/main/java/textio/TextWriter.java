package textio;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * The `TextWriter` class provides functionality to create a text file and write data to it.
 * <p>
 * It uses the specified file path to create a text file and writes the provided data to that file.
 * If the file cannot be written to the specified path, an error will be thrown with appropriate error messages.
 */
public class TextWriter {
    /**
     * Creates a text file from the provided data and writes it to the specified path.
     * <p>
     * If the file cannot be written to the specified path, an error will be thrown with appropriate error messages.
     *
     * @param path the path where the text file will be created
     * @param data the data to be written to the text file
     */
    public void write(String path, String data) {
        try {
            File file = createFile(path);
            PrintWriter writer = new PrintWriter(file);
            writer.print(data);
            writer.close();
        } catch (IOException e) {
            System.out.printf("Couldn't create file at %s.\n", path);
            throw new IllegalArgumentException(String.format("Couldn't create file at %s.\n", path));
        }
    }

    /**
     * Creates a file at the specified path.
     * <p>
     * If the file cannot be created at the specified path, an error will be thrown with appropriate error messages.
     *
     * @param path the path where the file will be created
     * @return the created File object
     * @throws IOException if there is an issue creating the file
     */
    private File createFile(String path) throws IOException {
        File file = new File(path);
        file.createNewFile();
        return file;
    }
}

