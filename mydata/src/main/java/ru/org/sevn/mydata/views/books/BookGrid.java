package ru.org.sevn.mydata.views.books;

import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.icon.VaadinIcon;
import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.mydata.entity.QBookEntity;
import ru.org.sevn.mydata.entity.QTagEntity;
import ru.org.sevn.mydata.entity.TagEntity;
import ru.org.sevn.mydata.repo.BookEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.mydata.views.FileUtil;
import ru.org.sevn.mydata.views.tags.TagsFilter;
import ru.org.sevn.va.USymbol;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.grid.TextFieldFilter;
import ru.org.sevn.va.renderer.lit.ClickEvent;
import ru.org.sevn.va.renderer.lit.OnClick;
import ru.org.sevn.va.renderer.lit.Tag;
import ru.org.sevn.va.renderer.lit.Tags;

public class BookGrid extends Grid<BookModel> {

    private final BookEntityQdslFilter filter = new BookEntityQdslFilter ();
    private final Grid.Column<BookModel> control;

    @Setter
    private BiConsumer<BookModel, ClickEvent> controlClick = (e, evt) -> {};

    @Setter
    private BiConsumer<BookModel, ClickEvent> rebuildClick = (e, evt) -> {};

    @Setter
    private BiConsumer<BookModel, ClickEvent> verifiedClick = (e, evt) -> {};

    @Setter
    @Accessors (fluent = true, chain = true)
    public static class Ctx {
        TagEntityRepository tagEntityRepository;
        Supplier<Path> supplierDataDir;
    }

    public BookGrid (Ctx ctx) {
        super (BookModel.class, false);

        this.control = addColumn (new Tags<> (
                new Tag ("div")
                        .addTagContent (e -> USymbol.OPEN_FOLDER)
                        .addAttribute (new OnClick ( (e, evt) -> {
                            controlClick.accept ((BookModel) e, (ClickEvent) evt);
                        })),
                new Tag ("div")
                        .addTagContent (e -> USymbol.CLOCKWISE_DOWNWARDS_AND_UPWARDS_OPEN_CIRCLE_ARROWS)
                        .addAttribute (new OnClick ( (e, evt) -> {
                            rebuildClick.accept ((BookModel) e, (ClickEvent) evt);
                        })),
                new Tag ("div")
                        .addTagContent (e -> {
                            var bm = (BookModel) e;
                            if (bm.entity () != null &&
                                    bm.entity ().verified () != null &&
                                    bm.entity ().verified ()) {

                                return USymbol.PLUS;
                            }
                            return USymbol.MINUS;
                        })
                        .addAttribute (new OnClick ( (e, evt) -> {
                            verifiedClick.accept ((BookModel) e, (ClickEvent) evt);
                        }))
        //
        )
                .getLitRenderer ());
        control
                .setFrozen (true)
                .setResizable (true)
                .setWidth ("20px");

        var pathId = //addColumn (BookModel::pathId)
                addColumn (Tag.of (this, "div")
                        .addTagContent (el -> el.pathId ())
                        .addAttribute (OnClick.of (this.getBeanType (), (e, evt) -> {
                            //
                            if (evt.isCtrlKey ()) {
                                FileUtil.open ( () -> Path.of (ctx.supplierDataDir.get ().toString (), e.getPathId ()).toString ());
                            }
                        }))
                        .getLitRenderer ())
                        .setHeader ("pathId").setResizable (true);
        var title = addColumn (BookModel::title).setHeader ("title").setResizable (true);
        var url = addColumn (BookModel::url).setHeader ("url").setResizable (true);
        var tags = addColumn (new Tag<BookModel> ("div")
                .addTagContent (e -> e.getTags ().stream ().map (t -> t.value ())
                        .collect (Collectors.joining (", ")))
                .getLitRenderer ())
                .setHeader ("tags").setResizable (true);
        var author = addColumn (BookModel::author).setHeader ("author").setResizable (true);
        var img = addColumn (BookModel::img).setHeader ("img").setResizable (true);
        var dsc = addColumn (BookModel::dsc).setHeader ("dsc").setResizable (true);

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
            StringPath path = QBookEntity.bookEntity.title;
            var tf = new TextFieldFilter (path, null, (n, v) -> {
                filter.setPropertyFilter (n, v == null ? null : path.containsIgnoreCase (v));
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (title).setComponent (tf);
        }
        {
            ListPath<String, StringPath> path = QBookEntity.bookEntity.author;
            var tf = new TextFieldFilter (path.getMetadata(), null, (n, v) -> {
                filter.setPropertyFilter (n, v == null ? null : path.any().containsIgnoreCase (v));
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (author).setComponent (tf);
        }
        {
            ListPath<TagEntity, QTagEntity> path = QBookEntity.bookEntity.tags;
            var tf = new TagsFilter ( () -> ctx.tagEntityRepository, path, null, (n, v) -> {
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

    public GridDataView<BookModel> setItems (ModelRepositoryDataProvider<BookEntity, BookModel, BookEntityRepository, BookEntityQdslFilter> dp) {
        var fdp = dp.withConfigurableFilter ();
        fdp.setFilter (filter);
        return super.setItems (fdp);
    }

    public BookEntityQdslFilter getFilter () {
        return filter;
    }

}
