package ru.org.sevn.va;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import java.util.UUID;
import java.util.function.Consumer;

public class VaUtil {
    public static <T extends com.vaadin.flow.component.Component> T idcmp (T cmp) {
        cmp.setId (UUID.randomUUID ().toString ().replace ("-", ""));
        return cmp;
    }

    public static <T extends HasStyle> T addClassName (final T cmp, String... classNames) {
        for (final String s : classNames) {
            cmp.addClassName (s);
        }
        return cmp;
    }

    public static void getPageTitle (Consumer<String> pageTitleConsumer) {
        UI.getCurrent ().getPage ().executeJs ("return document.title").then (String.class, pageTitle -> {
            pageTitleConsumer.accept (pageTitle);
        });
    }

    public static void getClientWidth (final String id, Consumer<Integer> clientWidthConsumer) {
        UI.getCurrent ().getPage ().executeJs ("return document.getElementById('" + id + "').clientWidth").then (Integer.class, clientWidth -> {
            clientWidthConsumer.accept (clientWidth);
        });
    }

    public static void getClientHeight (final String id, Consumer<Integer> clientHeightConsumer) {
        UI.getCurrent ().getPage ().executeJs ("return document.getElementById('" + id + "').clientHeight").then (Integer.class, clientHeight -> {
            clientHeightConsumer.accept (clientHeight);
        });
    }

    public static boolean isDisplayed (Component cmp) {
        if (cmp.isVisible ()) {
            var parent = cmp.getParent ().orElse (null);
            if (parent != null) {
                return isDisplayed (parent);
            }
            else {
                return true;
            }
        }
        else {
            return false;
        }
    }

}
