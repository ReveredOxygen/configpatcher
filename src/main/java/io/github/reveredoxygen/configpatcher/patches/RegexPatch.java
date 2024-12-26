package io.github.reveredoxygen.configpatcher.patches;

import io.github.reveredoxygen.configpatcher.ConfigPatcher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RegexPatch {
    private String regex;
    private String replacement;

    public void apply(Path target) throws IOException {
            String newText = Files.readString(target).replaceAll(regex, replacement);
            Files.writeString(target, newText);
    }
}
