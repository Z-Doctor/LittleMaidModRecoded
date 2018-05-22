package zdoctor.littlemaidmod.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ModelServant extends ModelBase {

	public ModelRenderer bipedHead;
	public ModelRenderer bipedBody;
	public ModelRenderer bipedLeftArm;
	public ModelRenderer bipedRightArm;
	public ModelRenderer bipedLeftLeg;
	public ModelRenderer bipedRightLeg;

	public ModelBiped.ArmPose leftArmPose;
	public ModelBiped.ArmPose rightArmPose;

	public boolean isSneak;

	public ModelServant() {
		this(0.0F);
	}

	public ModelServant(float modelSize) {
		this(modelSize, 64, 32);
	}

	public ModelServant(float modelSize, int textureWidthIn, int textureHeightIn) {
		this.textureWidth = textureWidthIn;
		this.textureHeight = textureHeightIn;

		this.leftArmPose = ModelBiped.ArmPose.EMPTY;
		this.rightArmPose = ModelBiped.ArmPose.EMPTY;

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(-4, -8, -4, 8, 8, 8, modelSize);
		this.bipedHead.setRotationPoint(0F, 0F, 0F);

		this.bipedBody = new ModelRenderer(this, 32, 8);
		this.bipedBody.addBox(-3F, 0F, -2F, 6, 7, 4, modelSize);
		this.bipedBody.setRotationPoint(0, 0, 0);

		this.bipedRightArm = new ModelRenderer(this, 48, 0);
		this.bipedRightArm.addBox(-2.0F, -1F, -1F, 2, 8, 2, modelSize);
		this.bipedRightArm.setRotationPoint(-3.0F, 1.5F, 0F);

		this.bipedLeftArm = new ModelRenderer(this, 56, 0);
		this.bipedLeftArm.addBox(0.0F, -1F, -1F, 2, 8, 2, modelSize);
		this.bipedLeftArm.setRotationPoint(3.0F, 1.5F, 0F);

		this.bipedRightLeg = new ModelRenderer(this, 32, 19);
		this.bipedRightLeg.addBox(-1F, 0F, -2F, 3, 9, 4, modelSize);
		this.bipedRightLeg.setRotationPoint(-1.9F, 7F, 0F);

		this.bipedLeftLeg = new ModelRenderer(this, 32, 19);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-2F, 0F, -2F, 3, 9, 4, modelSize);
		this.bipedLeftLeg.setRotationPoint(1.9F, 7F, 0F);

	}

	@Override
	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scale) {
		GlStateManager.pushMatrix();

		GlStateManager.translate(0, 0.5, 0);
		if (isSneak) {
			GlStateManager.translate(0.0F, 0.2F, 0.0F);
		}

		bipedHead.render(scale);
		bipedBody.render(scale);
		bipedLeftArm.render(scale);
		bipedRightArm.render(scale);
		bipedLeftLeg.render(scale);
		bipedRightLeg.render(scale);

		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
			float headPitch, float scaleFactor, Entity entityIn) {
		if (!(entityIn instanceof EntityServantBase))
			return;
		EntityServantBase servant = (EntityServantBase) entityIn;

		boolean flag = entityIn instanceof EntityLivingBase && ((EntityLivingBase) entityIn).getTicksElytraFlying() > 4;
		this.bipedHead.rotateAngleY = netHeadYaw * 0.017453292F;

		if (flag) {
			this.bipedHead.rotateAngleX = -((float) Math.PI / 4F);
		} else {
			this.bipedHead.rotateAngleX = headPitch * 0.017453292F;
		}

		this.bipedBody.rotateAngleY = 0.0F;
		this.bipedRightArm.rotationPointZ = 0.0F;
		this.bipedRightArm.rotationPointX = -3.0F;
		this.bipedLeftArm.rotationPointZ = 0.0F;
		this.bipedLeftArm.rotationPointX = 3.0F;
		float f = 1.0F;

		if (flag) {
			f = (float) (entityIn.motionX * entityIn.motionX + entityIn.motionY * entityIn.motionY
					+ entityIn.motionZ * entityIn.motionZ);
			f = f / 0.2F;
			f = f * f * f;
		}

		if (f < 1.0F) {
			f = 1.0F;
		}

		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount
				* 0.5F / f;
		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F / f;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;
		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount / f;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount
				/ f;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;
		this.bipedRightLeg.rotateAngleZ = 0.0F;
		this.bipedLeftLeg.rotateAngleZ = 0.0F;

		if (this.isRiding) {
			this.bipedRightArm.rotateAngleX += -((float) Math.PI / 3F);
			this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 3F);
			// TODO Adjust?
			this.bipedRightLeg.rotateAngleX = -1.4137167F;
			this.bipedRightLeg.rotateAngleY = ((float) Math.PI / 9F);
			this.bipedRightLeg.rotateAngleZ = 0.07853982F;
			this.bipedLeftLeg.rotateAngleX = -1.4137167F;
			this.bipedLeftLeg.rotateAngleY = -((float) Math.PI / 9F);
			this.bipedLeftLeg.rotateAngleZ = -0.07853982F;
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedRightArm.rotateAngleZ = 0.0F;

		switch (this.leftArmPose) {
		case EMPTY:
			this.bipedLeftArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - 0.9424779F;
			this.bipedLeftArm.rotateAngleY = 0.5235988F;
			break;
		case ITEM:
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			this.bipedLeftArm.rotateAngleY = 0.0F;
		default:
			break;
		}

		switch (this.rightArmPose) {
		case EMPTY:
			this.bipedRightArm.rotateAngleY = 0.0F;
			break;
		case BLOCK:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - 0.9424779F;
			this.bipedRightArm.rotateAngleY = -0.5235988F;
			break;
		case ITEM:
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
			this.bipedRightArm.rotateAngleY = 0.0F;
		default:
			break;
		}

		if (this.swingProgress > 0.0F) {
			EnumHandSide enumhandside = this.getMainHand(entityIn);
			ModelRenderer modelrenderer = this.getArmForSide(enumhandside);
			float f1 = this.swingProgress;
			this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt(f1) * ((float) Math.PI * 2F)) * 0.2F;

			if (enumhandside == EnumHandSide.LEFT) {
				this.bipedBody.rotateAngleY *= -1.0F;
			}

			this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
			this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;
			this.bipedLeftArm.rotateAngleX += this.bipedBody.rotateAngleY;
			f1 = 1.0F - this.swingProgress;
			f1 = f1 * f1;
			f1 = f1 * f1;
			f1 = 1.0F - f1;
			float f2 = MathHelper.sin(f1 * (float) Math.PI);
			float f3 = MathHelper.sin(this.swingProgress * (float) Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F)
					* 0.75F;
			modelrenderer.rotateAngleX = (float) ((double) modelrenderer.rotateAngleX
					- ((double) f2 * 1.2D + (double) f3));
			modelrenderer.rotateAngleY += this.bipedBody.rotateAngleY * 2.0F;
			modelrenderer.rotateAngleZ += MathHelper.sin(this.swingProgress * (float) Math.PI) * -0.4F;
		}

		if (servant.isWaiting()) {
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0F;
			this.bipedLeftLeg.rotationPointZ = 0F;
			this.bipedRightLeg.rotationPointY = 7.0F;
			this.bipedLeftLeg.rotationPointY = 7.0F;
			this.bipedHead.rotationPointY = 0.0F;

			this.bipedLeftArm.rotateAngleX = (float) Math.toRadians(-55D);
			this.bipedLeftArm.rotateAngleY = (float) Math.toRadians(30D);

			this.bipedRightArm.rotateAngleX = (float) Math.toRadians(-55D);
			this.bipedRightArm.rotateAngleY = (float) Math.toRadians(-30D);
		} else if (this.isSneak) {
			this.bipedBody.rotateAngleX = 0.7F;
			this.bipedRightArm.rotateAngleX += 0.4F;
			this.bipedLeftArm.rotateAngleX += 0.4F;
			this.bipedRightLeg.rotationPointZ = 3.0F;
			this.bipedLeftLeg.rotationPointZ = 3.0F;
			this.bipedRightLeg.rotationPointY = 4.0F;
			this.bipedLeftLeg.rotationPointY = 4.0F;
			this.bipedHead.rotationPointY = 1.0F;
		} else {
			this.bipedBody.rotateAngleX = 0.0F;
			this.bipedRightLeg.rotationPointZ = 0.1F;
			this.bipedLeftLeg.rotationPointZ = 0.1F;
			this.bipedRightLeg.rotationPointY = 7.0F;
			this.bipedLeftLeg.rotationPointY = 7.0F;
			this.bipedHead.rotationPointY = 0.0F;
		}

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

		if (this.rightArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
			bipedBody.rotateAngleZ = bipedHead.rotateAngleZ;
			this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY;
			this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY + 0.4F;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;

		} else if (this.leftArmPose == ModelBiped.ArmPose.BOW_AND_ARROW) {
			this.bipedRightArm.rotateAngleY = -0.1F + this.bipedHead.rotateAngleY - 0.4F;
			this.bipedLeftArm.rotateAngleY = 0.1F + this.bipedHead.rotateAngleY;
			this.bipedRightArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
			this.bipedLeftArm.rotateAngleX = -((float) Math.PI / 2F) + this.bipedHead.rotateAngleX;
		}

	}

	@Override
	public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float limbSwing, float limbSwingAmount,
			float partialTickTime) {
		super.setLivingAnimations(entitylivingbaseIn, limbSwing, limbSwingAmount, partialTickTime);
	}

	public void setModelAttributes(ModelBase model) {
		super.setModelAttributes(model);

		if (model instanceof ModelBiped) {
			ModelBiped modelbiped = (ModelBiped) model;
			this.leftArmPose = modelbiped.leftArmPose;
			this.rightArmPose = modelbiped.rightArmPose;
			this.isSneak = modelbiped.isSneak;
		}
	}

	public void setVisible(boolean visible) {
		this.bipedHead.showModel = visible;
		this.bipedBody.showModel = visible;
		this.bipedRightArm.showModel = visible;
		this.bipedLeftArm.showModel = visible;
		this.bipedRightLeg.showModel = visible;
		this.bipedLeftLeg.showModel = visible;
	}

	public void postRenderArm(float scale, EnumHandSide side) {
		this.getArmForSide(side).postRender(scale);
	}

	protected ModelRenderer getArmForSide(EnumHandSide side) {
		return side == EnumHandSide.LEFT ? this.bipedLeftArm : this.bipedRightArm;
	}

	protected EnumHandSide getMainHand(Entity entityIn) {
		if (entityIn instanceof EntityLivingBase) {
			EntityLivingBase entitylivingbase = (EntityLivingBase) entityIn;
			EnumHandSide enumhandside = entitylivingbase.getPrimaryHand();
			return entitylivingbase.swingingHand == EnumHand.MAIN_HAND ? enumhandside : enumhandside.opposite();
		} else {
			return EnumHandSide.RIGHT;
		}
	}
}
