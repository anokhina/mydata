package ru.org.sevn.mydata.repo;

import java.util.Optional;
import ru.org.sevn.mongo.repo.QuerydslMongoRepository;
import ru.org.sevn.mydata.entity.BookEntity;

public interface BookEntityRepository extends QuerydslMongoRepository<BookEntity, String> {
    public Optional<BookEntity> findByPathId (String pathId);
}
