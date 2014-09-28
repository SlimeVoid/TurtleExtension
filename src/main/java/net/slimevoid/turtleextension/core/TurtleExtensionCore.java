package net.slimevoid.turtleextension.core;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.slimevoid.turtleextension.core.lib.ConfigurationLib;
import net.slimevoid.turtleextension.upgrades.FishingUpgrade;
import net.slimevoid.turtleextension.upgrades.ShearingUpgrade;
import dan200.computercraft.api.ComputerCraftAPI;

public class TurtleExtensionCore {

	public static void registerUpgrades() {
		registerShearingUpgrades();
		registerFishingUpgrade();
	}

	private static void registerShearingUpgrades() {
		if (ConfigurationLib.shearUpgradesEnabled) {
			for (String shearUpgrade : ConfigurationLib.shearUpgrades) {
				String[] string = shearUpgrade.split("-");
				int shearUpgradeID = Integer.parseInt(string[0]);
				String unlocalizedName = string[1];
				if (Item.itemRegistry.containsKey(unlocalizedName)) {
					Object object = Item.itemRegistry.getObject(unlocalizedName);
					if (object != null && object instanceof ItemShears) {
						ItemStack shears = new ItemStack((Item) object);
						ComputerCraftAPI.registerTurtleUpgrade(new ShearingUpgrade(shearUpgradeID, shears));
					}
				}
			}
		}
	}
	
	private static void registerFishingUpgrade() {
		ItemStack fishingRod = new ItemStack(Items.fishing_rod);
		ComputerCraftAPI.registerTurtleUpgrade(new FishingUpgrade(ConfigurationLib.fishingUpgradeID, fishingRod));
	}

}
