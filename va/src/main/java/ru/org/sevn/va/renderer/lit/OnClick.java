package ru.org.sevn.va.renderer.lit;

import com.vaadin.flow.data.renderer.LitRenderer;
import java.util.function.BiConsumer;

public class OnClick<OBJ> extends TagAttribute<OBJ> {

    private final BiConsumer<OBJ, ClickEvent> onClick;

    public OnClick (BiConsumer<OBJ, ClickEvent> onClick) {
        super ("onClick",
                (ta) -> {
                    return "@click=${(e) => {" + ta.getPropertyId () + "(e.shiftKey, e.ctrlKey, e.altKey, e.metaKey); e.preventDefault();}}";
                });
        this.onClick = onClick;
    }

    public static <OBJ> OnClick<OBJ> of (Class<OBJ> cls, BiConsumer<OBJ, ClickEvent> onClick) {
        return new OnClick (onClick);
    }

    @Override
    public LitRenderer<OBJ> addValueFuntionality (LitRenderer<OBJ> lr) {
        lr.withFunction (getPropertyId (), (el, args) -> {
            var evt = new ClickEvent ();
            evt.shiftKey = args.getBoolean (0);
            evt.ctrlKey = args.getBoolean (1);
            evt.altKey = args.getBoolean (2);
            evt.metaKey = args.getBoolean (3);
            onClick.accept (el, evt);
        });
        return lr;
    }

}
