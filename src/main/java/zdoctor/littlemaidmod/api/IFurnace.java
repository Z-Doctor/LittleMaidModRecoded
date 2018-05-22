package zdoctor.littlemaidmod.api;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;

public interface IFurnace extends ISidedInventory {
	public boolean isBurning();
	
	public int getCookTime(ItemStack stack);
	
}
