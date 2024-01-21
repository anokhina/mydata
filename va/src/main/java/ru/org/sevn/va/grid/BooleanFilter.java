package ru.org.sevn.va.grid;

import com.querydsl.core.types.dsl.BooleanPath;
import com.vaadin.flow.component.select.Select;
import java.util.function.BiConsumer;

public class BooleanFilter extends Select<Boolean> {
    private final String name;

    public BooleanFilter (BooleanPath path, BiConsumer<String, Boolean> valueConsumer) {
        this (path, path.getMetadata ().getName (), valueConsumer);
    }

    public BooleanFilter (BooleanPath path, String label, BiConsumer<String, Boolean> valueConsumer) {
        name = path.getMetadata ().getName ();
        var tf = this;
        tf.setLabel (label);
        tf.setWidthFull ();
        tf.setEmptySelectionAllowed (true);
        tf.setItems (Boolean.TRUE, Boolean.FALSE);
        tf.addValueChangeListener (evt -> {
            var str = evt.getValue ();
            valueConsumer.accept (name, str);
        });
    }

    public String getName () {
        return name;
    }

}
