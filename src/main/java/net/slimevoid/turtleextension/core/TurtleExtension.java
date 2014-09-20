package net.slimevoid.turtleextension.core;

import net.slimevoid.library.ICommonProxy;
import net.slimevoid.turtleextension.core.lib.CoreLib;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(
        modid = CoreLib.MOD_ID,
        name = CoreLib.MOD_NAME,
        version = CoreLib.MOD_VERSION,
        dependencies = CoreLib.MOD_DEPENDENCIES)
public class TurtleExtension {
	
	@Instance(CoreLib.MOD_ID)
	public static TurtleExtension instance;
	
	@SidedProxy(
			clientSide = CoreLib.MOD_CLIENT_PROXY,
			serverSide = CoreLib.MOD_COMMON_PROXY
	)
	public static ICommonProxy proxy;
	
	@EventHandler
	public void PreInit(FMLPreInitializationEvent event) {
        proxy.registerConfigurationProperties(event.getSuggestedConfigurationFile());
		proxy.preInit();
	}
	
	@EventHandler
	public void Init(FMLInitializationEvent event) {
		proxy.init();
	}
	
	@EventHandler
	public void PostInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}
