package ru.org.sevn.mongo.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import com.querydsl.core.types.OrderSpecifier;

@NoRepositoryBean
public interface QuerydslMongoRepository<E, ID> extends MongoRepository<E, ID>, QuerydslPredicateExecutor<E> {

}
