package ru.org.sevn.mydata.repo;

import ru.org.sevn.mongo.repo.QuerydslMongoRepository;
import ru.org.sevn.mydata.entity.LinkEntity;

public interface LinkEntityRepository extends QuerydslMongoRepository<LinkEntity, String> {

}
