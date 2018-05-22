package zdoctor.littlemaidmod.client.renderer.entity.maid.layer;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.layers.LayerArmorBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zdoctor.littlemaidmod.MaidModMain;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class LayerMaidArmor<T extends ModelBase> extends LayerArmorBase<T> {

	public LayerMaidArmor(RenderLivingBase<? extends EntityMaid> rendererIn) {
		super(rendererIn);
	}

	@Override
	protected void initArmor() {

	}

	@Override
	protected void setModelSlotVisible(T p_188359_1_, EntityEquipmentSlot slotIn) {

	}

	@Override
	protected T getArmorModelHook(EntityLivingBase entity, ItemStack itemStack, EntityEquipmentSlot slot, T model) {
		// TODO Auto-generated method stub
		return super.getArmorModelHook(entity, itemStack, slot, model);
	}

	@Override
	public ResourceLocation getArmorResource(Entity entity, ItemStack stack, EntityEquipmentSlot slot, String type) {
		ItemArmor item = (ItemArmor) stack.getItem();
		String texture = item.getArmorMaterial().getName();
		String domain = MaidModMain.MODID;
		int idx = texture.indexOf(':');
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}
		String s1 = String.format("%s:textures/models/armor/%s_layer_%d%s.png", domain, texture,
				(isLegSlot(slot) ? 2 : 1), type == null ? "" : String.format("_%s", type));

		s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = new ResourceLocation(s1);

		return resourcelocation;
	}

	private boolean isLegSlot(EntityEquipmentSlot slotIn) {
		return slotIn == EntityEquipmentSlot.LEGS;
	}

}
