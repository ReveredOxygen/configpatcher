package io.github.reveredoxygen.configpatcher;

import net.fabricmc.loader.api.LanguageAdapter;
import net.fabricmc.loader.api.LanguageAdapterException;
import net.fabricmc.loader.api.ModContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// We can pretend to be a language adapter to load before any other mod
// This is important because if we modify their configs, we want to do it before they can load them
// This trick is stolen from YOSBR
public class ConfigPatcher implements LanguageAdapter {
	public static final String MOD_ID = "configpatcher";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public ConfigPatcher() {
		LOGGER.info("Hello Fabric world!");
	}

	// Not a real language adapter so we don't need this
	@Override
	public <T> T create(ModContainer mod, String value, Class<T> type) throws LanguageAdapterException {
		throw new IllegalStateException();
	}
}