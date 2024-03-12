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

import com.vladsch.flexmark.util.ast.Node;
import java.io.File;
import java.nio.file.Path;
import java.util.logging.Level;
import lombok.extern.java.Log;
import org.json.JSONObject;

@Log
public class FileIndexer {

    public static ItemInfo getItemInfo (final Path filePath) throws Exception {
        log.log (Level.INFO, "Try to parse: {0}", filePath.toString ());
        final String src = MdFileParser.readFile (filePath);

        final Node doc = MdFileParser.parse (src);

        ItemInfoBuilder iib = new ItemInfoBuilder (new ItemInfo ().setPath (filePath.toString ()));
        ItemInfo ii = iib.fromNode (doc);

        if (! ii.isContentEmpty ()) {
            for (String c : ii.getContent ()) {
                final File cf = new File (filePath.toFile ().getParentFile (), c);
                if (cf.exists ()) {
                    ii.addContentSize (c, cf.length ());
                }
            }
        }
        if ( (ii.getUrl () == null || ii.getUrl ().trim ().length () == 0) ||
                (ii.isContentEmpty ())) {

            return ii;
        }
        else {
            MdFileParser.ast (doc);
            JSONObject jo = new JSONObject (ii);
            log.severe (">>>" + jo.toString (2));
        }
        throw new Exception ("Can't parse " + filePath);
    }

    public static String print (ItemInfo ii) {
        ItemInfoBuilder iib = new ItemInfoBuilder (ii);
        return iib.print ();
    }
}
