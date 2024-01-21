package ru.org.sevn.va.data;

import java.util.stream.Stream;

public class DefaultModelConverter<T> implements ModelConverter<T, T> {

    @Override
    public T fromModel (T m) {
        return m;
    }

    @Override
    public T toModel (T t) {
        return t;
    }

    @Override
    public Stream<T> toModels (Stream<T> ts) {
        return ts;
    }

    @Override
    public Stream<T> fromModels (Stream<T> ms) {
        return ms;
    }

}
