package io.github.reveredoxygen.configpatcher.patches;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteFilePatch {
    public void apply(Path target) throws IOException {
        Files.delete(target);
    }
}
