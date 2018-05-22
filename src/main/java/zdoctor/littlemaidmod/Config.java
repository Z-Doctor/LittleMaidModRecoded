package zdoctor.littlemaidmod;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import zdoctor.lazylibrary.common.config.EasyConfigGui;
import zdoctor.lazylibrary.common.util.TextHelper;

public class Config extends EasyConfigGui {
	protected static Config CONFIG;
	
	public Config() {
	}

	public Config(FMLPreInitializationEvent e) {
		super(e, "Default");
		CONFIG = this;
		new Options();
	}

	@Override
	public String getTitle() {
		return TextHelper.translateToLocal("lmmr.config");
	}
	
	public static class Options {
		protected static final String GAMEPLAY = "gameplay";
		public Options() {
			CONFIG.getConfig().addCustomCategoryComment(GAMEPLAY, GAMEPLAY + ".comment");
		}
		public static final BooleanProperty SPAWN_WITH_UNIFORM = new BooleanProperty(CONFIG ,GAMEPLAY, "config.spawn.maid.uniform", false);
		public static final BooleanProperty ADULT_MODE = new BooleanProperty(CONFIG ,GAMEPLAY, "config.adult.mode", false);
		
		public static int cfg_spawnWeight = 5;
		public static int cfg_spawnLimit = 20;
		public static int cfg_minGroupSize = 1;
		public static int cfg_maxGroupSize = 3;
		public static boolean cfg_canDespawn = false;
		public static boolean cfg_checkOwnerName = false;
		public static boolean cfg_antiDoppelganger = true;
		public static boolean cfg_enableSpawnEgg = true;

		public static boolean cfg_VoiceDistortion = false;

		public static boolean cfg_PrintDebugMessage = false;
		public static boolean cfg_DeathMessage = true;
		public static boolean cfg_Dominant = false;
		public static boolean cfg_isModelAlphaBlend = false;
		public static boolean cfg_isFixedWildMaid = false;

		public static float cfg_voiceRate = 0.1f;
		public static boolean cfg_Aggressive = true;
		public static int cfg_maidOverdriveDelay = 64;
	}
	
	public static class Variables {
		public static final ResourceLocation MAID = new ResourceLocation(MaidModMain.MODID, "textures/entity/maid");


		public static final String PREFIX_SKIN = MAID.toString() + "/skins/sk_";
		public static final ResourceLocation[] SKIN_VARIANTS = new ResourceLocation[] {
				new ResourceLocation(PREFIX_SKIN + "0.png"), new ResourceLocation(PREFIX_SKIN + "1.png"),
				new ResourceLocation(PREFIX_SKIN + "2.png") };

		public static final String PREFIX_UG = MAID.toString() + "/undergarments/ug_";
		public static final ResourceLocation[] UG_VARIANTS = new ResourceLocation[] {
				new ResourceLocation(PREFIX_UG + "0.png") };

		public static final String PREFIX_HAIR = MAID.toString() + "/hairstyles/hs_";
		public static final ResourceLocation[] HAIR_VARIANTS = new ResourceLocation[] {
				new ResourceLocation(PREFIX_HAIR + "0.png"), new ResourceLocation(PREFIX_HAIR + "1.png"),
				new ResourceLocation(PREFIX_HAIR + "2.png"), new ResourceLocation(PREFIX_HAIR + "3.png"),
				new ResourceLocation(PREFIX_HAIR + "4.png"), new ResourceLocation(PREFIX_HAIR + "5.png"),
				new ResourceLocation(PREFIX_HAIR + "6.png"), new ResourceLocation(PREFIX_HAIR + "7.png") };

		public static final String PREFIX_EYE = MAID.toString() + "/eyes/ey_";
		public static final String SUBFIX_PUPIL = "_pupil_";
		public static final String SUBFIX_IRIS = "_iris_";
		public static final ResourceLocation[] EYE_VARIANTS = new ResourceLocation[] {
				new ResourceLocation(PREFIX_EYE + "0.png") };
		public static final ResourceLocation[] PUPIL_VARIANTS = new ResourceLocation[] {
				new ResourceLocation(PREFIX_EYE + "0" + SUBFIX_PUPIL + "0.png") };
		public static final ResourceLocation[] IRIS_VARIANTS = new ResourceLocation[] {
				new ResourceLocation(PREFIX_EYE + "0" + SUBFIX_IRIS + "0.png") };

		public static final String PREFIX_UNIFORM = MAID.toString() + "/uniform/uni_";
		public static final ResourceLocation[] UNIFORM_VARIANTS = new ResourceLocation[] {
				new ResourceLocation(PREFIX_UNIFORM + "0.png") };
	}
	
}
