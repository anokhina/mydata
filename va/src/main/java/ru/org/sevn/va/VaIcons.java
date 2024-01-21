package ru.org.sevn.va;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import java.util.function.Supplier;

public class VaIcons {
    public static final Supplier<Component> NEW = () -> VaadinIcon.FILE_ADD.create ();
    public static final Supplier<Component> DEL = () -> VaadinIcon.TRASH.create ();
    public static final Supplier<Component> CLEAR = () -> VaadinIcon.FILE_REMOVE.create ();
    public static final Supplier<Component> SAVE = () -> VaadinIcon.CHECK.create ();
    public static final Supplier<Component> OK = () -> VaadinIcon.CHECK.create ();
    public static final Supplier<Component> CANCEL = () -> new Icon ("lumo", "cross");
    public static final Supplier<Component> YES = () -> VaadinIcon.CHECK.create ();
}
