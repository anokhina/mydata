package ru.org.sevn.va.grid;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.dsl.StringPath;
import com.vaadin.flow.component.textfield.TextField;
import java.util.function.BiConsumer;
import org.apache.commons.lang3.StringUtils;

public class TextFieldFilter extends TextField {
    private final String name;
    private boolean trimInput = true;

    public TextFieldFilter (Path path, BiConsumer<String, String> valueConsumer) {
        this (path, path.getMetadata ().getName (), valueConsumer);
    }

    public TextFieldFilter (PathMetadata path, BiConsumer<String, String> valueConsumer) {
        this (path.getName(), path.getName (), valueConsumer);
    }
    
    public TextFieldFilter (PathMetadata path, String label, BiConsumer<String, String> valueConsumer) {
        this (path.getName(), label, valueConsumer);
    }
    
    public TextFieldFilter (Path path, String label, BiConsumer<String, String> valueConsumer) {
        this(path.getMetadata(), label, valueConsumer);
    }
    
    public TextFieldFilter (String name, String label, BiConsumer<String, String> valueConsumer) {
        this.name = name;
        var tf = this;
        tf.setLabel (label);
        tf.setWidthFull ();
        tf.setClearButtonVisible (true);
        tf.addValueChangeListener (evt -> {
            var str = evt.getValue ();
            if (StringUtils.isBlank (str)) {
                valueConsumer.accept (name, null);
            }
            else {
                if (trimInput) {
                    valueConsumer.accept (name, str.trim ());
                }
                else {
                    valueConsumer.accept (name, str);
                }
            }
        });
    }

    public void setTrimInput (boolean trimInput) {
        this.trimInput = trimInput;
    }

    public String getName () {
        return name;
    }

}
