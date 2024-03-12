package ru.org.sevn.va.scroll;

import com.vaadin.flow.component.orderedlayout.Scroller;

public interface UseScroll {

    default Scroller.ScrollDirection useScrollDirection () {
        return Scroller.ScrollDirection.NONE;
    }

    public static void setScrollDirection (Scroller scroller) {
        if (scroller.getContent () instanceof UseScroll) {
            scroller.setScrollDirection ( ((UseScroll) scroller.getContent ()).useScrollDirection ());
        }
        else {
            scroller.setScrollDirection (Scroller.ScrollDirection.BOTH);
        }
    }
}
