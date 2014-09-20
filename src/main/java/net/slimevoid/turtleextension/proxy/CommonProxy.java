package net.slimevoid.turtleextension.proxy;

import java.io.File;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.slimevoid.library.ICommonProxy;
import net.slimevoid.turtleextension.core.lib.ConfigurationLib;

public class CommonProxy implements ICommonProxy {

	@Override
	public Object getClientGuiElement(int arg0, EntityPlayer arg1, World arg2,
			int arg3, int arg4, int arg5) {
		return null;
	}

	@Override
	public String getMinecraftDir() {
		return null;
	}

	@Override
	public Object getServerGuiElement(int arg0, EntityPlayer arg1, World arg2,
			int arg3, int arg4, int arg5) {
		return null;
	}

	@Override
	public void init() {
	}

	@Override
	public boolean isClient(World arg0) {
		return false;
	}

	@Override
	public void postInit() {
	}

	@Override
	public void preInit() {
	}

	@Override
	public void registerConfigurationProperties(File configFile) {
		ConfigurationLib.CreateConfig(configFile);
		ConfigurationLib.CommonConfig();
	}

	@Override
	public void registerEventHandlers() {
	}

	@Override
	public void registerRenderInformation() {
	}

	@Override
	public void registerTickHandlers() {
	}

	@Override
	public void registerTileEntitySpecialRenderer(
			Class<? extends TileEntity> arg0) {
	}

}
