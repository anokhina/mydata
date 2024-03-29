package ru.org.sevn.va.renderer.lit;

import java.util.function.Function;

public class DisabledTagAttribute<OBJ> extends AnyTagAttribute<OBJ> {

    public DisabledTagAttribute (Function<OBJ, Boolean> booleanProducer) {
        super ("disabled",
                (ta) -> "?disabled=${item." + ta.getPropertyId () + "}", e -> booleanProducer.apply (e));
    }

}
