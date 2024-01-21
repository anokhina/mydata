package ru.org.sevn.va.combo;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.data.provider.QuerySortOrder;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.CrudRepository;
import ru.org.sevn.va.data.StringQdslRepositoryDataProvider;

public class VaEntityMultiselectCombobox<T> extends MultiSelectComboBox<T> {

    private StringQdslRepositoryDataProvider dataProvider;

    public VaEntityMultiselectCombobox (Supplier<? extends QuerydslPredicateExecutor<T>> repo, StringPath entityQpath, QuerySortOrder... orders) {
        this (repo,
                ostr -> {
                    if (ostr.isPresent () && ! StringUtils.isBlank (ostr.get ())) {
                        return entityQpath.containsIgnoreCase (ostr.get ().trim ());
                    }
                    return entityQpath.isNotNull ();
                }, orders);
    }

    public VaEntityMultiselectCombobox (Supplier<? extends QuerydslPredicateExecutor<T>> repo, Function<Optional<String>, Predicate> filterBuilder, QuerySortOrder... orders) {
        dataProvider = new StringQdslRepositoryDataProvider (repo, filterBuilder, orders);
        setItems (dataProvider);
    }

    public void setNewObjectBuilder (Function<String, T> newObjectBuilder, Supplier<? extends CrudRepository<T, ?>> repo) {
        var cbox = this;
        if (newObjectBuilder != null) {
            cbox.setAllowCustomValue (true);
            cbox.addCustomValueSetListener (e -> {
                String customValue = e.getDetail ();
                var entity = newObjectBuilder.apply (customValue);
                ((CrudRepository) repo.get ()).save (entity);
            });
        }
    }

    public VaEntityMultiselectCombobox<T> allowNull (boolean b) {
        setClearButtonVisible (b);
        return this;
    }

}
