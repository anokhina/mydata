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

import com.vladsch.flexmark.ext.attributes.AttributesExtension;
import com.vladsch.flexmark.ext.autolink.AutolinkExtension;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughSubscriptExtension;
import com.vladsch.flexmark.ext.gfm.tasklist.TaskListExtension;
import com.vladsch.flexmark.ext.ins.InsExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.ext.typographic.TypographicExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.test.util.AstCollectingVisitor;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.DataHolder;
import com.vladsch.flexmark.util.data.MutableDataSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class MdFileParser {
    final private static DataHolder OPTIONS = new MutableDataSet ()
            .set (Parser.EXTENSIONS, Arrays.asList (
                    //AbbreviationExtension
                    //AnchorLinkExtension.create(),
                    //AsideExtension
                    //DefinitionExtension
                    //EmojiExtension
                    //EnumeratedReferenceExtension
                    //FootnoteExtension
                    //GfmIssuesExtension
                    //GfmUsersExtension
                    //GitLabExtension
                    //JekyllTagExtension
                    //JiraConverterExtension
                    //MacroExtension
                    //SuperscriptExtension
                    //TocExtension
                    //SimTocExtension
                    //WikiLinkExtension
                    //MacroExtension
                    //YamlFrontMatterExtension
                    //YouTrackConverterExtension
                    //YouTubeLinkExtension
                    InsExtension.create (),
                    AttributesExtension.create (), AutolinkExtension.create (), //StrikethroughExtension.create() //or
                    StrikethroughSubscriptExtension.create (), TablesExtension.create (), TaskListExtension.create (), TypographicExtension.create ()))
    //.set(HtmlRenderer.SOFT_BREAK, "<br />\n")// uncomment to convert soft-breaks to hard breaks
    ;

    public static Parser getParser () {
        Parser parser = Parser.builder (OPTIONS).build ();
        return parser;
    }

    public static Node parse (final String src) {
        return getParser ().parse (src);
    }

    public static void ast (final Node document) {
        System.out.println ("\n---- AST ------------------------\n");
        System.out.println (new AstCollectingVisitor ().collectAndGetAstText (document));
    }

    public static String readFile (final Path p) throws IOException {
        try (final ByteArrayOutputStream baos = new ByteArrayOutputStream ()) {
            Files.copy (p, baos);
            final String str = new String (baos.toByteArray (), StandardCharsets.UTF_8);
            return str;
        }
    }

    public static HtmlRenderer getHtmlRenderer () {
        return HtmlRenderer.builder (OPTIONS).build ();
    }
}
