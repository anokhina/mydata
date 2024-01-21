package ru.org.sevn.mydata.util;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import ru.org.sevn.mddata.ItemInfo;
import ru.org.sevn.mongo.IndexLanguageEnum;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.mydata.views.books.BookBookModelBuilder;

@Component
public class BookEntityComponent {
    public static BookEntity build (ItemInfo ii, TagEntityComponent tagEntityComponent) {
        var b = new BookEntity ();

        b
                .author (BookBookModelBuilder.authors (ii.author ()))
                .content (ii.content ().stream ().collect (Collectors.joining ("\n")))
                .dsc (ii.description ())
                .lang (Optional.ofNullable (IndexLanguageEnum.fromString (ii.lang ())).map (op -> op.name ()).orElse (null))
                .img (ii.img ())
                .title (ii.title ())
                .url (ii.url ());
        if (tagEntityComponent != null) {
            b.tags (new ArrayList (tagEntityComponent.getTags (ii.tags ())));
        }

        return b;
    }
}
