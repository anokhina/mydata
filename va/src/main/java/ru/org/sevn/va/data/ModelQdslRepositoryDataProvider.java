package ru.org.sevn.va.data;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import com.querydsl.core.types.Predicate;

public class ModelQdslRepositoryDataProvider<T, MODEL> extends AbstractBackEndDataProvider<MODEL, Void> {

    private final QuerydslPredicateExecutor<T> qdsl;
    private Predicate qdslPredicate;
    private final ModelConverter<T, MODEL> mapper;

    public ModelQdslRepositoryDataProvider (QuerydslPredicateExecutor<T> qdsl, ModelConverter<T, MODEL> mapper) {
        this.mapper = mapper;
        this.qdsl = qdsl;
    }

    @Override
    protected Stream<MODEL> fetchFromBackEnd (Query<MODEL, Void> query) {
        var pr = getQdslPredicate ();
        if (pr == null) {
            return Stream.empty ();
        }
        Page<T> page;
        page = qdsl.findAll (pr, PageRequestBuilder.getPageRequest (query));
        return page.stream ().map (el -> mapper.toModel (el));
    }

    @Override
    protected int sizeInBackEnd (Query<MODEL, Void> query) {
        var pr = getQdslPredicate ();
        if (pr == null) {
            return 0;
        }
        return (int) qdsl.count (pr);
    }

    public Predicate getQdslPredicate () {
        return qdslPredicate;
    }

    public void setQdslPredicate (Predicate qdslPredicate) {
        this.qdslPredicate = qdslPredicate;
    }

}