package ru.org.sevn.mydata.views.tags;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.entity.TagEntity;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.va.combo.VaEntityCombobox;
import ru.org.sevn.va.combo.VaEntityMultiselectCombobox;

@PageTitle ("Tags")
@Route (value = "tags")
public class TagsView extends VerticalLayout {

    @Autowired
    public TagsView (TagEntityRepository tagEntityRepository) {
        setSizeFull ();
        {
            var tagsCombo = new VaEntityCombobox<TagEntity> (
                    () -> tagEntityRepository,
                    QTagEntity.tagEntity.value,
                    new QuerySortOrder ("value", SortDirection.ASCENDING));
            tagsCombo.setItemLabelGenerator (i -> i.getValue ());
            tagsCombo.setNewObjectBuilder (v -> new TagEntity ().value (v), () -> tagEntityRepository);
            tagsCombo.setLabel ("Tags:");
            tagsCombo.setClearButtonVisible (true);
            add (tagsCombo);
        }
        {
            var tagsCombo = new VaEntityMultiselectCombobox<TagEntity> (
                    () -> tagEntityRepository,
                    QTagEntity.tagEntity.value,
                    new QuerySortOrder ("value", SortDirection.ASCENDING));
            tagsCombo.setItemLabelGenerator (i -> i.getValue ());
            tagsCombo.setNewObjectBuilder (v -> new TagEntity ().value (v), () -> tagEntityRepository);
            tagsCombo.setLabel ("Tags:");
            tagsCombo.setClearButtonVisible (true);
            tagsCombo.setWidthFull ();
            add (tagsCombo);
        }

    }
}
