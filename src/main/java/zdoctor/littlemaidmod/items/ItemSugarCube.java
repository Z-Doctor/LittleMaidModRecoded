package zdoctor.littlemaidmod.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import zdoctor.lazylibrary.common.api.ICraftable;
import zdoctor.lazylibrary.common.base.EasyFood;
import zdoctor.lazylibrary.common.item.crafting.RecipeBuilder;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ItemSugarCube extends EasyFood implements ICraftable {

	public ItemSugarCube() {
		super("sugarcube", 1, 0.1F, false);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityServantBase) {
			EntityServantBase servant = (EntityServantBase) entityLiving;
			// servant.getFoodStats().addStats(this, stack);
			worldIn.playSound(null, servant.posX, servant.posY, servant.posZ, SoundEvents.ENTITY_PLAYER_BURP,
					SoundCategory.PLAYERS, 0.5F, worldIn.rand.nextFloat() * 0.1F + 0.9F);
			this.onServantEaten(stack, worldIn, servant);
			// servant.addStat(StatList.getObjectUseStats(this));
		}
		return super.onItemUseFinish(stack, worldIn, entityLiving);
	}

	public void onServantEaten(ItemStack stack, World world, EntityServantBase servant) {
		applyEffect(stack, world, servant);
		servant.heal(1);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 10;
	}

	@Override
	public IRecipe getRecipe() {
		return RecipeBuilder.create("", new ItemStack(this), Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR,
				Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR, Items.SUGAR);
	}

}
