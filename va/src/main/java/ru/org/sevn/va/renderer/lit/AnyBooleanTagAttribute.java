package ru.org.sevn.va.renderer.lit;

import java.util.function.Function;

public class AnyBooleanTagAttribute<OBJ> extends AnyTagAttribute<OBJ> {

    public AnyBooleanTagAttribute (String property, String template, Function<OBJ, Boolean> booleanProducer) {
        super (property, template, el -> {
            java.lang.Boolean b = booleanProducer.apply (el);
            return (b != null) ? b : false;
        });
    }

    public AnyBooleanTagAttribute (String property, Function<TagAttribute<OBJ>, String> templateBuilder, Function<OBJ, Boolean> booleanProducer) {
        super (property, templateBuilder, el -> {
            java.lang.Boolean b = booleanProducer.apply (el);
            return (b != null) ? b : false;
        });
    }

}
