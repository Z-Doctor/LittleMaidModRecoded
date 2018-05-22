package zdoctor.littlemaidmod.proxy;

import java.util.Random;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zdoctor.littlemaidmod.Config;
import zdoctor.littlemaidmod.MaidModMain;
import zdoctor.littlemaidmod.client.gui.handler.MaidGuiHandler;
import zdoctor.littlemaidmod.init.LMMEntities;
import zdoctor.littlemaidmod.init.LMMEvents;
import zdoctor.littlemaidmod.init.LMMItems;

public class CommonProxy {

	public static Random randomSoundChance;

	public void preInit(FMLPreInitializationEvent e) {
		LMMEntities.init();
		LMMItems.init();
		new Config(e);

	}

	public void init(FMLInitializationEvent e) {
		NetworkRegistry.INSTANCE.registerGuiHandler(MaidModMain.INSTANCE, new MaidGuiHandler());
		MinecraftForge.EVENT_BUS.register(new LMMEvents());
	}

	//
	public void postInit(FMLPostInitializationEvent e) {
		// List<IResourcePack> defaultResourcePacks =
		// ObfuscationReflectionHelper.getPrivateValue(Minecraft.class,
		// Minecraft.getMinecraft(), "defaultResourcePacks", "field_110449_ao");
		// defaultResourcePacks.add(new LMM_SoundResourcePack());
		// defaultResourcePacks.add(new LMMNX_OldZipTexturesLoader());
		//
		//
		// BiomeGenBase[] biomeList = null;
		// if (cfg_spawnWeight > 0) {
		// if (cfg_Dominant) {
		// biomeList = BiomeGenBase.getBiomeGenArray();
		// } else {
		// biomeList = new BiomeGenBase[] { BiomeGenBase.desert, BiomeGenBase.plains,
		// BiomeGenBase.savanna,
		// BiomeGenBase.mushroomIsland, BiomeGenBase.forest, BiomeGenBase.birchForest,
		// BiomeGenBase.swampland, BiomeGenBase.taiga, BiomeGenBase.icePlains };
		// }
		// for (BiomeGenBase biome : biomeList) {
		// if (biome != null) {
		// EntityRegistry.addSpawn(LMM_EntityLittleMaid.class, cfg_spawnWeight,
		// cfg_minGroupSize,
		// cfg_maxGroupSize, EnumCreatureType.CREATURE, biome);
		// }
		// }
		// }
		//
		// LMM_IFF.loadIFFs();
	}

}