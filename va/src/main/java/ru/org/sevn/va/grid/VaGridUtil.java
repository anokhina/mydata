package ru.org.sevn.va.grid;

import com.vaadin.flow.component.grid.Grid;

public class VaGridUtil {
    public static void selectFirst (final Grid grid) {
        try {
            grid.select (grid.getDataCommunicator ().getItem (0));
        }
        catch (IndexOutOfBoundsException ex) {
            //ok - no data
        }
    }

}
