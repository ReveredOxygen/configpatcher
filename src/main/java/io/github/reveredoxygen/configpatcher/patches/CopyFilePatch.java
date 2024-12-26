package io.github.reveredoxygen.configpatcher.patches;

import io.github.reveredoxygen.configpatcher.ConfigPatcher;

import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class CopyFilePatch {
    private String source;

    public void apply(Path target) throws IOException {
            Files.copy(ConfigPatcher.GAME_PATH.resolve(source), target, StandardCopyOption.REPLACE_EXISTING);
    }
}
