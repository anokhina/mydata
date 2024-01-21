package ru.org.sevn.mydata.repo;

import ru.org.sevn.mongo.repo.QuerydslMongoRepository;
import ru.org.sevn.mydata.entity.TagEntity;

public interface TagEntityRepository extends QuerydslMongoRepository<TagEntity, String> {

}
