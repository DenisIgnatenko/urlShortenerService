package urlshortenerservice.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "shortened_urls")
@Data
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 7)
    private String hash;

    @Column(name = "original_url", nullable = false)
    private String originalUrl;
}