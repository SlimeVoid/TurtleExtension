package net.slimevoid.turtleextension.client.proxy;

import java.io.File;

import net.slimevoid.turtleextension.core.lib.ConfigurationLib;
import net.slimevoid.turtleextension.proxy.CommonProxy;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerConfigurationProperties(File configFile) {
		super.registerConfigurationProperties(configFile);
		ConfigurationLib.ClientConfig();
	}

}
