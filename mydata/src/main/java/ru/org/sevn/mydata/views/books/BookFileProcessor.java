package ru.org.sevn.mydata.views.books;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import lombok.RequiredArgsConstructor;
import ru.org.sevn.mddata.FileIndexer;
import ru.org.sevn.mydata.entity.BookEntity;
import ru.org.sevn.mydata.repo.BookEntityRepository;
import ru.org.sevn.mydata.util.BookEntityComponent;
import ru.org.sevn.mydata.util.TagEntityComponent;
import ru.org.sevn.mydata.views.files.FileProcessor;

@RequiredArgsConstructor
public class BookFileProcessor implements FileProcessor {

    public static final String FILE_NAME_INDEXED = "indexed.md";

    private final Path dir;
    private final BookEntityRepository bookEntityRepository;
    private final TagEntityComponent tagEntityComponent;

    @Override
    public FileVisitResult processFile (Path filePath, BasicFileAttributes attrs) throws Exception {
        if (! attrs.isDirectory ()) {
            java.io.File file = filePath.toFile ();
            if (file.getName ().equalsIgnoreCase ("index.md")) {

                java.nio.file.Path relPath = dir.relativize (filePath);
                java.nio.file.Path relPathParent = relPath.getParent ();

                //TODO
                System.out.println ("------------" + dir.toString () + ": " + relPath);

                if (relPathParent != null) {
                    var iiSrc = BookEntityComponent.parse (filePath, relPathParent.toString ());

                    if (iiSrc != null && iiSrc.getTitle () != null) {
                        System.out.println (">>ii>" + iiSrc);

                        var entity = bookEntityRepository.findByPathId (iiSrc.pathId ()).orElseGet ( () -> new BookEntity ());

                        var be = BookEntityComponent.build (entity, iiSrc, tagEntityComponent);

                        bookEntityRepository.save (be);
                        System.out.println (">>ib>" + be);

                        var ii = BookEntityComponent.buildItemInfo (dir.toString (), be);
                        var indexedPath = Path.of (filePath.getParent ().toString (), FILE_NAME_INDEXED);
                        System.out.println ("----------ii--" + indexedPath);
                        Files.writeString (indexedPath, FileIndexer.print (ii), StandardCharsets.UTF_8);
                    }
                }
            }
        }
        return java.nio.file.FileVisitResult.CONTINUE;
    }

}
