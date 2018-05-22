package zdoctor.littlemaidmod.proxy.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import zdoctor.littlemaidmod.MaidModMain;

public class MaidNetworkHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(MaidModMain.MODID);

	public static void init() {

	};

	{
	}

}
