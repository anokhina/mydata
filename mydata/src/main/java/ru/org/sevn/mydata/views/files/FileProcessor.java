package ru.org.sevn.mydata.views.files;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public interface FileProcessor {

    FileVisitResult processFile (Path file, BasicFileAttributes attrs) throws Exception;
}
