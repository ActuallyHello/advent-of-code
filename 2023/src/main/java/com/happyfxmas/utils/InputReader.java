package com.happyfxmas.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class InputReader {
    private static final String RESOURCE_FOLDER = "src/main/resources/";
    public static List<String> readInput(String fileName) {
        try {
            return Files.readAllLines(Path.of(RESOURCE_FOLDER + fileName));
        } catch (IOException e) {
            System.out.printf("ERROR WHEN READING FILE WITH NAME=%s%n", fileName);
            throw new RuntimeException(e);
        }
    }
}
