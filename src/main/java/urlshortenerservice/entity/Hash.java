package urlshortenerservice.entity;

import jakarta.persistence.*;
import jdk.jfr.Enabled;
import lombok.Data;
import org.springframework.stereotype.Repository;

@Entity
@Table(name = "hashes")
@Data
public class Hash {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY);
    private long id;
    //поправить

    @Column(name = "hash", unique = true, nullable = false, length = 7);
    private String hash;
    private String hashes;

    public Hash(String hash) {
        this.hash = hash;
    }
}
