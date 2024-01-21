package ru.org.sevn.mydata.views.links;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import ru.org.sevn.mongo.IndexLanguageEnum;
import ru.org.sevn.mydata.entity.LinkEntity;
import ru.org.sevn.va.data.ModelConverter;

public class LinkLinkModelBuilder implements ModelConverter<LinkEntity, LinkModel> {

    public static LinkEntity getEntity (LinkModel bm) {
        LinkEntity b = bm.getEntity ();
        if (b == null) {
            b = new LinkEntity ();
        }

        b
                .author (authors (bm.author ()))
                .dsc (bm.dsc ())
                .lang (bm.lang () == null ? null : bm.lang ().name ())
                .img (bm.img ())
                .title (bm.title ())
                .tags (new ArrayList (bm.tags ()))
                .url (bm.url ());
        return b;
    }

    public static LinkModel getModel (LinkEntity bm) {
        var b = new LinkModel ();

        b.entity (bm);

        b
                .author (author (bm.author ()))
                .dsc (bm.dsc ())
                .lang (lang (bm.lang ()))
                .img (bm.img ())
                .title (bm.title ())
                .tags (new HashSet (bm.tags ()))
                .url (bm.url ());
        return b;
    }

    private static List<String> authors (String author) {
        if (author == null) {
            return new ArrayList ();
        }
        return Stream.of (author.split (",")).map (e -> e.trim ()).collect (Collectors.toList ());
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
    public LinkEntity fromModel (LinkModel m) {
        return getEntity (m);
    }

    @Override
    public LinkModel toModel (LinkEntity t) {
        return getModel (t);
    }
}
