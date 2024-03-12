package ru.org.sevn.mydata.repo;

import java.util.Optional;
import ru.org.sevn.mongo.repo.QuerydslMongoRepository;
import ru.org.sevn.mydata.entity.SettingsEntity;

public interface SettingsEntityRepository extends QuerydslMongoRepository<SettingsEntity, String> {
    public Optional<SettingsEntity> findByName (String name);
}
