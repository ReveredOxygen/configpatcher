package io.github.reveredoxygen.configpatcher.patches;

import io.github.reveredoxygen.configpatcher.ConfigPatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegexPatch {
    private String regex;
    private String replacement;

    public void apply(Path target) throws IOException, RuntimeException {
        String oldText = Files.readString(target);
        String newText = oldText.replaceAll(regex, replacement);

        if (oldText.equals(newText)) {
            throw new RuntimeException("Failed to find target for regex");
        }

        Files.writeString(target, newText);
    }
}
