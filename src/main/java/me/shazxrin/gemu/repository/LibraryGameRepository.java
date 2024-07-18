package me.shazxrin.gemu.repository;

import me.shazxrin.gemu.model.LibraryGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LibraryGameRepository extends CrudRepository<LibraryGame, String> {
    boolean existsByGameId(String gameId);

    Optional<LibraryGame> findByGameId(String gameId);

    Page<LibraryGame> findAllByIdNotNull(Pageable pageable);

    Page<LibraryGame> findAllByPlatformId(String platformId, Pageable pageable);

    void deleteByGameId(String gameId);
}
