package ru.org.sevn.mydata.views.books;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import ru.org.sevn.mongo.IndexLanguageEnum;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.va.data.ModelConverter;

public class BookBookModelBuilder implements ModelConverter<BookEntity, BookModel> {

    public static BookEntity getEntity (BookModel bm) {
        BookEntity b = bm.getEntity ();
        if (b == null) {
            b = new BookEntity ();
        }

        b
                .author (authors (bm.author ()))
                .content (bm.content ())
                .dsc (bm.dsc ())
                .lang (bm.lang () == null ? null : bm.lang ().name ())
                .img (bm.img ())
                .title (bm.title ())
                .tags (new ArrayList (bm.tags ()))
                .url (bm.url ());
        return b;
    }

    public static BookModel getModel (BookEntity bm) {
        var b = new BookModel ();

        b.entity (bm);

        b
                .author (author (bm.author ()))
                .content (bm.content ())
                .dsc (bm.dsc ())
                .lang (lang (bm.lang ()))
                .img (bm.img ())
                .title (bm.title ())
                .tags (new HashSet (bm.tags ()))
                .url (bm.url ());
        return b;
    }

    public static List<String> authors (String author) {
        if (author == null) {
            return new ArrayList ();
        }
        return Stream.of (author.split (",")).map (e -> e.trim ()).collect (Collectors.toList ());
    }

    private static List<String> tags (String tags) {
        if (tags == null) {
            return new ArrayList ();
        }
        return Stream.of (tags.split (",")).map (e -> e.trim ()).collect (Collectors.toList ());
    }

    private static String author (List<String> author) {
        if (author == null) {
            return "";
        }
        return author.stream ().collect (Collectors.joining (", "));
    }

    private static IndexLanguageEnum lang (String lang) {
        if (StringUtils.isBlank (lang)) {
            return null;
        }
        else {
            return IndexLanguageEnum.valueOf (lang.toLowerCase ());
        }
    }

    @Override
    public BookEntity fromModel (BookModel m) {
        return getEntity (m);
    }

    @Override
    public BookModel toModel (BookEntity t) {
        return getModel (t);
    }
}
