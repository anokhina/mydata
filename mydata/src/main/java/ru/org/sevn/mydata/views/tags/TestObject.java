package ru.org.sevn.mydata.views.tags;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
public class TestObject {
    @Getter
    @Setter
    @ToString.Include
    @EqualsAndHashCode.Include
    private String value;
}
