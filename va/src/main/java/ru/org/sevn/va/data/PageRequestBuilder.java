package ru.org.sevn.va.data;

import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestBuilder {

    public static <E> PageRequest getPageRequest (Query<E, ?> query, QuerySortOrder... sortOrders) {
        if (sortOrders != null && sortOrders.length > 0) {
            return PageRequest.of (query.getPage (), query.getPageSize (), getOrders (sortOrders));
        }

        final List<QuerySortOrder> ordersLst = query.getSortOrders ();
        if (ordersLst != null) {
            return PageRequest.of (query.getPage (), query.getPageSize (), getOrders (ordersLst));
        }
        else {
            return PageRequest.of (query.getPage (), query.getPageSize ());
        }
    }

    public static Sort getOrders (QuerySortOrder... arr) {
        var lst = new ArrayList<Sort.Order> ();
        for (var el : arr) {
            var o = new Sort.Order (getDirection (el.getDirection ()), el.getSorted ());
            //not supported - got "Given sort contained an Order for name with ignore case! MongoDB does not support sorting ignoring case currently!"
            //lst.add(o.ignoreCase());
            lst.add (o);
        }
        return Sort.by (lst);
    }

    public static Sort getOrders (Iterable<QuerySortOrder> arr) {
        var lst = new ArrayList<Sort.Order> ();
        for (var el : arr) {
            var o = new Sort.Order (getDirection (el.getDirection ()), el.getSorted ());
            //not supported - got "Given sort contained an Order for name with ignore case! MongoDB does not support sorting ignoring case currently!"
            //lst.add(o.ignoreCase());
            lst.add (o);
        }
        return Sort.by (lst);
    }

    public static Sort.Direction getDirection (final SortDirection d) {

        switch (d) {
            case ASCENDING :
                return Sort.Direction.ASC;
            case DESCENDING :
                return Sort.Direction.DESC;
        }
        return null;
    }

}
