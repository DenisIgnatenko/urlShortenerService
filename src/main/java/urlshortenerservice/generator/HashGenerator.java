package urlshortenerservice.generator;

import lombok.extern.slf4j.Slf4j;
import urlshortenerservice.entity.Hash;
import urlshortenerservice.repository.UniqueIDRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class HashGenerator {

    private final UniqueIDRepository hashRepository;
    private static final String BASE_62_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    @Value("${hash.range:1000}")
    private int maxRange;

    @Transactional
    // Тут нужно добавить Scheduler или какой-то другой механизм, чтобы этот метод вызывался периодически
    // в хранилище свободных хэшей.
    public void generateHash() {
        log.info("Generating up to {} new hashes", maxRange);

        List<Long> range = hashRepository.getNextUniqueIDsRange(maxRange);
        log.debug("Received {} unique IDs from sequence", range.size());

        List<Hash> hashes = range.stream()
                .map(this::applyBase62Encoding)
                .map(Hash::new)
                .toList();

        log.debug("Saving {} hashes to database", hashes.size());
        hashRepository.saveAll(hashes);
        hashRepository.flush();

        log.info("Successfully saved {} hashes to database", hashes.size());
//        range.forEach((number -> {
//            String hash = applyBase62Encoding(number);
//            hashRepository.save(new Hash(hash));
//            // Тут надо проверить, как работает save в String Data. Нужно сделать так, чтобы
//            // сохранялся сразу батч из 1000 элементов, а не каждый по отдельности.
//            // Сейчас как будто бы в цикле тысячу раз мы будем вызывать save, что не очень хорошо.
//            // @batchUpdate в jdbcTemplate? Но это не String Data job, а JdbcTemplate. Проверить.
//            // Может быть использовать SessionFactory из Hibernate, чтобы сохранить сразу все элементы в одном запросе?
//            // Или просто собрать коллекцию и вызвать saveAll?
//            // Например, так:
//            //            List<Hash> hashes = range.stream()
//            //                    .map(this::applyBase62Encoding)
//            //                    .map(Hash::new)
//            //                    .toList();
//            //            hashRepository.saveAll(hashes);
//            // Еще нужно распараллелить этот процесс, чтобы он не блокировал основной поток приложения.
//            // Может использовать ParallelStream для этого? Он строит несколько параллельных потоков.
//            // Но это может привести к проблемам с производительностью, если он на самом деле не нужен -
//            // например, когда запрашиваемое количество хэшей меньше, чем размер пула потоков.
//        }));
    }

    // Этот метод должен вызываться из LocalCacheService, когда нужно получить хэши для создания коротких ссылок.
    // Плюс нам важно чтобы LocalCacheService не блокировался при получении хэшей
    // Нам не нужно ждать пока он получит хэши, поэтому нам нужно использовать @Async + completableFuture
    @Transactional
    public List<String> getHashes(long amount) {
        log.info("Requesting {} hashes from database", amount);
        List<Hash> hashes = hashRepository.findAndDelete(amount);
        log.info("Fetched {} hashes from database", hashes.size());

        if (hashes.size() < amount) {
            log.warn("Insufficient hashes in DB. Needed: {}, Found: {}", amount, hashes.size());
            generateHash();
            List<Hash> additional = hashRepository.findAndDelete(amount - hashes.size());
            log.info("Fetched additional {} hashes after generation", additional.size());
            hashes.addAll(additional);
        }
        return hashes.stream().map(Hash::getHash).toList();
    }

    @Async("hashGeneratorExecutor")
    public CompletableFuture<List<String>> getHashesAsync(long amount) {
        return CompletableFuture.completedFuture(getHashes(amount));

    }

    private String applyBase62Encoding(Long number) {
        StringBuilder builder = new StringBuilder();

        while (number > 0) {
            builder.append(BASE_62_CHARACTERS.charAt((int) (number % BASE_62_CHARACTERS.length())));
            number /= BASE_62_CHARACTERS.length();
        }

        // Тут нужно взять первое число, которое будет делиться на 62 с остатком 7 раз.
        // Чтобы сразу получать 7 символов. Хотя и такой вид ссылки https://faandg.school/a тоже будет работать

        return builder.toString();
    }
}
