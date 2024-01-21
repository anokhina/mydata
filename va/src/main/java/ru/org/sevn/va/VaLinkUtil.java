package ru.org.sevn.va;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import java.util.Optional;

public class VaLinkUtil {

    public static void openLocation (Component fromComponent, boolean inNewWindow, String href) {
        openLocation (fromComponent.getUI (), inNewWindow, href);
    }

    public static void openLocation (Optional<UI> getUI, boolean inNewWindow, String href) {
        if (inNewWindow) {
            getUI.ifPresent (ui -> ui.getPage ().open (href, "_blank"));
        }
        else {
            getUI.ifPresent (ui -> ui.getPage ().setLocation (href));
        }
    }

    public static void urlOpen (Optional<UI> ui, final String url) {
        ui.orElse (UI.getCurrent ()).getPage ().open (url);
    }

    public static void urlGo (Optional<UI> ui, final String url) {
        ui.orElse (UI.getCurrent ()).getPage ().setLocation (url);
    }
}
