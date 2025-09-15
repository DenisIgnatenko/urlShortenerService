package urlshortenerservice.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "hashes")
@Data
@NoArgsConstructor
public class Hash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "hash", unique = true, nullable = false, length = 7)
    private String hash;

    public Hash(String hash) {
        this.hash = hash;
    }
}
