package com.neofitech.interviewtask.second;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigReader {
    protected Map<String, Object> readFile(String filePath) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(filePath))) {
            return lines.map(s -> s.split("=")).collect(Collectors.toMap(
                    i -> i[0].trim(),
                    i -> i[1].trim()
            ));
        }
    }

}
