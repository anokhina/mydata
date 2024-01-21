package ru.org.sevn.va.data;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.domain.PageRequest;

public interface RepositoryFilter<T, REPO extends PagingAndSortingRepository<T, ?> & CrudRepository<T, ?>> {
    Page<T> findFiltered (REPO repo, PageRequest pr);

    int count (REPO repo);

    void setFts (String fts);

    String getFts ();
}
