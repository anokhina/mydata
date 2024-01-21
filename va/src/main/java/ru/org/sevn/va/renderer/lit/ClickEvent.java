package ru.org.sevn.va.renderer.lit;

public class ClickEvent {
    public boolean shiftKey;
    public boolean ctrlKey;
    public boolean altKey;
    public boolean metaKey;

    public boolean isShiftKey () {
        return shiftKey;
    }

    public boolean isCtrlKey () {
        return ctrlKey;
    }

    public boolean isAltKey () {
        return altKey;
    }

    public boolean isMetaKey () {
        return metaKey;
    }
}
