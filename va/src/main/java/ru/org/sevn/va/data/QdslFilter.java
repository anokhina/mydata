package ru.org.sevn.va.data;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public class QdslFilter<T, REPO extends QuerydslPredicateExecutor<T> & PagingAndSortingRepository<T, ?> & CrudRepository<T, ?>> implements RepositoryFilter<T, REPO> {
    private Map<String, BooleanExpression> map = new LinkedHashMap ();
    private String fts;

    public void setPropertyFilter (String name, BooleanExpression expr) {
        map.put (name, expr);
    }

    public void setPropertyFilterOr (String name, List<BooleanExpression> lst) {
        var res = lst.get (0);
        for (int i = 1; i < lst.size (); i++) {
            res = res.or (lst.get (i));
        }
        setPropertyFilter (name, res);
    }

    public void setPropertyFilterAnd (String name, List<BooleanExpression> lst) {
        var res = lst.get (0);
        for (int i = 1; i < lst.size (); i++) {
            res = res.and (lst.get (i));
        }
        setPropertyFilter (name, res);
    }

    public BooleanExpression setPropertyFilter (String name) {
        return map.get (name);
    }

    public Predicate getPredicate () {
        return makeAndBooleanExpression (map.values ().stream ().filter (e -> e != null).toList (), () -> null);
    }

    public static BooleanExpression makeAndBooleanExpression (
            List<BooleanExpression> booleanExpressions,
            Supplier<BooleanExpression> defaultBooleanExpression) {
        if (booleanExpressions.isEmpty ()) {
            return defaultBooleanExpression.get ();
        }
        else {
            BooleanExpression qdslPredicate = (BooleanExpression) booleanExpressions.get (0);

            for (var i = 1; i < booleanExpressions.size (); i++) {
                BooleanExpression p = (BooleanExpression) booleanExpressions.get (i);
                qdslPredicate = qdslPredicate.and (p);
            }
            return qdslPredicate;
        }
    }

    @Override
    public Page<T> findFiltered (REPO repo, PageRequest p) {
        var pr = getPredicate ();
        if (pr == null) {
            return repo.findAll (p);
        }
        else {
            return repo.findAll (pr, p);
        }
    }

    @Override
    public int count (REPO repo) {
        var pr = getPredicate ();
        if (pr == null) {
            return (int) repo.count ();
        }
        else {
            return (int) repo.count (pr);
        }
    }

    @Override
    public void setFts (String fts) {
        this.fts = fts;
    }

    @Override
    public String getFts () {
        return fts;
    }
}
