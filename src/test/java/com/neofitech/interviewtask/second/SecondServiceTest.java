package com.neofitech.interviewtask.second;

import lombok.val;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SecondServiceTest {

    @TempDir
    private Path dirPath;

    @Nested
    class ConfigReaderTest {
        @Test
        void configReader() throws IOException {
            val reader = new ConfigReader();
            Path filePath = writeSampleFile(dirPath);
            Map<String, Object> stringObjectMap = reader.readFile(String.valueOf(filePath));
            assertAll(
                    () -> assertNotNull(stringObjectMap),
                    () -> assertFalse(stringObjectMap.isEmpty()),
                    () -> assertEquals("v1", stringObjectMap.get("k1")),
                    () -> assertEquals("v2", stringObjectMap.get("k2"))
            );
        }

    }


    private Path writeSampleFile(Path filePath) throws IOException {
        String body = """
                k1=v1
                k2=v2
                """;
        Path tempFile = Files.createTempFile(filePath, null, null);
        Files.write(tempFile, body.getBytes());
        return tempFile;
    }
}