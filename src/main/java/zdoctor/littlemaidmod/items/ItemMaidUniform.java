package zdoctor.littlemaidmod.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import zdoctor.lazylibrary.common.api.ICraftable;
import zdoctor.lazylibrary.common.base.EasyItem;
import zdoctor.lazylibrary.common.item.crafting.RecipeBuilder;
import zdoctor.littlemaidmod.api.IMaidUniform;

public class ItemMaidUniform extends EasyItem implements IMaidUniform, ICraftable {
	// TODO Implent a system to register maid uniforms using this item
	public ItemMaidUniform() {
		super("MaidUniformDefault");
		setCreativeTab(CreativeTabs.MISC);
		setMaxStackSize(1);
	}

	@Override
	public int getUniformID(ItemStack itemstack) {
		return itemstack.getMetadata();
	}

	@Override
	public IRecipe getRecipe() {
		return RecipeBuilder.create("", 3, 3, new ItemStack(this), Items.STRING,
				new ItemStack(Items.DYE, EnumDyeColor.BLACK.getMetadata()), Items.STRING, Items.STRING, Items.STRING,
				Items.STRING, Items.STRING, Items.STRING, Items.STRING);
	}

}
