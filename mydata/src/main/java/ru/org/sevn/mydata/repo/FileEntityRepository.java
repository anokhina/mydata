package ru.org.sevn.mydata.repo;

import java.util.Optional;
import ru.org.sevn.mongo.repo.QuerydslMongoRepository;
import ru.org.sevn.mydata.entity.FileEntity;

public interface FileEntityRepository extends QuerydslMongoRepository<FileEntity, String> {
    Optional<FileEntity> findByPath (String path);
}
