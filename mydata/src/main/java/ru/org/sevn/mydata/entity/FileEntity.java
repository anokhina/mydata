package ru.org.sevn.mydata.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Language;
import ru.org.sevn.entity.DbEntity;
import ru.org.sevn.entity.Identified;

@Document (language = "russian")
@Getter
@Setter
@Accessors (fluent = true, chain = true)
@EqualsAndHashCode (onlyExplicitlyIncluded = true, callSuper = false)
@ToString
public class FileEntity extends DbEntity implements Identified<String> {
    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Indexed (unique = true)
    private String path;

    @TextIndexed
    private String dsc;

    @Indexed
    private String name;

    @Indexed
    private String ext;

    @Indexed
    private Long size;

    private boolean sortedOut;

    @Language
    private String lang;

    @Override
    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getPath () {
        return path;
    }

    public void setPath (String path) {
        this.path = path;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getExt () {
        return ext;
    }

    public void setExt (String ext) {
        this.ext = ext;
    }

    public Long getSize () {
        return size;
    }

    public void setSize (Long size) {
        this.size = size;
    }

    public boolean isSortedOut () {
        return sortedOut;
    }

    public void setSortedOut (boolean sortedOut) {
        this.sortedOut = sortedOut;
    }

}
