package com.neofitech.interviewtask.second;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecondService {
    private final ConfigReader configReader;

    @Value("${second.filepath}")
    private String filePath;

    private Map<String, Object> data;
    private LocalDateTime lastDataRead;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();


    /**
     * always called within readLocks
     */
    boolean cacheInvalid() {
        return lastDataRead == null || !lastDataRead.isAfter(LocalDateTime.now().minusMinutes(1));
    }

    /**
     * Almost same as CachedData Sample from {@link java.util.concurrent.locks.ReentrantReadWriteLock}
     * <p>
     * Mind the limit of 65535 read locks.
     * <p>
     * As discussed in "readme.md", we dont serve stale data
     **/
    Object readValue(String key) throws IOException {
        rwl.readLock().lock();
        if (cacheInvalid()) {
            // Must release read lock before acquiring write lock
            rwl.readLock().unlock();
            rwl.writeLock().lock();
            try {
                // Recheck state because another thread might have
                // acquired write lock and changed state before we did.
                if (cacheInvalid()) {
                    data = configReader.readFile(filePath);
                    lastDataRead = LocalDateTime.now();
                }
                // Downgrade by acquiring read lock before releasing write lock
                rwl.readLock().lock();
            } finally {
                rwl.writeLock().unlock(); // Unlock write, still hold read
            }
        }

        try {
            return data.get(key);
        } finally {
            rwl.readLock().unlock();
        }
    }

    public ResponseEntity<SecondDto> getValue(String key) {
        // validation could also be done with javax.validation and spring validation stuff
        // but why introduce extra complexity?
        if (key == null || key.isEmpty()) return ResponseEntity.badRequest().build();

        try {
            Object value = readValue(key);
            // we assume
            return ResponseEntity.ok()
                    .body(new SecondDto(value));
        } catch (IOException e) {
            // Throwing exception and catching exception is not a performant thing to do.
            // gotta consider freq of occurring exceptions, ...
            // due to simple nature of this task we can just return ResponseEntity.
            // throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);

            log.error("Hit exception while getting value {}", key, e);
            return ResponseEntity.internalServerError().build();


        }
    }
}
