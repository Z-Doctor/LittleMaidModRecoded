package zdoctor.littlemaidmod.client.renderer;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelBiped.ArmPose;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import zdoctor.lazylibrary.client.util.TextureLocation;
import zdoctor.littlemaidmod.MaidModMain;
import zdoctor.littlemaidmod.client.model.ModelMaid;
import zdoctor.littlemaidmod.client.model.ModelServant;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public abstract class RendererServant<T extends EntityServantBase> extends RenderLiving<T> {
	public static final ResourceLocation DEFAULT = new TextureLocation.EntityTextureLocation(MaidModMain.MODID,
			"littlemaid/mob_littlemaid");

	public RendererServant(RenderManager renderManager) {
		this(renderManager, new ModelServant(), 0.3F);
	}

	public RendererServant(RenderManager renderManager, ModelBase modelbaseIn, float shadowsizeIn) {
		super(renderManager, modelbaseIn, shadowsizeIn);
	}

	@Override
	public ModelServant getMainModel() {
		return (ModelServant) super.getMainModel();
	}

	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks) {
		ItemStack itemstack = entity.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		ArmPose armPose = ModelBiped.ArmPose.EMPTY;

		if (!itemstack.isEmpty()) {
			armPose = ModelBiped.ArmPose.ITEM;

			if (entity.getItemInUseCount() > 0) {
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.BLOCK) {
					armPose = ModelBiped.ArmPose.BLOCK;
				} else if (enumaction == EnumAction.BOW) {
					armPose = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			}
			((ModelServant) getMainModel()).rightArmPose = armPose;
		}

		((ModelServant) getMainModel()).rightArmPose = armPose;

		double d0 = y;

		if (entity.isSneaking()) {
			d0 = y - 0.125D;
		}

		this.setModelVisibilities(entity);
		super.doRender(entity, x, d0, z, entityYaw, partialTicks);
	}

	private void setModelVisibilities(EntityServantBase maid) {
		ModelServant modelMaid = this.getMainModel();

		// if (clientPlayer.isSpectator()) {
		// modelMaid.setVisible(false);
		// modelMaid.bipedHead.showModel = true;
		// modelMaid.bipedHeadwear.showModel = true;
		// } else {
		ItemStack itemstack = maid.getHeldItemMainhand();
		ItemStack itemstack1 = maid.getHeldItemOffhand();
		modelMaid.setVisible(true);
		// modelMaid.bipedHeadwear.showModel =
		// clientPlayer.isWearing(EnumPlayerModelParts.HAT);
		// modelMaid.bipedBodyWear.showModel =
		// clientPlayer.isWearing(EnumPlayerModelParts.JACKET);
		// modelMaid.bipedLeftLegwear.showModel =
		// clientPlayer.isWearing(EnumPlayerModelParts.LEFT_PANTS_LEG);
		// modelMaid.bipedRightLegwear.showModel =
		// clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_PANTS_LEG);
		// modelMaid.bipedLeftArmwear.showModel =
		// clientPlayer.isWearing(EnumPlayerModelParts.LEFT_SLEEVE);
		// modelMaid.bipedRightArmwear.showModel =
		// clientPlayer.isWearing(EnumPlayerModelParts.RIGHT_SLEEVE);
		modelMaid.isSneak = maid.isSneaking();
		ModelBiped.ArmPose modelbiped$armpose = ModelBiped.ArmPose.EMPTY;
		ModelBiped.ArmPose modelbiped$armpose1 = ModelBiped.ArmPose.EMPTY;

		if (!itemstack.isEmpty()) {
			modelbiped$armpose = ModelBiped.ArmPose.ITEM;

			if (maid.getItemInUseCount() > 0) {
				EnumAction enumaction = itemstack.getItemUseAction();

				if (enumaction == EnumAction.BLOCK) {
					modelbiped$armpose = ModelBiped.ArmPose.BLOCK;
				} else if (enumaction == EnumAction.BOW) {
					modelbiped$armpose = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			}
		}

		if (!itemstack1.isEmpty()) {
			modelbiped$armpose1 = ModelBiped.ArmPose.ITEM;

			if (maid.getItemInUseCount() > 0) {
				EnumAction enumaction1 = itemstack1.getItemUseAction();

				if (enumaction1 == EnumAction.BLOCK) {
					modelbiped$armpose1 = ModelBiped.ArmPose.BLOCK;
				}
				
				else if (enumaction1 == EnumAction.BOW) {
					modelbiped$armpose1 = ModelBiped.ArmPose.BOW_AND_ARROW;
				}
			}
		}

		if (maid.getPrimaryHand() == EnumHandSide.RIGHT) {
			modelMaid.rightArmPose = modelbiped$armpose;
			modelMaid.leftArmPose = modelbiped$armpose1;
		} else {
			modelMaid.rightArmPose = modelbiped$armpose1;
			modelMaid.leftArmPose = modelbiped$armpose;
		}
		// }
	}

	@Override
	public ResourceLocation getEntityTexture(EntityServantBase entity) {
		return null;
	}

	@Override
	public void transformHeldFull3DItemLayer() {
		GlStateManager.translate(0.0F, 0.1875F, 0.0F);
	}

	/**
	 * Allows the render to do state modifications necessary before the model is
	 * rendered.
	 */
	protected void preRenderCallback(EntityServantBase entitylivingbaseIn, float partialTickTime) {
		float f = 0.9375F;
		GlStateManager.scale(0.9375F, 0.9375F, 0.9375F);
	}

	public void renderRightArm(EntityServantBase servant) {
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelServant modelServant = this.getMainModel();
		this.setModelVisibilities(servant);
		GlStateManager.enableBlend();
		modelServant.swingProgress = 0.0F;
		modelServant.isSneak = false;
		modelServant.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, servant);
		modelServant.bipedRightArm.rotateAngleX = 0.0F;
		modelServant.bipedRightArm.render(0.0625F);
		GlStateManager.disableBlend();
	}

	public void renderLeftArm(EntityServantBase servant) {
		float f = 1.0F;
		GlStateManager.color(1.0F, 1.0F, 1.0F);
		float f1 = 0.0625F;
		ModelServant modelServant = this.getMainModel();
		this.setModelVisibilities(servant);
		GlStateManager.enableBlend();
		modelServant.isSneak = false;
		modelServant.swingProgress = 0.0F;
		modelServant.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, servant);
		modelServant.bipedLeftArm.rotateAngleX = 0.0F;
		modelServant.bipedLeftArm.render(0.0625F);
		GlStateManager.disableBlend();
	}

	@Override
	protected void applyRotations(T entityLiving, float p_77043_2_, float rotationYaw, float partialTicks) {
		if (entityLiving.isEntityAlive() && entityLiving.isPlayerSleeping()) {
			// GlStateManager.rotate(entityLiving.getBedOrientationInDegrees(), 0.0F, 1.0F,
			// 0.0F);
			// GlStateManager.rotate(this.getDeathMaxRotation(entityLiving), 0.0F, 0.0F,
			// 1.0F);
			// GlStateManager.rotate(270.0F, 0.0F, 1.0F, 0.0F);
		} else if (entityLiving.isElytraFlying()) {
			super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
			float f = (float) entityLiving.getTicksElytraFlying() + partialTicks;
			float f1 = MathHelper.clamp(f * f / 100.0F, 0.0F, 1.0F);
			GlStateManager.rotate(f1 * (-90.0F - entityLiving.rotationPitch), 1.0F, 0.0F, 0.0F);
			Vec3d vec3d = entityLiving.getLook(partialTicks);
			double d0 = entityLiving.motionX * entityLiving.motionX + entityLiving.motionZ * entityLiving.motionZ;
			double d1 = vec3d.x * vec3d.x + vec3d.z * vec3d.z;

			if (d0 > 0.0D && d1 > 0.0D) {
				double d2 = (entityLiving.motionX * vec3d.x + entityLiving.motionZ * vec3d.z)
						/ (Math.sqrt(d0) * Math.sqrt(d1));
				double d3 = entityLiving.motionX * vec3d.z - entityLiving.motionZ * vec3d.x;
				GlStateManager.rotate((float) (Math.signum(d3) * Math.acos(d2)) * 180.0F / (float) Math.PI, 0.0F, 1.0F,
						0.0F);
			}
		} else {
			super.applyRotations(entityLiving, p_77043_2_, rotationYaw, partialTicks);
		}
	}

}
