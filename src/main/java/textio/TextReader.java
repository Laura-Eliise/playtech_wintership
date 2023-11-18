package textio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The TextReader class provides functionality to read the content of a text file.
 * <p>
 * It uses the specified file path to locate and read the content of the text file.
 * If the file at the specified path does not exist or is not a text file, the program
 * will throw an error with appropriate error messages.
 */
public class TextReader {
    /**
     * Reads the content of a text file at the specified path.
     * <p>
     * If the file at the specified path does not exist or is not a text file, the program
     * will throw an error with appropriate error messages.
     *
     * @param path the path of the text file to be read
     * @return a list of strings representing the lines of the text file
     * @throws IllegalArgumentException if the file is not found, is not a text file,
     *                                  or if there is an unexpected exception during file reading
     */
    public List<String> read(String path) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + path);
        }

        try {
            FileReader file = new FileReader(new File(resource.toURI()));
            BufferedReader reader = new BufferedReader(file);

            List<String> data = reader.lines().collect(Collectors.toList());
            file.close();
            reader.close();

            return data;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException("Couldn't find file at " + path);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected exception at " + path + "\n" + e.getMessage());
        }
    }
}

