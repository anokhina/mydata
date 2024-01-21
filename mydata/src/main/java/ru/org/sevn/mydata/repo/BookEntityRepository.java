package ru.org.sevn.mydata.repo;

import ru.org.sevn.mongo.repo.QuerydslMongoRepository;
import ru.org.sevn.mydata.entity.BookEntity;

public interface BookEntityRepository extends QuerydslMongoRepository<BookEntity, String> {

}
