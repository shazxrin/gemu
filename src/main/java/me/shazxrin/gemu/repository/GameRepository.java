package me.shazxrin.gemu.repository;

import me.shazxrin.gemu.entity.Game;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends CrudRepository<Game, String> {
    Game findByExternalId(long externalId);
}
