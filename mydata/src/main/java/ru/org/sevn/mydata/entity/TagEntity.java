package ru.org.sevn.mydata.entity;

import lombok.EqualsAndHashCode;
import ru.org.sevn.entity.DbEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.org.sevn.entity.Identified;

@Document
@Getter
@Setter
@Accessors (fluent = true, chain = true)
@EqualsAndHashCode (onlyExplicitlyIncluded = true, callSuper = false)
@ToString
public class TagEntity extends DbEntity implements Identified<String> {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Indexed (unique = true)
    private String value;

    @Override
    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

}