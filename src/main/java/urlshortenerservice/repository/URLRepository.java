package urlshortenerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import urlshortenerservice.entity.ShortenedUrl;

import java.util.Optional;

@Repository
public interface URLRepository extends JpaRepository<ShortenedUrl, Long> {
    Optional<ShortenedUrl> findByHash(String hash);
}
