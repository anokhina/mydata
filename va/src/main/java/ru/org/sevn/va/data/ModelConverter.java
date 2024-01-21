package ru.org.sevn.va.data;

import java.util.stream.Stream;

public interface ModelConverter<T, MODEL> {

    T fromModel (MODEL m);

    MODEL toModel (T t);

    default Stream<T> fromModels (Stream<MODEL> ms) {
        return ms.map (e -> fromModel (e));
    }

    default Stream<MODEL> toModels (Stream<T> ts) {
        return ts.map (e -> toModel (e));
    }
}
