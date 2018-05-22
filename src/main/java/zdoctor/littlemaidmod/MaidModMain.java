package zdoctor.littlemaidmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.littlemaidmod.proxy.CommonProxy;

@Mod(modid = MaidModMain.MODID, version = MaidModMain.VERSION, useMetadata = true, dependencies = "required-after:forge@[14.23.3.2655,);required-after:lazylibrary@[1.0.1.0,)")
public class MaidModMain {

	public static final String MODID = "lmmr";
	public static final String VERSION = "1.0.0.0";
	
	@SidedProxy(clientSide = "zdoctor.littlemaidmod.proxy.ClientProxy", serverSide = "zdoctor.littlemaidmod.proxy.ServerProxy")
	public static CommonProxy proxy;

	@Instance
	public static MaidModMain INSTANCE = new MaidModMain();

	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
//		new ModelCreator(e);
//		FMLCommonHandler.instance().exitJava(0, true);
		proxy.preInit(e);
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
	}

}
