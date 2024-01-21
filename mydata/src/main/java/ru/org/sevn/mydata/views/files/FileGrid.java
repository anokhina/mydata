package ru.org.sevn.mydata.views.files;

import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridDataView;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParam;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.StringUtils;
import ru.org.sevn.mydata.entity.FileEntity;
import ru.org.sevn.mydata.entity.QFileEntity;
import ru.org.sevn.mydata.repo.FileEntityRepository;
import ru.org.sevn.mydata.repo.TagEntityRepository;
import ru.org.sevn.va.USymbol;
import ru.org.sevn.va.VaLinkUtil;
import ru.org.sevn.va.data.ModelRepositoryDataProvider;
import ru.org.sevn.va.grid.BooleanFilter;
import ru.org.sevn.va.grid.TextFieldFilter;
import ru.org.sevn.va.renderer.lit.ClickEvent;
import ru.org.sevn.va.renderer.lit.OnClick;
import ru.org.sevn.va.renderer.lit.Tag;

public class FileGrid extends Grid<FileEntity> {
    private final FileEntityQdslFilter filter = new FileEntityQdslFilter ();
    private final Grid.Column<FileEntity> control;
    private final Grid.Column<FileEntity> del;

    private BiConsumer<FileEntity, ClickEvent> controlClick = (e, evt) -> {};
    private BiConsumer<FileEntity, ClickEvent> delClick = (e, evt) -> {};

    private Map<String, TextFieldFilter> stringFilters = new HashMap ();

