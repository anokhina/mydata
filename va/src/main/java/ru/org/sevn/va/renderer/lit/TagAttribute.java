package ru.org.sevn.va.renderer.lit;

import com.vaadin.flow.data.renderer.LitRenderer;
import lombok.Data;

@Data
public abstract class TagAttribute<OBJ> {

    private String property;
    private String template;

    public TagAttribute (String property) {
        this (property, String.format ("%s='${item.%s}'", property, property));
    }

    public TagAttribute (String property, String template) {
        this.property = property;
        this.template = template;
    }

    public abstract LitRenderer<OBJ> addValueFuntionality (LitRenderer<OBJ> lr);

}
