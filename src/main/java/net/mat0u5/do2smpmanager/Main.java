package net.mat0u5.do2smpmanager;

import net.fabricmc.api.ModInitializer;

import net.mat0u5.do2smpmanager.config.ConfigManager;
import net.mat0u5.do2smpmanager.utils.ModRegistries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main implements ModInitializer {
	public static final String MOD_ID = "do2smpmanager";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static ConfigManager config;

	@Override
	public void onInitialize() {
		config = new ConfigManager("./config/"+MOD_ID+".properties");

		ModRegistries.registerModStuff();
		LOGGER.info("Initializing DO2SMP Manager...");
	}
}