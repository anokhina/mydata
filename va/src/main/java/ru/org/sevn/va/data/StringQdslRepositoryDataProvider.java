package ru.org.sevn.va.data;

import com.querydsl.core.types.Predicate;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.springframework.data.domain.Page;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public class StringQdslRepositoryDataProvider<T> extends AbstractBackEndDataProvider<T, String> {
    private final Supplier<? extends QuerydslPredicateExecutor<T>> qdsl;
    private final Function<Optional<String>, Predicate> filterConverter;
    private QuerySortOrder [] orders;

    public StringQdslRepositoryDataProvider (
            final Supplier<? extends QuerydslPredicateExecutor<T>> qdsl,
            Function<Optional<String>, Predicate> filterConverter,
            QuerySortOrder... orders) {
        this.qdsl = qdsl;
        this.filterConverter = filterConverter;
        this.orders = orders;
    }

    protected Predicate getQdslPredicate (Optional<String> str) {
        return filterConverter.apply (str);
    }

    @Override
    protected Stream<T> fetchFromBackEnd (Query<T, String> query) {
        var pr = getQdslPredicate (query.getFilter ());
        if (pr == null) {
            return Stream.empty ();
        }
        Page<T> page;
        var pageable = PageRequestBuilder.getPageRequest (query, orders);
        page = qdsl.get ().findAll (pr, pageable);
        return page.stream ();
    }

    @Override
    protected int sizeInBackEnd (Query<T, String> query) {
        var pr = getQdslPredicate (query.getFilter ());
        if (pr == null) {
            return 0;
        }
        return (int) qdsl.get ().count (pr);
    }

}
