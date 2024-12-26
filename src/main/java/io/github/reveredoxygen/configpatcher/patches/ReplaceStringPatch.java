package io.github.reveredoxygen.configpatcher.patches;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReplaceStringPatch {
    private String original;
    private String replacement;

    public void apply(Path target) throws IOException {
        String newText = Files.readString(target).replace(original, replacement);
        Files.writeString(target, newText);
    }
}
