package ru.org.sevn.mydata.util;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import ru.org.sevn.mddata.FileIndexer;
import ru.org.sevn.mddata.ItemInfo;
import ru.org.sevn.mongo.IndexLanguageEnum;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.mydata.entity.StringLongObject;
import ru.org.sevn.mydata.views.books.BookBookModelBuilder;

@Log
public class BookEntityComponent {

    public static ItemInfo buildItemInfo (String basePath, BookEntity be) {
        var ii = new ItemInfo ();

        ii
                .author (be.author ().stream ().collect (Collectors.joining (", ")))
                .description (be.dsc ())
                .lang (be.lang ())
                .img (be.img ())
                .title (be.title ())
                .url (be.url ());

        for (var e : be.content ().split ("\n")) {
            ii.content ().add (e);
        }

        be.tags ().forEach (e -> {
            ii.tags ().add (e.value ());
        });

        ii
                .pathId (be.pathId ());
        if (be.pathId () != null) {
            ii
                    .path (Path.of (basePath, be.pathId (), "index.md").toString ());
        }

        ii
                .indexed (true)
                .changed (false);

        if (be.getUpdateTime () != null) {
            ii.date (new Date (be.getUpdateTime ()));
        }

        be.contentSize ().forEach (e -> {
            ii.contentSize ().put (e.getKey (), e.getValue ());
        });

        return ii;
    }

    public static BookEntity build (BookEntity b, ItemInfo ii, TagEntityComponent tagEntityComponent) {

        b
                .pathId (ii.pathId ())
                .author (BookBookModelBuilder.authors (ii.author ()))
                .content (ii.content ().stream ().collect (Collectors.joining ("\n")))
                .dsc (ii.description ())
                .lang (Optional.ofNullable (IndexLanguageEnum.fromString (ii.lang ())).map (op -> op.name ()).orElse (null))
                .img (ii.img ())
                .title (ii.title ())
                .url (ii.url ());

        b.tags ().clear ();
        if (tagEntityComponent != null) {
            b.tags (new ArrayList (tagEntityComponent.getTags (ii.tags ())));
        }

        b.contentSize ().clear ();
        ii.contentSize ().forEach ( (k, v) -> {
            b.contentSize ().add (StringLongObject.builder ().key (k).value (v).build ());
        });

        return b;
    }

    public static ItemInfo parse (final Path filePath, String relPathParent) {
        try {
            var ii = FileIndexer.getItemInfo (filePath);
            ii.setPathId (relPathParent);

            return ii;
        }
        catch (Exception e) {
            log.log (Level.SEVERE, "Fail to parse: " + filePath.toString (), e);
        }

        return null;
    }

}
