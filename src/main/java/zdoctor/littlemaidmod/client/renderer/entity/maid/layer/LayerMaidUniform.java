package zdoctor.littlemaidmod.client.renderer.entity.maid.layer;

import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import zdoctor.littlemaidmod.Config;
import zdoctor.littlemaidmod.api.IMaid;
import zdoctor.littlemaidmod.client.renderer.RendererServant;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class LayerMaidUniform implements LayerRenderer<EntityLivingBase> {
	private RendererServant<? extends EntityMaid> maidRender;

	public LayerMaidUniform(RendererServant<? extends EntityMaid> maidRenderIn) {
		this.maidRender = maidRenderIn;
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!entitylivingbaseIn.isInvisible() && ((IMaid) entitylivingbaseIn).hasUniform()) {
			this.maidRender.bindTexture(Config.Variables.UNIFORM_VARIANTS[((EntityMaid) entitylivingbaseIn).getUniform()]);
			this.maidRender.getMainModel().render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks,
					netHeadYaw, headPitch, scale);
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}

}
