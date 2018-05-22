// Cubik Studio 2.9.480 Beta JAVA exporter
// Designed by Z_Doctor

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CubikModel extends ModelBase {

    //fields
    public ModelRenderer e1_Maidhead;
    public ModelRenderer e2_Maidbody;
    public ModelRenderer e3_Maidleftarm;
    public ModelRenderer e4_Maidrightarm;
    public ModelRenderer e5_Maidleftleg;
    public ModelRenderer e6_Maidrightleg;
    public ModelRenderer e7_Skirt;
    public ModelRenderer e8_Hair;
    public ModelRenderer e9_Chigonright;
    public ModelRenderer e10_Chigonleft;
    public ModelRenderer e11_Chigonback;
    public ModelRenderer e12_Ponytail;
    public ModelRenderer e13_Ponytailright;
    public ModelRenderer e14_Ponytailleft;

    public CubikModel()
    {
        textureWidth = 64;
        textureHeight = 64;

        e1_Maidhead = new ModelRenderer(this, 0, 48);
        e1_Maidhead.setRotationPoint(-8F, 24F, -8F);
        e1_Maidhead.addBox(-4F, -8F, -4F, 8, 8, 8);
        e1_Maidhead.setTextureSize(64, 64);
        e1_Maidhead.mirror = false;
        setRotation(e1_Maidhead, 0F, 0F, 0F);
        e2_Maidbody = new ModelRenderer(this, 0, 37);
        e2_Maidbody.setRotationPoint(-8F, 10F, -8F);
        e2_Maidbody.addBox(-3F, -3.814697E-06F, -2F, 6, 7, 4);
        e2_Maidbody.setTextureSize(64, 64);
        e2_Maidbody.mirror = false;
        setRotation(e2_Maidbody, 0F, 0F, 0F);
        e3_Maidleftarm = new ModelRenderer(this, 0, 1);
        e3_Maidleftarm.setRotationPoint(-11F, 9F, -8F);
        e3_Maidleftarm.addBox(-2F, -1F, -1F, 2, 8, 2);
        e3_Maidleftarm.setTextureSize(64, 64);
        e3_Maidleftarm.mirror = false;
        setRotation(e3_Maidleftarm, 0F, 0F, 0F);
        e4_Maidrightarm = new ModelRenderer(this, 8, 1);
        e4_Maidrightarm.setRotationPoint(-5.000001F, 9.000002F, -8F);
        e4_Maidrightarm.addBox(9.536743E-07F, -1.000002F, -1F, 2, 8, 2);
        e4_Maidrightarm.setTextureSize(64, 64);
        e4_Maidrightarm.mirror = false;
        setRotation(e4_Maidrightarm, 0F, 0F, 0F);
        e5_Maidleftleg = new ModelRenderer(this, 0, 24);
        e5_Maidleftleg.setRotationPoint(-9F, -1.000003F, -8F);
        e5_Maidleftleg.addBox(-2F, 2.861023E-06F, -2F, 3, 9, 4);
        e5_Maidleftleg.setTextureSize(64, 64);
        e5_Maidleftleg.mirror = false;
        setRotation(e5_Maidleftleg, 0F, 0F, 0F);
        e6_Maidrightleg = new ModelRenderer(this, 0, 11);
        e6_Maidrightleg.setRotationPoint(-7F, -1.000003F, -8F);
        e6_Maidrightleg.addBox(-1F, 2.861023E-06F, -2F, 3, 9, 4);
        e6_Maidrightleg.setTextureSize(64, 64);
        e6_Maidrightleg.mirror = false;
        setRotation(e6_Maidrightleg, 0F, 0F, 0F);
        e7_Skirt = new ModelRenderer(this, 32, 48);
        e7_Skirt.setRotationPoint(-8F, 4F, -8F);
        e7_Skirt.addBox(-4F, 0F, -4F, 8, 8, 8);
        e7_Skirt.setTextureSize(64, 64);
        e7_Skirt.mirror = false;
        setRotation(e7_Skirt, 0F, 0F, 0F);
        e8_Hair = new ModelRenderer(this, 20, 41);
        e8_Hair.setRotationPoint(-8F, 16F, -8F);
        e8_Hair.addBox(-4F, -3.814697E-06F, 1F, 8, 4, 3);
        e8_Hair.setTextureSize(64, 64);
        e8_Hair.mirror = false;
        setRotation(e8_Hair, 0F, 0F, 0F);
        e9_Chigonright = new ModelRenderer(this, 54, 42);
        e9_Chigonright.setRotationPoint(-12F, 26.50001F, -6.500003F);
        e9_Chigonright.addBox(-1F, -1.50001F, -1.499997F, 1, 3, 3);
        e9_Chigonright.setTextureSize(64, 64);
        e9_Chigonright.mirror = false;
        setRotation(e9_Chigonright, 0F, 0F, 0F);
        e10_Chigonleft = new ModelRenderer(this, 20, 23);
        e10_Chigonleft.setRotationPoint(-4.000004F, 26.50001F, -6.500003F);
        e10_Chigonleft.addBox(3.814697E-06F, -1.50001F, -1.499997F, 1, 3, 3);
        e10_Chigonleft.setTextureSize(64, 64);
        e10_Chigonleft.mirror = false;
        setRotation(e10_Chigonleft, 0F, 0F, 0F);
        e11_Chigonback = new ModelRenderer(this, 42, 42);
        e11_Chigonback.setRotationPoint(-8F, 25F, -4F);
        e11_Chigonback.addBox(-2F, -2F, 0F, 4, 4, 2);
        e11_Chigonback.setTextureSize(64, 64);
        e11_Chigonback.mirror = false;
        setRotation(e11_Chigonback, 0F, 0F, 0F);
        e12_Ponytail = new ModelRenderer(this, 20, 29);
        e12_Ponytail.setRotationPoint(-8.000002F, 14.00001F, -3.000008F);
        e12_Ponytail.addBox(-1.499998F, -1.500006F, 7.629395E-06F, 3, 9, 3);
        e12_Ponytail.setTextureSize(64, 64);
        e12_Ponytail.mirror = false;
        setRotation(e12_Ponytail, 0F, 0F, 0F);
        e13_Ponytailright = new ModelRenderer(this, 14, 27);
        e13_Ponytailright.setRotationPoint(-13F, 15.50001F, -6.500003F);
        e13_Ponytailright.addBox(-0.5F, -1.00001F, -0.9999971F, 1, 8, 2);
        e13_Ponytailright.setTextureSize(64, 64);
        e13_Ponytailright.mirror = false;
        setRotation(e13_Ponytailright, 0F, 0F, 0F);
        e14_Ponytailleft = new ModelRenderer(this, 14, 14);
        e14_Ponytailleft.setRotationPoint(-3.000005F, 15.50001F, -6.500003F);
        e14_Ponytailleft.addBox(-0.4999952F, -1.00001F, -0.9999971F, 1, 8, 2);
        e14_Ponytailleft.setTextureSize(64, 64);
        e14_Ponytailleft.mirror = false;
        setRotation(e14_Ponytailleft, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        e1_Maidhead.render(f5);
        e2_Maidbody.render(f5);
        e3_Maidleftarm.render(f5);
        e4_Maidrightarm.render(f5);
        e5_Maidleftleg.render(f5);
        e6_Maidrightleg.render(f5);
        e7_Skirt.render(f5);
        e8_Hair.render(f5);
        e9_Chigonright.render(f5);
        e10_Chigonleft.render(f5);
        e11_Chigonback.render(f5);
        e12_Ponytail.render(f5);
        e13_Ponytailright.render(f5);
        e14_Ponytailleft.render(f5);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
     
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
 
}