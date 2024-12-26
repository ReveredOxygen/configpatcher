package io.github.reveredoxygen.configpatcher.patches;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class AppendLinesPatch {
    private List<String> lines;

    public void apply(Path target) throws IOException {
            List<String> newLines = Files.readAllLines(target);
            newLines.addAll(lines);
            Files.write(target, newLines);
    }
}
