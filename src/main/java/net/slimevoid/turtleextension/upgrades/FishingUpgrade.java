package net.slimevoid.turtleextension.upgrades;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
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
		int waterBlocks = 0;
		waterBlocks += this.isWaterBlock(this.pos.posX + 1, this.pos.posY - 1, this.pos.posZ) ? 1 : 0;
		waterBlocks += this.isWaterBlock(this.pos.posX - 1, this.pos.posY - 1, this.pos.posZ) ? 1 : 0;
		waterBlocks += this.isWaterBlock(this.pos.posX, this.pos.posY - 1, this.pos.posZ + 1) ? 1 : 0;
		waterBlocks += this.isWaterBlock(this.pos.posX, this.pos.posY - 1, this.pos.posZ - 1) ? 1 : 0;
		return waterBlocks > 0;
	}

	protected int getWaterBlocks(int offsetX, int offsetY, int offsetZ) {
		int waterBlocks = 0;
		for(int x = this.pos.posX - offsetX; x <= this.pos.posX + offsetX; x++) {
			for(int z = this.pos.posZ - offsetZ; z <= this.pos.posZ + offsetZ; z++) {
				waterBlocks += this.isWaterBlock(x, this.pos.posY - 1, z) ? 1 : 0;
			}
		}
		return waterBlocks;
	}

	
	protected boolean isWaterBlock(int x, int y, int z) {
		Block block = this.getBlock(x, y, z);
		return (block.isAssociatedBlock(Blocks.water) || block.isAssociatedBlock(Blocks.flowing_water));
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
		
		int waterBlocks = this.getWaterBlocks(4, 0, 4);
		
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
	public void update(ITurtleAccess turtle, TurtleSide side) {
	}

}
