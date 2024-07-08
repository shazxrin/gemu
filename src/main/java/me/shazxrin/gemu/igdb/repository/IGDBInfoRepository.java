package me.shazxrin.gemu.igdb.repository;

import me.shazxrin.gemu.igdb.model.IGDBInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGDBInfoRepository extends CrudRepository<IGDBInfo, String> {
    IGDBInfo findFirstByIdNotNull();
}
