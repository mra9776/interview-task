package com.neofitech.interviewtask.second;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigReader {
    public Map<String, Object> readFile(String filePath) throws IOException {
        var data = Files.readString(Path.of(filePath));
        return parseData(data);
    }

    public Map<String, Object> parseData(String data) {
        return Arrays.stream(data.split("\n")).map(s -> s.split("=")).collect(Collectors.toMap(
                i -> i[0].trim(),
                i -> i[1].trim()
        ));
    }

    public String parseAsString(Map<String, Object> data) {
        return data.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("\n"));
    }
}
