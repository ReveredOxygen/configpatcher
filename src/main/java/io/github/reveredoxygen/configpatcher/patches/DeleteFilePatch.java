package io.github.reveredoxygen.configpatcher.patches;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class DeleteFilePatch {
    public void apply(Path target) throws IOException {
        // Recursively delete if target is a directory
        if (Files.isDirectory(target)) {
            // Code taken from https://stackoverflow.com/a/27917071
            Files.walkFileTree(target, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    super.postVisitDirectory(dir, exc);
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            Files.delete(target);
        }
    }
}
