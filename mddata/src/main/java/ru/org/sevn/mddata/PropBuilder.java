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

import com.vladsch.flexmark.ast.AutoLink;
import com.vladsch.flexmark.ast.BulletList;
import com.vladsch.flexmark.ast.BulletListItem;
import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.ast.Link;
import com.vladsch.flexmark.ast.LinkRef;
import com.vladsch.flexmark.ast.Paragraph;
import com.vladsch.flexmark.ast.Text;
import com.vladsch.flexmark.util.ast.Node;

public class PropBuilder {

    private final Prop p;

    public PropBuilder (Prop p) {
        this.p = p;
    }

    public Prop getObject () {
        return p;
    }

    public void read (BulletListItem h) {
        for (Node n : h.getChildren ()) {
            p.addListValue (h.getChildChars ().toString ().trim ());
        }
    }

    public void read (BulletList h) {
        for (Node n : h.getChildren ()) {
            if (n instanceof BulletListItem) {
                read ((BulletListItem) n);
            }
        }
    }

    public void read (Heading h) {
        p.addValue (h.getText ().toString ());
    }

    public void read (Link l) {
        p.addValue (l.getUrl ().toString ());
    }

    public void read (AutoLink l) {
        p.addValue (l.getUrl ().toString ());
    }

    public void read (LinkRef l) {
        p.addValue (l.getReference ().toString ());
    }

    public void read (Text l) {
        p.addValue (l.getChars ().toStringOrNull ());
    }

    public void read (Paragraph pg) {
        for (Node n : pg.getChildren ()) {

            if (n instanceof Link) {
                read ((Link) n);
                break;
            }
            else if (n instanceof AutoLink) {
                read ((AutoLink) n);
                break;
            }
            else if (n instanceof LinkRef) {
                read ((LinkRef) n);
                break;
            }
            else if (n instanceof Text) {
                read ((Text) n);
                break;
            }
        }
    }

}
