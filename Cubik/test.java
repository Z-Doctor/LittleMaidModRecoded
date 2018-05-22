// Cubik Studio 2.9.480 Beta JAVA exporter
// Designed by Z_Doctor

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class CubikModel extends ModelBase {

    //fields
    public ModelRenderer e1;
    public ModelRenderer e2;
    public ModelRenderer e3;
    public ModelRenderer e4;
    public ModelRenderer e5;
    public ModelRenderer e6;

    public CubikModel()
    {
        textureWidth = 64;
        textureHeight = 64;

        e1 = new ModelRenderer(this, 0, 16);
        e1.setRotationPoint(-8F, 16F, -8F);
        e1.addBox(-4F, -8F, -4F, 8, 8, 8);
        e1.setTextureSize(64, 64);
        e1.mirror = false;
        setRotation(e1, 0F, 0F, 0F);
        e2 = new ModelRenderer(this, 0, 0);
        e2.setRotationPoint(-8F, 12F, 4F);
        e2.addBox(0F, -12F, 0F, 8, 12, 4);
        e2.setTextureSize(64, 64);
        e2.mirror = false;
        setRotation(e2, 0F, 0F, 0F);
        e3 = new ModelRenderer(this, 32, 16);
        e3.setRotationPoint(3.999996F, 36.00001F, -2.000004F);
        e3.addBox(3.814697E-06F, -12.00001F, 3.814697E-06F, 4, 12, 4);
        e3.setTextureSize(64, 64);
        e3.mirror = false;
        setRotation(e3, 0F, 0F, 0F);
        e4 = new ModelRenderer(this, 48, 16);
        e4.setRotationPoint(-7.999996F, 36.00001F, -2F);
        e4.addBox(-3.814697E-06F, -12.00001F, 0F, 4, 12, 4);
        e4.setTextureSize(64, 64);
        e4.mirror = false;
        setRotation(e4, 0F, 0F, 0F);
        e5 = new ModelRenderer(this, 32, 0);
        e5.setRotationPoint(-3.99999F, 24.00002F, -2F);
        e5.addBox(-1.049042E-05F, -12.00002F, 0F, 4, 12, 4);
        e5.setTextureSize(64, 64);
        e5.mirror = false;
        setRotation(e5, 0F, 0F, 0F);
        e6 = new ModelRenderer(this, 48, 0);
        e6.setRotationPoint(0F, 24F, -2F);
        e6.addBox(0F, -12F, 0F, 4, 12, 4);
        e6.setTextureSize(64, 64);
        e6.mirror = false;
        setRotation(e6, 0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        e1.render(f5);
        e2.render(f5);
        e3.render(f5);
        e4.render(f5);
        e5.render(f5);
        e6.render(f5);
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