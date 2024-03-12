package ru.org.sevn.va.scroll;

import com.vaadin.flow.component.orderedlayout.Scroller;

public interface UseScrollVertical extends UseScroll {

    @Override
    default Scroller.ScrollDirection useScrollDirection () {
        return Scroller.ScrollDirection.VERTICAL;
    }

}
