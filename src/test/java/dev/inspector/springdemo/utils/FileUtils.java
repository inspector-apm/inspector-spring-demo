package dev.inspector.springdemo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtils {
    public static String readResponseFromFile(String fileName) throws IOException, URISyntaxException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        var resource = classLoader.getResource("responses/" + fileName);
        if (resource == null) {
            throw new FileNotFoundException("File is not found in path 'responses/" + fileName + "'");
        }
        return Files.readString(Paths.get(resource.toURI()));
    }

    public static File readResponseToFile(String fileName) throws FileNotFoundException, URISyntaxException {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        var resource = classLoader.getResource("responses/" + fileName);
        if (resource == null) {
            throw new FileNotFoundException("File is not found in path 'responses/" + fileName + "'");
        }
        return Paths.get(resource.toURI()).toFile();
    }
}
