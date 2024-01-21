package ru.org.sevn.mydata.views.links;

import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.icon.VaadinIcon;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import ru.org.sevn.mydata.entity.LinkEntity;
import ru.org.sevn.mydata.entity.QLinkEntity;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.entity.TagEntity;
import ru.org.sevn.mydata.repo.LinkEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.mydata.views.tags.TagsFilter;
import ru.org.sevn.va.USymbol;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.grid.TextFieldFilter;
import ru.org.sevn.va.renderer.lit.ClickEvent;
import ru.org.sevn.va.renderer.lit.OnClick;
import ru.org.sevn.va.renderer.lit.Tag;

public class LinkGrid extends Grid<LinkModel> {
    private final LinkEntityQdslFilter filter = new LinkEntityQdslFilter ();
    private final Grid.Column<LinkModel> control;

    private BiConsumer<LinkModel, ClickEvent> controlClick = (e, evt) -> {};

    public LinkGrid (TagEntityRepository tagEntityRepository) {
        super (LinkModel.class, false);

        this.control = addColumn (new Tag ("div")
                .addTagContent (e -> USymbol.OPEN_FOLDER)
                .addAttribute (new OnClick ( (e, evt) -> {
                    controlClick.accept ((LinkModel) e, (ClickEvent) evt);
                }))
                .getLitRenderer ());
        control
                .setFrozen (true)
                .setResizable (true)
                .setWidth ("20px");

        var title = addColumn (LinkModel::title).setHeader ("title").setResizable (true);
        var url = addColumn (LinkModel::url).setHeader ("url").setResizable (true);
        var tags = addColumn (new Tag<LinkModel> ("div")
                .addTagContent (e -> e.getTags ().stream ().map (t -> t.value ())
                        .collect (Collectors.joining (", ")))
                .getLitRenderer ())
                .setHeader ("tags").setResizable (true);
        var author = addColumn (LinkModel::author).setHeader ("author").setResizable (true);
        var img = addColumn (LinkModel::img).setHeader ("img").setResizable (true);
        var dsc = addColumn (LinkModel::dsc).setHeader ("dsc").setResizable (true);

        getHeaderRows ().clear ();
        var headerRow = appendHeaderRow ();

        {
            var searchBtn = VaadinIcon.SEARCH.create ();
            searchBtn.addClickListener (evt -> {
                getDataProvider ().refreshAll ();
            });
            headerRow.getCell (control).setComponent (searchBtn);
        }
        {
            StringPath path = QLinkEntity.linkEntity.title;
            var tf = new TextFieldFilter (path, null, (n, v) -> {
                filter.setPropertyFilter (n, v == null ? null : path.containsIgnoreCase (v));
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (title).setComponent (tf);
        }
        {
            ListPath<TagEntity, QTagEntity> path = QLinkEntity.linkEntity.tags;
            var tf = new TagsFilter ( () -> tagEntityRepository, path, (n, v) -> {
                if (v == null || v.isEmpty ()) {
                    filter.setPropertyFilter (n, null);
                }
                else {
                    var lst = v.stream ().map (ve -> path.contains (ve)).toList ();
                    filter.setPropertyFilterOr (n, lst);
                }
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (tags).setComponent (tf);
        }
    }

    public void setControlClick (BiConsumer<LinkModel, ClickEvent> controlClick) {
        this.controlClick = controlClick;
    }

    public GridDataView<LinkModel> setItems (ModelRepositoryDataProvider<LinkEntity, LinkModel, LinkEntityRepository, LinkEntityQdslFilter> dp) {
        var fdp = dp.withConfigurableFilter ();
        fdp.setFilter (filter);
        return super.setItems (fdp);
    }

    public LinkEntityQdslFilter getFilter () {
        return filter;
    }

}
