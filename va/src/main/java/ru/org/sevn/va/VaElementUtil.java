package ru.org.sevn.va;

import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.dom.DomListenerRegistration;
import com.vaadin.flow.dom.Element;

public class VaElementUtil {

    // events https://www.w3schools.com/jsref/dom_obj_event.asp

    public static DomListenerRegistration addClickListener (Element el, DomEventListener listener) {
        return el.addEventListener ("click", listener);
    }
}
