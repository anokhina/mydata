package ru.org.sevn.mongo;

//stopwords

import org.apache.commons.lang3.StringUtils;

//https://github.com/mongodb/mongo/tree/master/src/mongo/db/fts

public enum IndexLanguageEnum {
    none ("none"),
    danish ("da"),
    dutch ("nl"),
    english ("en"),
    finnish ("fi"),
    french ("fr"),
    german ("de"),
    hungarian ("hu"),
    italian ("it"),
    norwegian ("nb"),
    portuguese ("pt"),
    romanian ("ro"),
    russian ("ru"),
    spanish ("es"),
    swedish ("sv"),
    turkish ("tr");

    private final String shortName;

    private IndexLanguageEnum (String shortName) {
        this.shortName = shortName;
    }

    public String getShortName () {
        return shortName;
    }

    public static IndexLanguageEnum fromString (String s) {
        if (s != null) {
            for (var e : values ()) {
                if (StringUtils.startsWithIgnoreCase (s, e.name ())) {
                    return e;
                }

                if (StringUtils.equals (s, e.getShortName ())) {
                    return e;
                }
            }
        }
        return null;
    }
}