    public FileGrid (TagEntityRepository tagEntityRepository) {
        super (FileEntity.class, false);

        this.control = addColumn (new Tag ("div")
                .addTagContent (e -> USymbol.OPEN_FOLDER)
                .addAttribute (new OnClick ( (e, evt) -> {
                    controlClick.accept ((FileEntity) e, (ClickEvent) evt);
                }))
                .getLitRenderer ());
        control
                .setFrozen (true)
                .setResizable (true)
                .setWidth ("3em")
                .setFlexGrow (0);
        this.del = addColumn (new Tag ("div")
                .addTagContent (e -> "x")
                .addAttribute (new OnClick ( (e, evt) -> {
                    delClick.accept ((FileEntity) e, (ClickEvent) evt);
                }))
                .getLitRenderer ());
        del
                .setFrozen (true)
                .setResizable (true)
                .setWidth ("2em")
                .setFlexGrow (0);

        // ^/media/sevn/Elements/razbor/SEAGATE/store/Java/NetBeansProjects/var/
        var exists = addColumn (e -> {
            var f = new File (e.path ());
            var ex = f.exists ();
            if (! ex) {
                select (e);
            }
            return "" + ex;
        }).setHeader ("exists").setResizable (true);
        exists.setWidth ("5em").setFlexGrow (0);

        var sortedOut = addColumn (FileEntity::sortedOut).setHeader ("sortedOut").setResizable (true).setKey ("sortedOut");
        sortedOut.setWidth ("5em").setFlexGrow (0);
        var pathf = addColumn (FileEntity::path).setHeader ("path").setResizable (true).setSortable (true).setKey ("path");
        pathf.setFlexGrow (3);

        var name = (Grid.Column<FileEntity>) addColumn (Tag.of (this, "div")
                .addTagContent (el -> el.getName ())
                .addAttribute (OnClick.of (this.getBeanType (), (e, evt) -> {
                    //
                    if (evt.isCtrlKey ()) {

                        var rl = new RouterLink (FileView.class);
                        rl.setQueryParameters (new QueryParameters (Map.of (
                                "name", List.of (e.getName ()),
                                "ext", List.of (e.getExt ()))));
                        VaLinkUtil.openLocation (this, true, rl.getHref ());
                    }
                }))
                .getLitRenderer ());
        name
                .setHeader ("name").setResizable (true).setSortable (true).setKey ("name")
                .setFlexGrow (2);

        var ext = addColumn (FileEntity::ext).setHeader ("ext").setResizable (true).setKey ("ext");
        ext.setWidth ("5em").setFlexGrow (0);
        var size = addColumn (FileEntity::size).setHeader ("size").setResizable (true).setSortable (true).setKey ("size");
        size.setWidth ("10em").setFlexGrow (0);
        var dsc = addColumn (FileEntity::dsc).setHeader ("dsc").setResizable (true).setKey ("dsc");
        dsc.setFlexGrow (1);

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
            StringPath path = QFileEntity.fileEntity.path;
            var tf = new TextFieldFilter (path, null, (n, v) -> {
                if (StringUtils.isBlank (v)) {
                    filter.setPropertyFilter (n, null);
                }
                else {
                    if (v.startsWith ("^")) {
                        filter.setPropertyFilter (n, path.startsWith (v.substring (1)));
                    }
                    else if (v.startsWith ("?")) {
                        filter.setPropertyFilter (n, path.containsIgnoreCase (v));
                    }
                    else if (v.startsWith ("$")) {
                        filter.setPropertyFilter (n, path.endsWith (v));
                    }
                    else {
                        filter.setPropertyFilter (n, path.contains (v));
                    }
                }
            });
            tf.addKeyUpListener (evt -> {
                if (evt.getKey () == Key.ENTER) {
                    getDataProvider ().refreshAll ();
                }
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (pathf).setComponent (tf);

            //tf.setValue("/notebook.sav/");
        }
        {
            StringPath path = QFileEntity.fileEntity.name;
            var tf = new TextFieldFilter (path, null, (n, v) -> {
                filter.setPropertyFilter (n, StringUtils.isBlank (v) ? null : path.containsIgnoreCase (v));
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (name).setComponent (tf);
            stringFilters.put ("name", tf);
        }
        {
            StringPath path = QFileEntity.fileEntity.ext;
            var tf = new TextFieldFilter (path, null, (n, v) -> {
                filter.setPropertyFilter (n, StringUtils.isBlank (v) ? null : path.containsIgnoreCase (v));
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (ext).setComponent (tf);
            stringFilters.put ("ext", tf);
        }
        {
            StringPath path = QFileEntity.fileEntity.dsc;
            var tf = new TextFieldFilter (path, null, (n, v) -> {
                filter.setPropertyFilter (n, StringUtils.isBlank (v) ? null : path.containsIgnoreCase (v));
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (dsc).setComponent (tf);
            stringFilters.put ("dsc", tf);
        }

        {
            BooleanPath path = QFileEntity.fileEntity.sortedOut;
            var tf = new BooleanFilter (path, null, (n, v) -> {
                filter.setPropertyFilter (n, v == null ? null : path.eq (v));
            });
            filter.setPropertyFilter (tf.getName (), null);
            headerRow.getCell (sortedOut).setComponent (tf);
            tf.setValue (Boolean.FALSE);
        }
    }

    public void setControlClick (BiConsumer<FileEntity, ClickEvent> controlClick) {
        this.controlClick = controlClick;
    }

    public void setDelClick (BiConsumer<FileEntity, ClickEvent> delClick) {
        this.delClick = delClick;
    }

    public GridDataView<FileEntity> setItems (ModelRepositoryDataProvider<FileEntity, FileEntity, FileEntityRepository, FileEntityQdslFilter> dp) {
        var fdp = dp.withConfigurableFilter ();
        fdp.setFilter (filter);
        return super.setItems (fdp);
    }

    public FileEntityQdslFilter getFilter () {
        return filter;
    }

    public void setFilterValue (String param, String v) {
        setFilterValue (param, v, false);
    }

    public void setFilterValue (String param, String v, boolean refresh) {
        if (stringFilters.containsKey (param)) {
            stringFilters.get (param).setValue (v);
            if (refresh) {
                getDataProvider ().refreshAll ();
            }
        }
    }
}
