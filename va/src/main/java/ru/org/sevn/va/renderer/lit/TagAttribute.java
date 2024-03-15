package ru.org.sevn.va.renderer.lit;

import com.vaadin.flow.data.renderer.LitRenderer;
import java.util.UUID;
import java.util.function.Function;
import lombok.Data;

@Data
public abstract class TagAttribute<OBJ> {

    private final String id = UUID.randomUUID ().toString ().replace ("-", "");
    private final String property;
    private String template;

    public TagAttribute (String property) {
        this.property = property;
        this.template = String.format ("%s='${item.%s}'", property, property + id);
    }

    public TagAttribute (String property, String template) {
        this.property = property;
        this.template = template;
    }

    public TagAttribute (String property, Function<TagAttribute<OBJ>, String> templateGenerator) {
        this.property = property;
        this.template = templateGenerator.apply (this);
    }

    public abstract LitRenderer<OBJ> addValueFuntionality (LitRenderer<OBJ> lr);

    public String getPropertyId () {
        return getProperty () + getId ();
    }
}
