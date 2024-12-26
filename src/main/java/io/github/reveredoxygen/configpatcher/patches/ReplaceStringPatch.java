package io.github.reveredoxygen.configpatcher.patches;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ReplaceStringPatch {
    private String original;
    private String replacement;

    public void apply(Path target) throws IOException, RuntimeException {
        String oldText = Files.readString(target);
        String newText = oldText.replace(original, replacement);

        if (oldText.equals(newText)) {
            throw new RuntimeException("Failed to find target for replacement");
        }

        Files.writeString(target, newText);
    }
}
