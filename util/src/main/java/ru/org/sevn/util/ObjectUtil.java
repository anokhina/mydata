package ru.org.sevn.util;

import java.util.function.Consumer;
import lombok.Data;

public class ObjectUtil {

    @Data
    public static class ObjectValue<T> {
        private T value;

        public ObjectValue (T v) {
            this.value = v;
        }

        public T get () {
            return value;
        }

        public ObjectValue<T> apply (Consumer<T> c) {
            c.accept (value);
            return this;
        }
    }

    public static <T> ObjectValue<T> obj (T v) {
        return new ObjectValue (v);
    }

    public static <T> T apply (final T eb, final Consumer<T> consumer) {
        consumer.accept (eb);
        return eb;
    }
}
