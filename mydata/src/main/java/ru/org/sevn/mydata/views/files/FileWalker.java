package ru.org.sevn.mydata.views.files;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWalker extends SimpleFileVisitor<Path> {

    private final List<String> excludeNames;
    private final List<PathMatcher> matchers;
    private final FileProcessor fileProcessor;

    public FileWalker (final FileProcessor metadataExtractor, final String... excludeName) {
        this.excludeNames = List.of (excludeName);
        this.fileProcessor = metadataExtractor;
        matchers = excludeNames.stream ().map (en -> FileSystems.getDefault ().getPathMatcher ("glob:" + en)).toList ();
    }

    boolean canWalk (Path file, BasicFileAttributes attrs) {
        final Path name = file.getFileName ();
        if (name != null && matchers.stream ().filter (m -> m.matches (name)).findAny ().isPresent ()) {
            System.out.println (file);
            return false;
        }
        if (attrs.isDirectory ()) {
            if (excludeNames
                    .stream ()
                    .map (excludeName -> file.resolve (excludeName))
                    .filter (child -> child != null && child.toFile ().exists ())
                    .findAny ()
                    .isPresent ()) {

                return false;
            }
        }
        return true;
    }

    @Override
    public FileVisitResult visitFile (Path file, BasicFileAttributes attrs) {
        if (canWalk (file, attrs)) {
            return processFile (file, attrs);
        }
        else {
            return CONTINUE;
        }
    }

    @Override
    public FileVisitResult preVisitDirectory (Path dir, BasicFileAttributes attrs) {
        if (canWalk (dir, attrs)) {
            return processFile (dir, attrs);
        }
        return SKIP_SUBTREE;
    }

    private FileVisitResult processFile (Path file, BasicFileAttributes attrs) {
        try {
            return fileProcessor.processFile (file, attrs);
        }
        catch (Exception ex) {
            //TODO
            Logger.getLogger (FileWalker.class.getName ()).log (Level.SEVERE, null, ex);
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed (Path file, IOException exc) {
        System.err.println (exc);
        return CONTINUE;
    }
}
