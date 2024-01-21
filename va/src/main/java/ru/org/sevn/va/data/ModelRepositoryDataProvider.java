package ru.org.sevn.va.data;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.mongodb.core.query.*;

public class ModelRepositoryDataProvider<T, MODEL, REPO extends PagingAndSortingRepository<T, ?> & CrudRepository<T, ?>, FILTER extends RepositoryFilter<T, REPO>>
        extends AbstractBackEndDataProvider<MODEL, FILTER> {

    private final REPO repository;
    private final MongoTemplate mongoTemplate;
    private final ModelConverter<T, MODEL> mapper;
    private QuerySortOrder [] orders;
    private final String repositoryName;
    private final Class<T> repositoryType;

    public ModelRepositoryDataProvider (
            String repositoryName,
            Class<T> repositoryType,
            MongoTemplate mongoTemplate,
            REPO repo,
            ModelConverter<T, MODEL> mapper,
            QuerySortOrder... orders) {
        this.repositoryName = repositoryName;
        this.repositoryType = repositoryType;
        this.mongoTemplate = mongoTemplate;
        this.repository = repo;
        this.mapper = mapper;
        this.orders = orders;
    }

    public PagingAndSortingRepository<T, ?> getRepository () {
        return repository;
    }

    @Override
    protected Stream<MODEL> fetchFromBackEnd (Query<MODEL, FILTER> query) {
        var pr = PageRequestBuilder.getPageRequest (query, this.orders);
        if (query.getFilter ().isEmpty ()) {
            var page = repository.findAll (pr);
            return mapper.toModels (page.stream ());
        }
        else {
            return findFiltered (query.getFilter ().get (), pr);
        }
    }

    @Override
    protected int sizeInBackEnd (Query<MODEL, FILTER> query) {
        if (query.getFilter ().isEmpty ()) {
            return (int) ((CrudRepository<T, ?>) repository).count ();
        }
        else {
            return countFiltered (query.getFilter ().get ());
        }
    }

    private int countFiltered (FILTER filter) {
        if (StringUtils.isBlank (filter.getFts ())) {
            return filter.count (repository);
        }
        else {
            return (int) ftsCount (filter.getFts ());
        }
    }

    private Stream<MODEL> findFiltered (FILTER filter, PageRequest pr) {
        if (StringUtils.isBlank (filter.getFts ())) {
            return mapper.toModels (filter.findFiltered (repository, pr).stream ());
        }
        else {
            List<T> res = ftsResult (filter.getFts (), pr.getOffset (), pr.getPageSize ());
            return mapper.toModels (res.stream ());
        }
    }

    List<T> ftsResult (String fts, long skip, int limit) {
        var textQuery = TextQuery.queryText (new TextCriteria ()
                .caseSensitive (false)
                .diacriticSensitive (false)
                .matchingAny (fts))
                .sortByScore ()
                .limit (limit).skip (skip);

        var result = mongoTemplate.find (textQuery, repositoryType, repositoryName);
        return result;
    }

    long ftsCount (String fts) {
        var textQuery = TextQuery.queryText (
                new TextCriteria ()
                        .caseSensitive (false)
                        .diacriticSensitive (false)
                        .matchingAny (fts));
        var result = mongoTemplate.count (textQuery, repositoryType, repositoryName);
        return result;
    }
}