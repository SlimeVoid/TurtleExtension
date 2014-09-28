package net.slimevoid.turtleextension.upgrades;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.IShearable;
import net.slimevoid.turtleextension.core.lib.TurtleLib;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;

public class FishingUpgrade extends TurtleUpgradeBase {
	
	private boolean isFishing = false;
	private long timeFishing;

	public FishingUpgrade(int upgradeID, ItemStack fishStack) {
		super(upgradeID, fishStack);
	}

	@Override
	public String getUnlocalisedAdjective() {
		return TurtleLib.FISH_UPGRADE;
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}
	
	protected boolean isWaterAround() {
		return this.getWaterBlocks(1, 1, 1) > 0;
	}

	protected int getWaterBlocks(int offsetX, int offsetY, int offsetZ) {
		int waterBlocks = 0;
		for(int x = this.pos.posX-offsetX; x <= this.pos.posX+offsetX; x++) {
			for(int y = this.pos.posY-offsetY; y <= this.pos.posY+offsetY; y++) {
				for(int z = this.pos.posZ-offsetZ; z <= this.pos.posZ+offsetZ; z++) {
					waterBlocks += this.isWaterBlock(x, y, z) ? 1 : 0;
				}
			}
		}
		return waterBlocks;
	}

	
	protected boolean isWaterBlock(int x, int y, int z) {
		Block block = this.getBlock();
		if(block.getMaterial().isLiquid() && block.isAssociatedBlock(Blocks.water)) {
			return true;
		}
		return false;
	}

	private boolean catchFish(double fish, double time) {
		double probability = time - 0.3 + fish;
		double target = Math.random();
		if (target <= probability) {
			this.storeItemStack(new ItemStack(Items.fish));
			return true;
		}
		return false;
	}

	@Override
	protected boolean turtleDig() {
		if (!this.isFishing) {
			return false;
		}
		
		this.isFishing = false;
		
		if (!this.isWaterAround()) {
			return false;
		}
		
		int waterBlocks = this.getWaterBlocks(4, 4, 4);
		
		long currentTime = this.world.getTotalWorldTime();
		long passedTime = currentTime - this.timeFishing;
		this.timeFishing = 0;
		if (passedTime == 0) {
			return false;
		}
		
		double fishPercentage = waterBlocks * (0.3 / (9 * 9 * 9 - 1));
		double timePercentage = Math.log(passedTime / 40) / 3;
		
		if (this.catchFish(fishPercentage, timePercentage)) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean turtleAttack() {
		
		if (!this.isWaterAround()) {
			return false;
		}
		
		this.isFishing = true;
		this.timeFishing = this.world.getTotalWorldTime();
		
		return true;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Items.shears.getIconFromDamage(0);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
	}

}
