package urlshortenerservice.generator;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
@Slf4j
public class LocalCache {

    private final HashGenerator hashGenerator;

    @Value("${hash.cache.capacity:1000}")
    private int capacity = 1;

    @Value("${hash.cache.fill.percent:20}")
    private int fillPercent = 20;

    private final AtomicBoolean filling = new AtomicBoolean(false);

    private final Queue<String> hashes = new ArrayBlockingQueue<>(capacity);

    // Тут надо подумать. Метод init вызывается при создании бина, но getHashes работает асинхронно.
    // Этот метод выполнится очень быстро, а getHashes может занять много времени.
    // Мы сделали два метода, один асинхронный, который будет получать хэши из базы,
    // а другой синхронный.
    @PostConstruct
    public void init() {
        log.info("Initializing LocalCache with capacity: {}", capacity);
        List<String> initialHashes = hashGenerator.getHashes(capacity);
        log.info("Fetched {} hashes from HashGenerator during initialization", initialHashes.size());
        hashes.addAll(initialHashes);
    }

    public String getHash() {
        log.debug("Current cache size: {}", hashes.size());
        if (hashes.size() / (capacity / 100.0) < fillPercent) {
            log.info("Hash cache below fill percent ({}%), triggering async refill", fillPercent);
            if (!filling.compareAndSet(false, true)) {
                hashGenerator.getHashesAsync(capacity)
                        .thenAccept(fetched -> {
                            log.info("Fetched {} hashes asynchronously", fetched.size());
                            hashes.addAll(fetched);
                            filling.set(false);
                        });
            }
        }
        return hashes.poll();
    }
}
