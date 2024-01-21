package ru.org.sevn.mydata.views.files;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import static java.nio.file.FileVisitResult.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileWalker extends SimpleFileVisitor<Path> {

    private final String excludeName;
    private final PathMatcher matcher;
    private final FileProcessor fileProcessor;

    public FileWalker (final FileProcessor metadataExtractor, final String excludeName) {
        this.excludeName = excludeName;
        this.fileProcessor = metadataExtractor;
        matcher = FileSystems.getDefault ().getPathMatcher ("glob:" + excludeName);
    }

    boolean canWalk (Path file, BasicFileAttributes attrs) {
        final Path name = file.getFileName ();
        if (name != null && matcher.matches (name)) {
            System.out.println (file);
            return false;
        }
        if (attrs.isDirectory ()) {
            Path child = file.resolve (excludeName);
            if (child != null && child.toFile ().exists ()) {
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
