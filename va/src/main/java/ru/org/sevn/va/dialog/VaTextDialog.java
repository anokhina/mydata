package ru.org.sevn.va.dialog;

import com.vaadin.flow.component.html.Div;

public class VaTextDialog extends VaDialog<Div> {

    public VaTextDialog (String msg) {
        this (ATTENTION, msg, BUTTON_OK);
    }

    public VaTextDialog (String title, String msg, String closeButtonTitle) {
        super (title, new Div (msg));
        addButton (closeButtonTitle, () -> true);
    }
}
