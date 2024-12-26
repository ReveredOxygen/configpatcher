package io.github.reveredoxygen.configpatcher;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;

// We can pretend to be a language adapter to load before any other mod
// This is important because if we modify their configs, we want to do it before they can load them
// This trick is stolen from YOSBR
public class ConfigPatcher implements LanguageAdapter {
    public static final String MOD_ID = "configpatcher";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("configpatcher");
    public static final Path GAME_PATH = FabricLoader.getInstance().getGameDir();

    public ConfigPatcher() {
        try {
            Files.list(CONFIG_PATH).filter(Files::isDirectory).forEachOrdered(ConfigPatcher::processPatchSet);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    private static void processPatchSet(Path path) {
        String name = path.getName(path.getNameCount() - 1).toString();

        // Load the current pack version
        String currentVersion;
        try {
            currentVersion = Files.readString(path.resolve("current-version.txt"));
        } catch (IOException e) {
            currentVersion = "";
        }
        if (currentVersion.isBlank()) {
            LOGGER.error("{} does not specify version, skipping", name);
            return;
        }

        // Load the patch config
        Map<String, Patch[]> patchMap;
        try {
            Gson gson = new Gson();
            TypeToken<Map<String, Patch[]>> patchMapType = new TypeToken<>() {
            };

            String patchesJson = Files.readString(path.resolve("patches.json"));
            patchMap = gson.fromJson(patchesJson, patchMapType);
        } catch (Exception e) {
            LOGGER.error(e.toString());
            patchMap = null;
        }

        if (patchMap == null) {
            LOGGER.error("Failed to load patches for {}", name);
            return;
        }

        // Load the version from last run
        String previousVersion;
        try {
            previousVersion = Files.readString(path.resolve("previous-version.txt"));
        } catch (IOException e) {
            previousVersion = "";
        }

        if (previousVersion.isBlank()) {
            LOGGER.info("First run for {}, assuming up to date", name);
            try {
                Files.writeString(path.resolve("previous-version.txt"), currentVersion);
            } catch (IOException e) {
                LOGGER.error(e.toString());
            }
            return;
        }

        // If we're up to date, we don't have to do anything
        if (semverCompare(previousVersion, currentVersion) == 0) {
            LOGGER.info("{} is up to date, skipping", name);
            return;
        }
        if (semverCompare(previousVersion, currentVersion) > 0) {
            LOGGER.warn("Version for {} went backwards, skipping", name);
            return;
        }

        // Put the patch versions in order
        String finalPreviousVersion = previousVersion;
        List<Map.Entry<String, Patch[]>> patches = patchMap.entrySet()
                .stream()
                .filter(x -> semverCompare(finalPreviousVersion, x.getKey()) < 0)
                .sorted((l, r) -> semverCompare(l.getKey(), r.getKey()))
                .toList();

        int patchCount = 0;
        int successCount = 0;

        for (Map.Entry<String, Patch[]> version : patches) {
            for (Patch patch : version.getValue()) {
                patchCount++;
                try {
                    patch.apply();
                    LOGGER.info("Applied {} for version {}", patch, version.getKey());
                    successCount++;
                } catch (Exception e) {
                    LOGGER.error("Failed applying {}: {}", patch, e);
                }
            }
        }

        LOGGER.info("Applied {}/{} patches for {}", successCount, patchCount, name);

        // If everything went well, update the last seen version
        try {
            Files.writeString(path.resolve("previous-version.txt"), currentVersion);
        } catch (IOException e) {
            LOGGER.error(e.toString());
        }
    }

    // Not a real language adapter so we don't need this
    @Override
    public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
        throw new IllegalStateException();
    }

    public static int semverCompare(String left, String right) {
        String[] leftSegments = left.split("[-+]")[0].split("\\.");
        String[] rightSegments = right.split("[-+]")[0].split("\\.");

        for (int i = 0; i < Math.min(leftSegments.length, rightSegments.length); i++) {
            int leftSeg = Integer.parseInt(leftSegments[i]);
            int rightSeg = Integer.parseInt(rightSegments[i]);

            int cmp = Integer.compare(leftSeg, rightSeg);

            if (cmp != 0) {
                return cmp;
            }
        }

        return 0;
    }
}