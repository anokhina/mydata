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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.extern.java.Log;
import ru.org.sevn.log.LogUtil;

@Log
public class ItemInfoBuilder {

    private final ItemInfo ii;

    public ItemInfoBuilder (ItemInfo ii) {
        this.ii = ii;
    }

    public ItemInfo getObject () {
        return ii;
    }

    private static final String nl = "\n";

    private static String nn (String s) {
        return s == null ? "" : s;
    }

    private static void print (StringBuilder sb, String propName, String propValue) {
        sb.append (nl).append ("[//]: # (" + propName + ")");
        if (propValue != null) {
            sb.append (nl);
            sb.append (nn (propValue)).append (nl);
        }
    }

    private static String img (String s) {
        return "[<img src=\"" + s + "\" width=\"150\"/>](" + s + ")";
    }

    private static String code (String s) {
        //TODO replacement
        return "`" + s.replace ("`", "") + "`";
    }

    private static String content (String s) {
        return "[" + code (s) + "](" + URLEncoder.encode (s, StandardCharsets.UTF_8) + ")";
    }

    private String date (Date date) {
        if (date != null) {
            var sdf = new SimpleDateFormat ("yyyy-MM-dd_HH:mm:ss");
            return sdf.format (date);
        }
        return null;
    }

    public String print () {
        StringBuilder sb = new StringBuilder ();

        print (sb, "title", "## " + nn (ii.title ()));
        print (sb, "author", ii.author ());
        print (sb, "dsc", ii.description ());
        print (sb, "tags", ii.tags ().stream ().map (e -> "- " + e).collect (Collectors.joining ("  \n")));
        print (sb, "img", img (ii.img ())); //TODO
        print (sb, "url", ii.url ()); //TODO

        if (ii.content ().isEmpty ()) {
            print (sb, "content", null);
        }
        else {
            ii.content ().forEach (c -> {
                print (sb, "content", content (c));
            });
        }

        print (sb, "indexed=" + ii.indexed (), null);
        print (sb, "changed=" + ii.changed (), null);
        print (sb, "date=" + date (ii.date ()), null);
        print (sb, "date +%F_%T", null);
        return sb.toString ();
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
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd_HH:mm:ss");//2022-01-11_16:50:22
                        return ii.setDate (sdf.parse (p.getValue ()));
                    }
                    catch (Exception ex) {
                        LogUtil.error (log, null, ex);
                    }
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
