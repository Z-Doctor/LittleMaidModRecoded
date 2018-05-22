package zdoctor.littlemaidmod.client.renderer.entity.maid.layer;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import zdoctor.littlemaidmod.Config;
import zdoctor.littlemaidmod.client.renderer.RendererServant;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class LayerMaidSkin implements LayerRenderer<EntityLivingBase> {
	private RendererServant<? extends EntityMaid> maidRender;

	public LayerMaidSkin(RendererServant<? extends EntityMaid> maidRenderIn) {
		this.maidRender = maidRenderIn;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!entitylivingbaseIn.isInvisible()) {
			this.maidRender.bindTexture(Config.Variables.SKIN_VARIANTS[((EntityMaid) entitylivingbaseIn).getSkinTone()]);
			this.maidRender.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks,
					netHeadYaw, headPitch, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

}
