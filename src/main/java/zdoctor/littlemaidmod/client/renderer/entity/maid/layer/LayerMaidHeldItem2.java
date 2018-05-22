package zdoctor.littlemaidmod.client.renderer.entity.maid.layer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumHandSide;
import zdoctor.littlemaidmod.client.model.ModelServant;
import zdoctor.littlemaidmod.client.renderer.RendererServant;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class LayerMaidHeldItem2 implements LayerRenderer<EntityMaid> {
	private RendererServant maidRenderer;

	public LayerMaidHeldItem2(RendererServant maidRendererIn) {
		this.maidRenderer = maidRendererIn;
	}

	@Override
	public void doRenderLayer(EntityMaid entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks,
			float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack itemstack = entitylivingbaseIn.getHeldItemMainhand();

		// When waiting the item doesn't show. Not because I couldn't figure out how to
		// render them when waiting ;) it makes sense though
		if (!itemstack.isEmpty() && !entitylivingbaseIn.isWaiting()) {
			GlStateManager.color(1.0F, 1.0F, 1.0F);
			GlStateManager.pushMatrix();

			if (this.maidRenderer.getMainModel().isChild) {
				GlStateManager.translate(0.0F, 0.625F, 0.0F);
				GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
				float f = 0.5F;
				GlStateManager.scale(0.5F, 0.5F, 0.5F);
			}

			GlStateManager.translate(-0.0625F, 0.53125F, 0.21875F);
			Item item = itemstack.getItem();
			Minecraft minecraft = Minecraft.getMinecraft();
//			System.out.println("Tick: " + entitylivingbaseIn.isHandActive());
			if (Block.getBlockFromItem(item).getDefaultState()
					.getRenderType() == EnumBlockRenderType.ENTITYBLOCK_ANIMATED) {
				((ModelServant) maidRenderer.getMainModel()).postRenderArm(0.0625F, EnumHandSide.RIGHT);
				this.maidRenderer.transformHeldFull3DItemLayer();
//				GlStateManager.translate(0.0625F, -0.125F, 0.0F);
				GlStateManager.scale(0.625F, -0.625F, 0.625F);
				GlStateManager.rotate(90.0F, 0.0F, 1.0F, 0.0F);
			} else if (item == Items.BOW && entitylivingbaseIn.isHandActive()) {
				((ModelServant) maidRenderer.getMainModel()).postRenderArm(0.0625F, EnumHandSide.RIGHT);
//				GlStateManager.translate(-0.05F, 0.45F, -0.3F);
//				GlStateManager.translate(-0.05F, 0.64F, -0.1F);
				GlStateManager.rotate(10.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.scale(0.675F, 0.675F, 0.675F);
			} else if (item.isFull3D()) {
				((ModelServant) maidRenderer.getMainModel()).postRenderArm(0.0625F, EnumHandSide.RIGHT);
				this.maidRenderer.transformHeldFull3DItemLayer();
				GlStateManager.translate(0.0625F, -0.125F, 0.0F);
				GlStateManager.scale(0.625F, -0.625F, 0.625F);
			} else {
				((ModelServant) maidRenderer.getMainModel()).postRenderArm(0.0625F, EnumHandSide.RIGHT);
				GlStateManager.translate(0.0F, 0.475F, -0.275F);
				GlStateManager.rotate(10.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.scale(0.775F, 0.775F, 0.775F);
			}

			GlStateManager.rotate(-80.0F, 1.0F, 0.0F, 0.0F);

			minecraft.getItemRenderer().renderItem(entitylivingbaseIn, itemstack,
					ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND);
			GlStateManager.popMatrix();
		}
	}

	@Override
	public boolean shouldCombineTextures() {
		return false;
	};

}
