//package zdoctor.littlemaidmod.client.model;
//
//import net.minecraft.client.model.ModelRenderer;
//import net.minecraft.client.renderer.GlStateManager;
//import net.minecraft.entity.Entity;
//import net.minecraft.util.EnumHandSide;
//import zdoctor.littlemaidmod.api.IMaid;
//
//public class ModelMaidCusom extends ModelServant {
//
//	public ModelRenderer chignon;
//	public ModelRenderer skirt;
//
//	public ModelMaidCusom() {
//		this(0.0F);
//	}
//
//	public ModelMaidCusom(float modelSize) {
//		super(modelSize);
//		build();
//	}
//
//	@Override
//	public void build() {
//		if (!CUSTOM_MAIDS_LOADED)
//			super.build();
//		else
//			defaultBuild();
//	}
//
//	@Override
//	public void defaultBuild() {
//		super.defaultBuild();
//
//		this.skirt = new ModelRenderer(this, 0, 16);
//		this.skirt.addBox(-4, 0, -4, 8, 8, 8, modelSize);
//		this.skirt.setRotationPoint(0F, 5F, 0F);
//		this.bipedBody.addChild(skirt);
//
//		this.chignon = new ModelRenderer(this, 24, 0);
//		this.chignon.addBox(-4F, 0F, 1F, 8, 4, 3, modelSize); // Hair
//		this.chignon.setTextureOffset(24, 18).addBox(-5F, -7F, 0.2F, 1, 3, 3, modelSize); // ChignonR
//		this.chignon.setTextureOffset(24, 18).addBox(4F, -7F, 0.2F, 1, 3, 3, modelSize); // ChignonL
//		this.chignon.setTextureOffset(52, 10).addBox(-2F, -7.2F, 4F, 4, 4, 2, modelSize); // ChignonB
//		this.chignon.setTextureOffset(46, 20).addBox(-1.5F, -6.8F, 4F, 3, 9, 3, modelSize); // Tail
//		this.chignon.setTextureOffset(58, 21).addBox(-5.5F, -6.8F, 0.9F, 1, 8, 2, modelSize); // SideTailR
//		this.chignon.mirror = true;
//		this.chignon.setTextureOffset(58, 21).addBox(4.5F, -6.8F, 0.9F, 1, 8, 2, modelSize); // SideTailL
//		this.bipedHead.addChild(chignon);
//	}
//
//	@Override
//	public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
//			float headPitch, float scale) {
//		if (CUSTOM_MAIDS_LOADED)
//			super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//		else
//			defaultRender(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//	}
//
//	@Override
//	public void defaultRender(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks,
//			float netHeadYaw, float headPitch, float scale) {
//		GlStateManager.pushMatrix();
//
//		this.chignon.isHidden = !((IMaid) entityIn).hasContract();
//
//		this.skirt.isHidden = !((IMaid) entityIn).hasUniform();
//
//		super.defaultRender(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
//		GlStateManager.popMatrix();
//	}
//
//	@Override
//	public void setVisible(boolean visible) {
//		super.setVisible(visible);
//	}
//
//	@Override
//	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
//			float headPitch, float scaleFactor, Entity entityIn) {
//		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
//
//		if (isSneak) {
//			this.skirt.rotationPointY = 4F;
//		} else {
//			this.skirt.rotationPointY = 5F;
//			// this.bipedCape.rotationPointY = 0.0F;
//		}
//	}
//
//	@Override
//	public void postRenderArm(float scale, EnumHandSide side) {
//		ModelRenderer modelrenderer = this.getArmForSide(side);
//
//		// if (this.smallArms) {
//		// float f = 0.5F * (float) (side == EnumHandSide.RIGHT ? 1 : -1);
//		// modelrenderer.rotationPointX += f;
//		// modelrenderer.postRender(scale);
//		// modelrenderer.rotationPointX -= f;
//		// } else {
//		modelrenderer.postRender(scale);
//		// }
//	}
//
//}
