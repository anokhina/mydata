/*
 * Copyright 2022 Veronica Anokhina.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.org.sevn.mddata;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@ToString
@Getter
@Setter
@Accessors (fluent = true, chain = true)
public class ItemInfo {
    private String lang;
    private String path;
    private String title;
    private String author;
    private String description;
    private Set<String> tags = new HashSet ();
    private String img;
    private String url;
    private List<String> content = new ArrayList ();
    private boolean indexed;
    private boolean changed;
    private Date date;
    private Map<String, Long> contentSize = new HashMap ();

    public String getTitle () {
        return title;
    }

    public ItemInfo setTitle (String title) {
        this.title = title;
        if (this.title == null) {
            this.title = "";
        }
        return this;
    }

    public String getAuthor () {
        return author;
    }

    public ItemInfo setAuthor (String author) {
        this.author = author;
        return this;
    }

    public String getDescription () {
        return description;
    }

    public ItemInfo setDescription (String description) {
        this.description = description;
        return this;
    }

    public Set<String> getTags () {
        return tags;
    }

    public ItemInfo setTags (Set<String> tags) {
        this.tags = tags;
        return this;
    }

    public String getImg () {
        return img;
    }

    public ItemInfo setImg (String img) {
        this.img = img;
        return this;
    }

    public String getUrl () {
        return url;
    }

    public ItemInfo setUrl (String url) {
        this.url = url;
        return this;
    }

    public boolean isContentEmpty () {
        return this.content.isEmpty ();
    }

    public List<String> getContent () {
        return content;
    }

    public ItemInfo addContent (String content) {
        if (content.trim ().length () > 0) {
            this.content.add (content);
        }
        return this;
    }

    public boolean isIndexed () {
        return indexed;
    }

    public ItemInfo setIndexed (boolean indexed) {
        this.indexed = indexed;
        return this;
    }

    public boolean isChanged () {
        return changed;
    }

    public ItemInfo setChanged (boolean changed) {
        this.changed = changed;
        return this;
    }

    public Date getDate () {
        return date;
    }

    public ItemInfo setDate (Date date) {
        this.date = date;
        return this;
    }

    public String getPath () {
        return path;
    }

    public ItemInfo setPath (String path) {
        this.path = path;
        return this;
    }

    public Long getContentSize (String content) {
        return contentSize.get (content);
    }

    public void addContentSize (String content, Long contentSize) {
        this.contentSize.put (content, contentSize);
    }

    public String getLang () {
        return lang;
    }

    public ItemInfo setLang (String lang) {
        this.lang = lang;
        return this;
    }

}
///home/sevn/winh/work/doc/links/files/202201/16/000750
/*
[//]: # (title)
## _Title_

[//]: # (author)
_author_

[//]: # (dsc)
_Description_

[//]: # (tags)
_tags_

[//]: # (img)
[<img src="img.png" width="150"/>](img.png)

[//]: # (url)
[_url_](_url_)

[//]: # (content)
_content_

[//]: # (indexed=false)
[//]: # (changed=false)
[//]: # (date=_date_)
[//]: # (date +%F_%T)

*/