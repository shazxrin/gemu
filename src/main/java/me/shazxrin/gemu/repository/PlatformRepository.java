package me.shazxrin.gemu.repository;

import me.shazxrin.gemu.entity.Platform;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformRepository extends CrudRepository<Platform, String> {
    Platform findFirstByName(String name);
}
