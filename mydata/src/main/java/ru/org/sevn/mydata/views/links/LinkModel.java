package ru.org.sevn.mydata.views.links;

import java.util.HashSet;
import java.util.Set;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.org.sevn.mydata.entity.LinkEntity;
import ru.org.sevn.mongo.IndexLanguageEnum;
import ru.org.sevn.mydata.entity.TagEntity;

@Getter
@Setter
@Accessors (fluent = true, chain = true)
public class LinkModel {

    @NotEmpty
    @NotNull
    private String title;
    private String author;
    private String dsc;
    @NotEmpty
    @NotNull
    private Set<TagEntity> tags = new HashSet ();
    private String img;
    @NotEmpty
    @NotNull
    private String url;

    private IndexLanguageEnum lang;

    private LinkEntity entity;

    public String getTitle () {
        return title;
    }

    public void setTitle (String title) {
        this.title = title;
    }

    public String getAuthor () {
        return author;
    }

    public void setAuthor (String author) {
        this.author = author;
    }

    public String getDsc () {
        return dsc;
    }

    public void setDsc (String dsc) {
        this.dsc = dsc;
    }

    public Set<TagEntity> getTags () {
        return tags;
    }

    public void setTags (Set<TagEntity> tags) {
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

    public LinkEntity getEntity () {
        return entity;
    }

    public void setEntity (LinkEntity entity) {
        this.entity = entity;
    }

    public IndexLanguageEnum getLang () {
        return lang;
    }

    public void setLang (IndexLanguageEnum lang) {
        this.lang = lang;
    }

}
