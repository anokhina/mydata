package ru.org.sevn.va.renderer.lit;

import com.vaadin.flow.data.renderer.LitRenderer;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LitRendererFactory {

    public static <OBJ> LitRenderer<OBJ> divText (
            Function<OBJ, String> textProducer, Function<OBJ, String> classNameProducer, Function<OBJ, String> toolTipProducer, BiConsumer<OBJ, ClickEvent> onClick) {
        return new Tag ("div")
                .addTagContent (textProducer)
                .addAttribute (new TitleTagAttribute (toolTipProducer))
                .addAttribute (new ClassTagAttribute (classNameProducer))
                .addAttribute (new OnClick (onClick))
                .getLitRenderer ();
    }

    public static <OBJ> LitRenderer<OBJ> link (
            Function<OBJ, String> textProducer, Function<OBJ, String> classNameProducer, Function<OBJ, String> toolTipProducer, Function<OBJ, String> ref) {
        return new Tag ("a")
                .addTagContent (textProducer)
                .addAttribute (new TitleTagAttribute (toolTipProducer))
                .addAttribute (new ClassTagAttribute (classNameProducer))
                /////////
                .addAttribute (new AnyTagAttribute ("url", ref))
                .getLitRenderer ();
    }

    public static <OBJ> LitRenderer<OBJ> button (
            String text, Function<OBJ, String> toolTipProducer, Function<OBJ, Boolean> disableProducer, BiConsumer<OBJ, ClickEvent> onClick) {
        return new Tag ("vaadin-button")
                .addTagContent (el -> text)
                .addAttribute (new TitleTagAttribute (toolTipProducer))
                .addAttribute (new OnClick (onClick))
                //.addAttribute(new DisabledTagAttribute(e -> true))
                .getLitRenderer ();
    }

    public static <OBJ> LitRenderer<OBJ> checkBox (
            boolean readOnly, Function<OBJ, Boolean> booleanProducer, Function<OBJ, String> classNameProducer, Function<OBJ, String> toolTipProducer, BiConsumer<OBJ, ClickEvent> onClick) {
        return new Tag ("vaadin-checkbox")
                .addAttribute (new ClassTagAttribute (classNameProducer))
                .addAttribute (new TitleTagAttribute (toolTipProducer))
                .addAttribute (new OnClick (onClick))
                .addAttribute (new ReadonlyTagAttribute (e -> readOnly))
                .addAttribute (new AnyBooleanTagAttribute<> ("checked", (ta) -> "?checked='${item." + ta.getPropertyId () + "}'", booleanProducer))
                .getLitRenderer ();
    }
}
