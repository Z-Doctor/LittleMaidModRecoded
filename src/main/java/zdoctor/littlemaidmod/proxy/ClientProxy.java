package zdoctor.littlemaidmod.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	// public static boolean isIntegratedServerRunning() {
	// return Minecraft.getMinecraft().isIntegratedServerRunning();
	// }
	//
	// public static World getMCtheWorld()
	// {
	// return Minecraft.getMinecraft().world;
	// }

	// public static void sendToServer(byte[] ldata) {
	// W_Network.sendPacketToServer(1, ldata);
	// }

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
//		new ModelCustomMaid();
//		FMLCommonHandler.instance().exitJava(0, true);
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}

	@Override
	public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
	}

}