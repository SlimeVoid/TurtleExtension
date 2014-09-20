package net.slimevoid.turtleextension.upgrades;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.ForgeDirection;
import net.slimevoid.turtleextension.core.lib.ConfigurationLib;
import net.slimevoid.turtleextension.core.lib.TurtleLib;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleUpgradeType;
import dan200.computercraft.api.turtle.TurtleVerb;

public class ShearUpgrade implements ITurtleUpgrade {

	@Override
	public int getUpgradeID() {
		return ConfigurationLib.shearUpgradeID;
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
	public ItemStack getCraftingItem() {
		return new ItemStack(Items.shears);
	}

	@Override
	public IPeripheral createPeripheral(ITurtleAccess turtle, TurtleSide side) {
		return null;
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side,
			TurtleVerb verb, int direction) {
		if ((side == TurtleSide.Left && verb == TurtleVerb.Attack)) {
			ChunkCoordinates pos = turtle.getPosition();
			AxisAlignedBB box = AxisAlignedBB.getBoundingBox(
					pos.posX - 0.5D,
					pos.posY - 0.5D,
					pos.posZ - 0.5D,
					pos.posX + 0.5D,
					pos.posY + 0.5D,
					pos.posZ + 0.5D
			);
			ForgeDirection facing = ForgeDirection.getOrientation(direction);
			World world = turtle.getWorld();
			List entities = world.getEntitiesWithinAABB(
					EntityLivingBase.class,
					box.offset(
							facing == ForgeDirection.EAST ? 1 : facing == ForgeDirection.WEST ? -1 : 0,
							0,
							facing == ForgeDirection.SOUTH ? 1 : facing == ForgeDirection.NORTH ? -1 : 0
							)
					);
			for (Object entity : entities) {
				if (entity instanceof EntityLivingBase) {
					itemInteractionForEntity(this.getCraftingItem(), (EntityLivingBase) entity);
				}
			}
			return TurtleCommandResult.success();
		}
		return TurtleCommandResult.failure();
	}

	public static boolean itemInteractionForEntity(ItemStack itemstack, EntityLivingBase entity) {
		if (entity.worldObj.isRemote) {
			return false;
		}
		if (entity instanceof IShearable) {
			IShearable target = (IShearable) entity;
			if (target.isShearable(itemstack, entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ)) {
				ArrayList<ItemStack> drops = target.onSheared(
						itemstack,
						entity.worldObj,
						(int)entity.posX,
						(int)entity.posY,
						(int)entity.posZ,
                        EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
				if (drops != null && drops.size() > 0) {
					for (ItemStack stack : drops) {
						EntityItem ent = entity.entityDropItem(stack, 0.0F);
					}
					itemstack.damageItem(1, entity);
					return true;
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
