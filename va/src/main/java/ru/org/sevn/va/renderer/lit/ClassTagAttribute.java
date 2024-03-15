package ru.org.sevn.va.renderer.lit;

import java.util.function.Function;

public class ClassTagAttribute<OBJ> extends AnyTagAttribute<OBJ> {

    public ClassTagAttribute (String s) {
        this (o -> s);
    }

    public ClassTagAttribute (Function<OBJ, String> titleProducer) {
        super ("className",
                (ta) -> "class='${item." + ta.getPropertyId () + "}'", e -> titleProducer.apply (e));
    }

}
