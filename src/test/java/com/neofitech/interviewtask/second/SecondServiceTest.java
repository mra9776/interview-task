package com.neofitech.interviewtask.second;

import lombok.val;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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


    @Nested
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Execution(ExecutionMode.CONCURRENT)
    class ServiceTest {
        private SecondService service;

        @BeforeAll
        public void setup() throws IOException {
            val sampleMap = new HashMap<String, Object>() {{
                put("k1", "v1");
                put("k2", "v2");
            }};
            val configReader = mock(ConfigReader.class);
            when(configReader.readFile(any())).thenReturn(sampleMap);
            this.service = spy(new SecondService(configReader));
            doAnswer(invocation -> Math.random() > 0.5).when(service).cacheInvalid();
        }

        @RepeatedTest(1000)
        void readValue() throws IOException {
            String value = (String) service.readValue("k1");
            assertEquals("v1", value);
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