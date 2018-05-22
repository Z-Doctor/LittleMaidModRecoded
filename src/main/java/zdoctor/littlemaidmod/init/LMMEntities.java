package zdoctor.littlemaidmod.init;

import net.minecraft.client.renderer.entity.RenderLivingBase;
import zdoctor.lazylibrary.common.entity.EasyEntityTracking;
import zdoctor.lazylibrary.common.entity.EasyLivingEntity;
import zdoctor.littlemaidmod.MaidModMain;
import zdoctor.littlemaidmod.client.renderer.RenderMaid;
import zdoctor.littlemaidmod.entity.EntityCustomXP;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class LMMEntities {
	public static void init() {
	};

	static {
		new EasyLivingEntity("Maid", EntityMaid.class) {
			@Override
			public Class<? extends RenderLivingBase> getRendererClass() {
				return RenderMaid.class;
			}
		}.setEggColors(0xff0000, 0xff5500);

		new EasyEntityTracking("customorb", EntityCustomXP.class, MaidModMain.MODID, 160, 20, true);
	}
}
