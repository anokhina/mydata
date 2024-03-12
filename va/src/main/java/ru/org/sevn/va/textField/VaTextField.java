package ru.org.sevn.va.textField;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.org.sevn.obj.ConfigurableObject;

public class VaTextField extends TextField implements ConfigurableObject<VaTextField> {

    @Getter
    private HorizontalLayout controlButtons = new HorizontalLayout ();

    @Getter
    @Setter
    @Accessors (fluent = true, chain = true)
    public static class Cfg {
        String label;
        String initialValue;
        String placeholder;
        ValueChangeListener<? super ComponentValueChangeEvent<TextField, String>> valueChangeListener;
    }

    public VaTextField (Cfg cfg) {
        super ();
        setWidthFull ();
        setLabel (cfg.label ());
        if (cfg.initialValue () != null) {
            setValue (cfg.initialValue ());
        }
        setPlaceholder (cfg.placeholder ());
        if (cfg.valueChangeListener () != null) {
            addValueChangeListener (cfg.valueChangeListener ());
        }

        setSuffixComponent (controlButtons);
    }

}
