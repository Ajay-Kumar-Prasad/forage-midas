package com.jpmc.midascore;
//loads a text file from your classpath and returns its contents as an array of lines.
import org.springframework.stereotype.Component;
import org.testcontainers.shaded.org.apache.commons.io.IOUtils;

import java.io.InputStream;

@Component
public class FileLoader {
    public String[] loadStrings(String path) {
        try {
            InputStream inputStream = this.getClass().getResourceAsStream(path);
            String fileText = IOUtils.toString(inputStream, "UTF-8");
            return fileText.split(System.lineSeparator());
        } catch (Exception e) {
            return null;
        }
    }
}
