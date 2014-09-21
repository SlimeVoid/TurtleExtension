package net.slimevoid.turtleextension.upgrades;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
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

public class ShearUpgrade extends TurtleUpgradeBase {

	public ShearUpgrade(int upgradeID, ItemStack shearStack) {
		super(upgradeID, shearStack);
	}

	@Override
	public String getUnlocalisedAdjective() {
		return TurtleLib.SHEAR_UPGRADE;
	}

	@Override
	public TurtleUpgradeType getType() {
		return TurtleUpgradeType.Tool;
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}
	
	@Override
	protected boolean turtleDig() {
		Block block = this.world.getBlock(this.offsetX, this.offsetY, this.offsetZ);
		int metadata = this.world.getBlockMetadata(this.offsetX, this.offsetY, this.offsetZ);
		if (!this.world.isAirBlock(this.offsetX, this.offsetY, this.offsetZ)) {
			if (this.getCraftingItem()./*getStrVsBlock*/func_150997_a(block) > 1.0F) {
				if (this.canHarvestBlock(block, metadata)) {
					ArrayList<ItemStack> drops = null;
					if (block instanceof IShearable) {
						drops = ((IShearable) block).onSheared(
								this.getCraftingItem(),
								this.world,
								this.offsetX,
								this.offsetY,
								this.offsetZ,
								0
						);
					} else {
						drops = block.getDrops(
								this.world,
								this.offsetX,
								this.offsetY,
								this.offsetZ,
								metadata, 0
						);
					}
					
					this.world./*setBlockToAir*/func_147480_a(this.offsetX, this.offsetY, this.offsetZ, false);
					
					for (ItemStack drop : drops) {
						this.storeItemStack(drop);
					}
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	protected boolean turtleAttack() {
		AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
				this.offsetX,
				this.offsetY,
				this.offsetZ,
				this.offsetX + 1.0D,
				this.offsetY + 1.0D,
				this.offsetZ + 1.0D);
		
		List entities = this.world.getEntitiesWithinAABBExcludingEntity(this.fakePlayer, box);
		
		if (entities == null) {
			return false;
		}
		
		for (Object object : entities) {
			if (object != null && object instanceof Entity) {
				Entity entity = (Entity) object;
				ItemStack shears = this.getCraftingItem();
				if (entity instanceof IShearable) {
					boolean canShear = ((IShearable) entity).isShearable(
							shears,
							this.world,
							(int) entity.posX,
							(int) entity.posY,
							(int) entity.posZ
					);
					ArrayList<ItemStack> drops = null;
					if (canShear) {
						drops = ((IShearable) entity).onSheared(
								shears,
								this.world,
								(int) entity.posX,
								(int) entity.posY,
								(int) entity.posZ,
								0
						);
						if (drops != null) {
							for (ItemStack drop : drops) {
								this.storeItemStack(drop);
							}
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return Items.shears.getIconFromDamage(0);
	}

	@Override
	public void update(ITurtleAccess turtle, TurtleSide side) {
	}

}
