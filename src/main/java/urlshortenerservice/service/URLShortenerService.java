package urlshortenerservice.service;

import io.swagger.v3.oas.annotations.servers.Server;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import urlshortenerservice.dto.ShortenUrlResponse;
import urlshortenerservice.entity.ShortenedUrl;
import urlshortenerservice.generator.LocalCache;
import urlshortenerservice.repository.URLRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class URLShortenerService {
    private final LocalCache localCache;
    private final URLRepository urlRepository;

    public ShortenUrlResponse shortenUrl(String originalUrl) {
        String hash = localCache.getHash();
        if (hash == null) {
            throw new IllegalStateException("No available hashes");
        }

        ShortenedUrl entity = new ShortenedUrl();
        entity.setHash(hash);
        entity.setOriginalUrl(originalUrl);
        urlRepository.save(entity);

        return new ShortenUrlResponse("http://localhost:8080/" + hash);
    }

    public String getOriginalUrl(String hash) {
        return urlRepository.findByHash(hash)
                .map(ShortenedUrl::getOriginalUrl)
                .orElseThrow(() -> new EntityNotFoundException("No URL found for hash: " + hash));
    }

}
