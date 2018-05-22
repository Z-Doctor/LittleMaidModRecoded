package zdoctor.littlemaidmod.client.renderer;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import zdoctor.littlemaidmod.client.model.ModelMaid;
import zdoctor.littlemaidmod.client.renderer.entity.maid.layer.LayerMaidElytra;
import zdoctor.littlemaidmod.client.renderer.entity.maid.layer.LayerMaidEyes;
import zdoctor.littlemaidmod.client.renderer.entity.maid.layer.LayerMaidHair;
import zdoctor.littlemaidmod.client.renderer.entity.maid.layer.LayerMaidHeldItem;
import zdoctor.littlemaidmod.client.renderer.entity.maid.layer.LayerMaidSkin;
import zdoctor.littlemaidmod.client.renderer.entity.maid.layer.LayerMaidUndergarments;
import zdoctor.littlemaidmod.client.renderer.entity.maid.layer.LayerMaidUniform;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class RenderMaid<T extends EntityMaid> extends RendererServant<T> {

	public RenderMaid(RenderManager rendermanagerIn) {
		super(rendermanagerIn, new ModelMaid(), 0.3f);
		System.out.println("New Model");
		this.addLayer(new LayerMaidSkin(this));
		this.addLayer(new LayerMaidEyes(this));
		this.addLayer(new LayerMaidHair(this));
		this.addLayer(new LayerMaidUndergarments(this));
		this.addLayer(new LayerMaidUniform(this));

		this.addLayer(new LayerMaidHeldItem(this));
		this.addLayer(new LayerArrow(this));
		this.addLayer(new LayerMaidElytra(this));
	}

	@Override
	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.4F, -0.275F);
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}

}