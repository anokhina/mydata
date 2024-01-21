package ru.org.sevn.va.renderer.lit;

import java.util.function.Function;

public class ReadonlyTagAttribute<OBJ> extends AnyTagAttribute<OBJ> {

    public ReadonlyTagAttribute (Function<OBJ, Boolean> titleProducer) {
        super ("readOnly", "?readOnly=${item.readOnly}", e -> titleProducer.apply (e));
    }

}
