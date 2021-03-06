package zdoctor.littlemaidmod.client.renderer.entity.maid.layer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import zdoctor.littlemaidmod.Config;
import zdoctor.littlemaidmod.api.IMaid;
import zdoctor.littlemaidmod.client.renderer.RendererServant;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class LayerMaidHair implements LayerRenderer<EntityLivingBase> {
	private RendererServant<? extends EntityMaid> maidRender;

	public LayerMaidHair(RendererServant<? extends EntityMaid> maidRenderIn) {
		this.maidRender = maidRenderIn;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!entitylivingbaseIn.isInvisible()) {
			this.maidRender.bindTexture(Config.Variables.HAIR_VARIANTS[((EntityMaid) entitylivingbaseIn).getHairStyle()]);
			float[] afloat = ((IMaid) entitylivingbaseIn).getHairColor().getColorComponentValues();
			GlStateManager.color(afloat[0], afloat[1], afloat[2]);
			this.maidRender.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks,
					netHeadYaw, headPitch, scale);
			GlStateManager.color(1F, 1F, 1F);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

}
