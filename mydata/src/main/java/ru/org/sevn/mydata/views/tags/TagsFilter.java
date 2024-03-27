package ru.org.sevn.mydata.views.tags;

import com.querydsl.core.types.dsl.ListPath;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import java.util.Collection;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.entity.TagEntity;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.va.combo.VaEntityMultiselectCombobox;

public class TagsFilter extends VaEntityMultiselectCombobox<TagEntity> {
    private final String name;

    public TagsFilter (Supplier<TagEntityRepository> tagEntityRepository, ListPath<TagEntity, QTagEntity> path, BiConsumer<String, Set<TagEntity>> valueConsumer) {
        this (tagEntityRepository, path, path.getMetadata ().getName (), valueConsumer);
    }

    public TagsFilter (Supplier<TagEntityRepository> tagEntityRepository, ListPath<TagEntity, QTagEntity> path, String label, BiConsumer<String, Set<TagEntity>> valueConsumer) {
        super (
                tagEntityRepository,
                QTagEntity.tagEntity.value,
                new QuerySortOrder ("value", SortDirection.ASCENDING));
        this.name = path.getMetadata ().getName ();

        var tagsCombo = this;
        tagsCombo.setItemLabelGenerator (i -> i.getValue ());
        tagsCombo.setLabel ("Tags:");
        tagsCombo.setClearButtonVisible (true);
        tagsCombo.setWidthFull ();
        addValueChangeListener (evt -> {
            valueConsumer.accept (name, evt.getValue ());
        });
        
        tagsCombo.setLabel(label);
    }

    public String getName () {
        return name;
    }

}
