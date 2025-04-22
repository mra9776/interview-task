package com.neofitech.interviewtask.third;

import com.neofitech.interviewtask.second.ConfigReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

// TODO: this service is really a repository not a service.
// TODO: refactor and extract interface, implementing different solutions separately.
// as discussed in `readme.md`, this is first and naive solution
@Service
public class ThirdService implements AutoCloseable {

    private final ConfigReader configReader;
    private Path filePath;

    public ThirdService(@Autowired ConfigReader configReader, @Value("${second.filepath}") String filePath) {
        this.configReader = configReader;
        this.filePath = Paths.get(filePath);
    }

    // super duper naive solution
    // TODO: avoid reading whole file if key is new. it's going to appended anyway.
    // TODO: might better idea to use NIO mapping stuff, but: https://bugs.java.com/bugdatabase/view_bug?bug_id=4715154
    void writeValueNaive(String key, String value) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "rw");
             FileChannel channel = file.getChannel();
             FileLock lock = channel.lock()) {
            ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
            channel.read(buffer);
            Map<String, Object> stringObjectMap = configReader.parseData(buffer.toString());
            stringObjectMap.put(key, value);

            channel.position(0);
            int newSize = channel.write(ByteBuffer.wrap(configReader.parseAsString(stringObjectMap).getBytes()));
            channel.truncate(newSize);
        }
    }

    public ResponseEntity<?> setValue(String key, String value) {
        try {
            writeValueNaive(key, value);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().build();
    }

    @Override
    public void close() throws Exception {
        // TODO: should I make file and channel a field in this class?
    }
}
