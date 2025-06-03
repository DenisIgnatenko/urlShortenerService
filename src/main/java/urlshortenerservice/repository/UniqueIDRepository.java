package urlshortenerservice.repository;


import urlshortenerservice.entity.Hash;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UniqueIDRepository extends CrudRepository<Hash, Long> {

    @Query(nativeQuery = true, value = """
            SELECT nextval('unique_hash_number_seq') FROM generate_series(1, :maxRange)
            """)
    List<Long> getNextUniqueIDsRange(int maxRange);

    // Тут в запросе нам важно не только получить хэши, но и удалить их из базы данных,
    // чтобы они не использовались повторно. Поэтому используем RETURNING, чтобы вернуть удаленные хэши.
    // Ведь запрос могут совершать несколько потоков одновременно, и если мы не удалим хэши,
    // то другой поток может получить те же хэши, что и первый, и это приведет к конфликтам.
    @Query(nativeQuery = true, value = """
                    DELETE FROM hash WHERE id IN (
                        SELECT id FROM hash ORDER BY id ASC LIMIT :amount
                    ) RETURNING  *
            """)
    List<Hash> findAndDelete(long amount);
}
