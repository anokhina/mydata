package ru.org.sevn.va.renderer.lit;

import java.util.function.Function;

public class TitleTagAttribute<OBJ> extends AnyTagAttribute<OBJ> {

    public TitleTagAttribute (String s) {
        this (o -> s);
    }

    public TitleTagAttribute (Function<OBJ, String> titleProducer) {
        super ("title", /*"title='${item.title}'",*/ e -> titleProducer.apply (e));
    } /*"title='${item.title}'",*/

}
