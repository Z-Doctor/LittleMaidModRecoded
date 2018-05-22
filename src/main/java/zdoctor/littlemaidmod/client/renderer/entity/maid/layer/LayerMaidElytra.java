package zdoctor.littlemaidmod.client.renderer.entity.maid.layer;

import net.minecraft.client.model.ModelElytra;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.client.renderer.entity.layers.LayerElytra;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class LayerMaidElytra extends LayerElytra {
	public static final ResourceLocation TEXTURE_ELYTRA = new ResourceLocation("textures/entity/elytra.png");
	protected final ModelElytra modelElytra = new ModelElytra();

	public LayerMaidElytra(RenderLivingBase<?> p_i47185_1_) {
		super(p_i47185_1_);
	}

	@Override
	public void doRenderLayer(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		ItemStack itemstack = entitylivingbaseIn.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

		if (itemstack.getItem() == Items.ELYTRA) {
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			GlStateManager.enableBlend();
			GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);

			this.renderPlayer.bindTexture(TEXTURE_ELYTRA);

			GlStateManager.pushMatrix();
			GlStateManager.translate(0.0F, 0.5F, 0.125F);
			GlStateManager.scale(0.75D, 0.75D, 0.75D);
			this.modelElytra.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale,
					entitylivingbaseIn);
			this.modelElytra.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch,
					scale);

			if (itemstack.isItemEnchanted()) {
				LayerArmorBase.renderEnchantedGlint(this.renderPlayer, entitylivingbaseIn, this.modelElytra, limbSwing,
						limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
			}

			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
		}
	}

}
