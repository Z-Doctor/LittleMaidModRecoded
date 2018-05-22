package zdoctor.littlemaidmod.entity.ai.maid;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIEat;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAIEatSugar extends ServantAIEat {

	protected EntityMaid maid;

	public MaidAIEatSugar(EntityMaid maid, Predicate<ItemStack> foodCondition) {
		super(maid, foodCondition);
		this.maid = maid;
	}

	@Override
	public boolean shouldExecute() {
		if (servant.isDead || servant.getHealth() == servant.getMaxHealth())
			return false;
		if (maid.isMaidMode(EnumMaidMode.BLOODSUCKER) && servant.getAttackTarget() != null)
			return false;
		if (servant.getAttackTarget() != null && servant.getHealth() / servant.getMaxHealth() >= 0.2F)
			return false;
		return true;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		if (!shouldExecute()) {
			if (maid.getMaidMode().isBattleMode() && !maid.getMaidMode().check(maid.getHeldItem(EnumHand.MAIN_HAND))) {
				for (int i = 0; i < maid.servantInventory.getMainInventorySize(); i++) {// +1 for active hand slot
					int slot = i + 1;
					ItemStack stack = maid.servantInventory.getStackInSlot(slot);
					if (maid.getMaidMode().check(stack)) {
						maid.servantInventory.swapStacks(0, slot);
						break;
					}
				}
			}
		}
	}

}
