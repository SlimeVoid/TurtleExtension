package net.slimevoid.turtleextension.core.lib;

import java.io.File;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.Configuration;
import net.slimevoid.turtleextension.core.TurtleExtensionCore;

public class ConfigurationLib {
	

	private static int upgradeID = 100;

	public static boolean shearUpgradesEnabled = true;
	private static String shearUpgradesName = "shearUpgrades";
	private static String shearUpgradesComment = "This will allow you to register additional Shear upgrades for turtles to use,\n"
												+ "in the format [UpgradeID]-[UnlocalizedName].\n"
												+ "If the list is empty this will disable the upgrades.";
	public static String[] shearUpgrades;
	
	public static boolean fishingUpgradeEnabled = true;
	public static String fishingUpgradeName = "fishingUpgrade";
	public static String fishingUpgradeComment = "This is the ID for the Fishing Rod upgrade, note that you should increment based on other upgrades.";
	public static int fishingUpgradeID;

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
		
		loadShearingUpgradeConfig();
		loadFishingUpgradeConfig();
		
		configuration.save();
		
		TurtleExtensionCore.registerUpgrades();
	}

	private static void loadShearingUpgradeConfig() {
		shearUpgradesEnabled = configuration.getBoolean("enableShearUpgrades", Configuration.CATEGORY_GENERAL, shearUpgradesEnabled, "Enables or Disables Shearing Upgrades");
		
		if (shearUpgradesEnabled) {
			
			String[] defaultShearUpgrade = { Integer.toString(upgradeID) + "-" + Item.itemRegistry.getNameForObject(Items.shears) };
			
			shearUpgrades = configuration.getStringList(shearUpgradesName, Configuration.CATEGORY_GENERAL, defaultShearUpgrade, shearUpgradesComment);
			
			if (shearUpgrades.length == 0) {
				shearUpgradesEnabled = false;
			} else {
				upgradeID += shearUpgrades.length;
			}
		}
	}
	
	private static void loadFishingUpgradeConfig() {
		fishingUpgradeEnabled = configuration.getBoolean("enableFishingUpgrade", Configuration.CATEGORY_GENERAL, fishingUpgradeEnabled, "Enables or Disables the Fishing Upgrade");
		
		if (fishingUpgradeEnabled) {
			fishingUpgradeID = configuration.getInt(fishingUpgradeName, Configuration.CATEGORY_GENERAL, upgradeID, 100, 255, fishingUpgradeComment);
			upgradeID++;
		}
	}
}
