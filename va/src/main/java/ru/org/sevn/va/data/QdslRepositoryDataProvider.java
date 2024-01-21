package ru.org.sevn.va.data;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.org.sevn.entity.Identified;

public class QdslRepositoryDataProvider<T extends Identified> extends ModelQdslRepositoryDataProvider<T, T> {

    public QdslRepositoryDataProvider (QuerydslPredicateExecutor<T> repo) {
        super (repo, new DefaultModelConverter<T> ());
    }
}