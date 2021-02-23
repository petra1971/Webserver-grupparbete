package se.group4.fileutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileReader {
    public static byte[] readFromFile(File file) {
        byte[] content = new byte[0];
        System.out.println("Filereader: Does file exists: " + file.exists());
        if (file.exists() && file.canRead()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                content = new byte[(int) file.length()];
                int count = fileInputStream.read(content);
                System.out.println("count i readFromFile " +count);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }
}
