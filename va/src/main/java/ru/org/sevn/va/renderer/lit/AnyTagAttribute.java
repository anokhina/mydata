package ru.org.sevn.va.renderer.lit;

import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.function.ValueProvider;
import java.util.function.Function;

public class AnyTagAttribute<OBJ> extends TagAttribute<OBJ> {

    private ValueProvider<OBJ, ?> provider;

    public AnyTagAttribute (String property, Function<OBJ, ?> valueProvider) {
        this (property, e -> valueProvider.apply (e));
    }

    public AnyTagAttribute (String property, ValueProvider<OBJ, ?> provider) {
        super (property);
        this.provider = provider;
    }

    public AnyTagAttribute (String property, String template, Function<OBJ, ?> valueProvider) {
        this (property, template, e -> valueProvider.apply (e));
    }

    public AnyTagAttribute (String property, String template, ValueProvider<OBJ, ?> provider) {
        super (property, template);
        this.provider = provider;
    }

    @Override
    public LitRenderer<OBJ> addValueFuntionality (LitRenderer<OBJ> lr) {
        lr.withProperty (getProperty (), provider);
        return lr;
    }

}
