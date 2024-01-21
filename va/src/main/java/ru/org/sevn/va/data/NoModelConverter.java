package ru.org.sevn.va.data;

public class NoModelConverter<T> implements ModelConverter<T, T> {

    @Override
    public T fromModel (T m) {
        return m;
    }

    @Override
    public T toModel (T t) {
        return t;
    }

}
