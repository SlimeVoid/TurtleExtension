package net.slimevoid.turtleextension.core.lib;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigurationLib {

	public static int shearUpgradeID = 100;

	private static File configurationFile;
	private static Configuration configuration;
	
	public static void CreateConfig(File configFile) {
        if (configurationFile == null) {
            configurationFile = configFile;
            configuration = new Configuration(configFile);
        }		
	}
	
	public static void ClientConfig() {
		configuration.load();
		configuration.save();
	}
	
	public static void CommonConfig() {
		configuration.load();
		
		shearUpgradeID = configuration.getInt("shearUpgradeID", Configuration.CATEGORY_GENERAL, shearUpgradeID, 65, 255, "Shear Upgrade ID");
		
		configuration.save();
	}
}
