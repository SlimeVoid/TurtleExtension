package net.slimevoid.turtleextension.upgrades;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.slimevoid.library.util.helpers.ItemHelper;

import com.mojang.authlib.GameProfile;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.TurtleVerb;

public abstract class TurtleUpgradeBase implements ITurtleUpgrade {
	
	private final int upgradeID;
	private final ItemStack craftingItem;
	
	public ITurtleAccess turtle;
	public World world;
	public ChunkCoordinates pos;
	public int direction;
	public int offsetX;
	public int offsetY;
	public int offsetZ;
	public EntityPlayer fakePlayer;
	
	public TurtleUpgradeBase(int upgradeID, ItemStack craftingItem) {
		this.upgradeID = upgradeID;
		this.craftingItem = craftingItem;
	}

	@Override
	public int getUpgradeID() {
		return this.upgradeID;
	}

	@Override
	public ItemStack getCraftingItem() {
		return this.craftingItem;
	}
	
	protected Block getFacingBlock() {
		return this.getBlock(this.offsetX, this.offsetY, this.offsetZ);
	}

	protected Block getBlock(int x, int y, int z) {
		return this.world.getBlock(x, y, z);
	}
	
	protected int getFacingBlockMetadata() {
		return this.getBlockMetadata(this.offsetX, this.offsetY, this.offsetZ);
	}
	
	protected int getBlockMetadata(int x, int y, int z) {
		return this.world.getBlockMetadata(x, y, z);
	}
	
	protected boolean isAirBlock() {
		return this.world.isAirBlock(this.offsetX, this.offsetY, this.offsetZ);
	}
	
	protected AxisAlignedBB getBoundingBox(float offsetX, float offsetY, float offsetZ) {
		return AxisAlignedBB.getBoundingBox(
					this.offsetX,
					this.offsetY,
					this.offsetZ,
					this.offsetX + 1.0D,
					this.offsetY + 1.0D,
					this.offsetZ + 1.0D).offset(offsetX, offsetY, offsetZ);
	}
	
	public void setTurtle(ITurtleAccess turtle, int direction) {
		this.turtle = turtle;
		this.world = turtle.getWorld();
		this.pos = turtle.getPosition();
		this.direction = direction;
		this.offsetX = pos.posX + Facing.offsetsXForSide[direction];
		this.offsetY = pos.posY + Facing.offsetsYForSide[direction];
		this.offsetZ = pos.posZ + Facing.offsetsZForSide[direction];
		if (!this.world.isRemote) {
			WorldServer server = DimensionManager.getWorld(this.world.provider.dimensionId);
			this.fakePlayer = new FakePlayer(server, new GameProfile(null, "turtlePlayer"));
		}
	}

	@Override
	public TurtleCommandResult useTool(ITurtleAccess turtle, TurtleSide side,
			TurtleVerb verb, int direction) {
		this.setTurtle(turtle, direction);
		boolean result = false;
		switch(verb) {
		case Dig :
			result = this.turtleDig();
			break;
		case Attack :
			result = this.turtleAttack();
			break;
		}
		return result ? TurtleCommandResult.success() : TurtleCommandResult.failure();
	}

	@Override
	public IIcon getIcon(ITurtleAccess turtle, TurtleSide side) {
		return this.getCraftingItem().getIconIndex();
	}

	protected abstract boolean turtleDig();

	protected abstract boolean turtleAttack();
	
	public boolean canHarvestBlock(Block block, int metadata) {
		this.fakePlayer.inventory.clearInventory(null, -1);
		this.fakePlayer.inventory.setInventorySlotContents(0, this.getCraftingItem());
		this.fakePlayer.inventory.currentItem = 0;
		return ForgeHooks.canHarvestBlock(block, this.fakePlayer, metadata);
	}
	
	public void storeItemStack(ItemStack itemstack) {
		if (!this.putItemStack(this.turtle.getInventory(), this.turtle.getSelectedSlot(), itemstack)) {
			ItemHelper.dropItem(this.world, this.pos.posX, this.pos.posY, this.pos.posZ, itemstack);
		}
	}
	
	public boolean putItemStack(IInventory inventory, int currentSlot, ItemStack stack) {
		int slotSize = inventory.getSizeInventory();
		int i = currentSlot;
		do {
			ItemStack currentStack = inventory.getStackInSlot(i);			
			if(currentStack == null) {
				inventory.setInventorySlotContents(i, stack.copy());
				stack = null;
				return true;
			}
			else if(currentStack.isStackable() && stack.isItemEqual(currentStack)) {
				int space = currentStack.getMaxStackSize() - currentStack.stackSize;
				if(space == 0) {
					continue;
				}
				if(space >= stack.stackSize) {
					currentStack.stackSize += stack.stackSize;
					stack = null;
					return true;
				}
				else {
					currentStack.stackSize = currentStack.getMaxStackSize();
					stack.stackSize -= space;
					continue;
				}
			}
			
			i = (i+1) % slotSize;
		} while(i != currentSlot);
		return false;
	}
}
