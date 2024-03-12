package ru.org.sevn.mydata.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StringLongObject {
    private String key;
    private Long value;
}
