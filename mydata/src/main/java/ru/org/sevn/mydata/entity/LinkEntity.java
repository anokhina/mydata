package ru.org.sevn.mydata.entity;

import java.util.ArrayList;
import ru.org.sevn.entity.DbEntity;
import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Language;
import ru.org.sevn.entity.Identified;

@Document (language = "russian")
@Getter
@Setter
@Accessors (fluent = true, chain = true)
@EqualsAndHashCode (onlyExplicitlyIncluded = true, callSuper = false)
@ToString
public class LinkEntity extends DbEntity implements Identified<String> {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @TextIndexed
    private String title;
    @Indexed
    private List<String> author;
    @TextIndexed
    private String dsc;
    @DBRef
    private List<TagEntity> tags = new ArrayList ();
    private String img;
    @Indexed
    private String url;

    @Language
    private String lang;

    @Override
    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public List<String> getAuthor () {
        return author;
    }

    public void setAuthor (List<String> author) {
        this.author = author;
    }

    public String getDsc () {
        return dsc;
    }

    public void setDsc (String dsc) {
        this.dsc = dsc;
    }

    public List<TagEntity> getTags () {
        return tags;
    }

    public void setTags (List<TagEntity> tags) {
        this.tags = tags;
    }

    public String getImg () {
        return img;
    }

    public void setImg (String img) {
        this.img = img;
    }

    public String getUrl () {
        return url;
    }

    public void setUrl (String url) {
        this.url = url;
    }

    public String getLang () {
        return lang;
    }

    public void setLang (String lang) {
        this.lang = lang;
    }

}
/*
[//]: # (title)
## Школа игры на фортепиано

[//]: # (author)
прд ред. Николаева А.

[//]: # (dsc)



[//]: # (tags)
- music
- фортепиано

[//]: # (img)
[<img src="img.png" width="150"/>](img.png)

[//]: # (url)

[//]: # (content)
[школа игры николаева.pdf](школа игры николаева.pdf)

[//]: # (indexed=false)
[//]: # (changed=false)
[//]: # (date=2022-01-11_16:15:41)
[//]: # (date +%F_%T)

*/