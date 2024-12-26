package io.github.reveredoxygen.configpatcher;

import io.github.reveredoxygen.configpatcher.patches.*;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class Patch {
    // I couldn't find a nicer design that GSON likes and would allow adding more patch types later

    private String target;
    private String description;

    // Only one of these will be initialized, depending on the json input
    private RegexPatch regex;
    private AppendLinesPatch appendLines;
    private ReplaceStringPatch replaceString;
    private CopyFilePatch copyFile;
    private DeleteFilePatch deleteFile;

    public void apply() throws Exception {
        Path targetPath = FileSystems.getDefault().getPath(target);
        targetPath = ConfigPatcher.GAME_PATH.resolve(targetPath);

        if (regex != null) {
            regex.apply(targetPath);
        } else if (appendLines != null) {
            appendLines.apply(targetPath);
        } else if (replaceString != null) {
            replaceString.apply(targetPath);
        } else if (copyFile != null) {
            copyFile.apply(targetPath);
        } else if (deleteFile != null) {
            deleteFile.apply(targetPath);
        }
    }

    public String getType() {
        if (regex != null) {
            return "regex";
        } else if (appendLines != null) {
            return "appendLines";
        } else if (replaceString != null) {
            return "replaceString";
        } else if (copyFile != null) {
            return "copyFile";
        } else if (deleteFile != null) {
            return "deleteFile";
        }

        return "INVALID";
    }

    @Override
    public String toString() {
        return "Patch{" +
                "type=" + getType() +
                ", description='" + description + '\'' +
                ", target=" + target +
                '}';
    }
}
