package ru.org.sevn.va.renderer.lit;

import com.vaadin.flow.data.renderer.LitRenderer;

public class Tags<OBJ> {
    private final Tag [] tags;

    public Tags (Tag<OBJ>... tags) {
        this.tags = tags;
    }

    public LitRenderer<OBJ> getLitRenderer () {
        java.lang.StringBuilder sb = new StringBuilder ();
        sb.append ("<div style='display: ruby;'>");
        for (var tag : tags) {
            tag.templateBuild (sb);
            sb.append (" ");
        }
        sb.append ("</div>");

        com.vaadin.flow.data.renderer.LitRenderer<OBJ> res = LitRenderer.<OBJ> of (sb.toString ());
        for (var tag : tags) {
            tag.templateProperties (res);
        }

        return res;
    }
}
