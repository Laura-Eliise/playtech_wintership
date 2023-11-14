package textio;

import errors.CustomException;

import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class TextReader {
    /// Reads the content of a text file at [path].
    /// If the file at [path] does not exist or is not a text file then
    /// The program will throw an error.
    public List<String> read(String path) throws CustomException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + path);
        }

        try {
            FileReader file = new FileReader(new File(resource.toURI()));
            BufferedReader reader = new BufferedReader(file);

            List<String> data = reader.lines().collect(Collectors.toList());

            file.close();
            reader.close();
            return data;
        } catch (FileNotFoundException e){
            throw new CustomException(String.format("Couldn't find file at %s", path));
        } catch (Exception e) {
            throw new CustomException(String.format("Unexpected exception at %s\n%s", path, e.getMessage()));
        }
    }
}
