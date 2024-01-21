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

import com.vladsch.flexmark.ast.BulletList;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Reference;
import com.vladsch.flexmark.util.ast.Node;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;

public class ItemInfoBuilder {

    private final ItemInfo ii;

    public ItemInfoBuilder (ItemInfo ii) {
        this.ii = ii;
    }

    public ItemInfo getObject () {
        return ii;
    }

    public ItemInfo addProp (Prop p) throws Exception {
        if (p.getValue () != null) {
            switch (p.getName ()) {
                case "path" :
                    return ii.setPath (p.getValue ());
                case "title" :
                    return ii.setTitle (p.getValue ());
                case "author" :
                    return ii.setAuthor (p.getValue ());
                case "lang" :
                    return ii.setLang (p.getValue ());
                case "dsc" :
                case "description" :
                    return ii.setDescription (p.getValue ());
                case "tags" :
                    ii.getTags ().addAll (Arrays.asList (p.getValue ().split (Prop.LIST_SEPARATOR)));
                    return ii;
                case "img" :
                    return ii.setImg (p.getValue ());
                case "url" :
                    return ii.setUrl (p.getValue ());
                case "content" :
                    if (p.getValue () != null) {
                        return ii.addContent (URLDecoder.decode (p.getValue (), "UTF-8"));
                    }
                case "indexed" :
                    return ii.setIndexed (Boolean.valueOf (p.getValue ()));
                case "changed" :
                    return ii.setChanged (Boolean.valueOf (p.getValue ()));
                case "date" :
                    SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd_HH:mm:ss");//2022-01-11_16:50:22
                    return ii.setDate (sdf.parse (p.getValue ()));
            }
        }
        return ii;
    }

    public ItemInfo fromNode (final Node doc) throws Exception {
        PropBuilder pb = null;
        for (Node n : doc.getChildren ()) {
            if (n instanceof Reference) {
                if (pb != null && pb.getObject ().getName () != null) {
                    addProp (pb.getObject ());
                }
                Reference r = (Reference) n;
                String title = r.getTitle ().toStringOrNull ();
                if (title != null) {
                    pb = new PropBuilder (new Prop (title));
                }
                else {
                    pb = null;
                }
            }
            else if (pb != null) {
                if (n instanceof Heading) {
                    pb.read ((Heading) n);
                }
                else if (n instanceof BulletList) {
                    pb.read ((BulletList) n);
                }
                else if (n instanceof Paragraph) {
                    pb.read ((Paragraph) n);
                }
            }
        }
        return ii;
    }

}
