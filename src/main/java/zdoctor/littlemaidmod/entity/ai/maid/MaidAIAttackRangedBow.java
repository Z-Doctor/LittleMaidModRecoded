package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIAttackRanged;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAIAttackRangedBow extends ServantAIAttackRanged<EntityMaid> {

	protected EntityMaid maid;

	public MaidAIAttackRangedBow(EntityMaid maid,  double moveSpeedAmp, int maxAttackTime, float maxAttackDistanceIn) {
		super(maid, moveSpeedAmp, maxAttackTime, maxAttackDistanceIn);
		this.maid = maid;
	}

	@Override
	public boolean shouldExecute() {
		return maid.isMaidMode(EnumMaidMode.ARCHER) && super.shouldExecute();
	}

	public boolean hasRangedWeapon() {
		if (maid.getMaidMode().check(maid.getHeldItemMainhand()))
			return true;
		if (maid.getMaidMode().isBattleMode() && !maid.getMaidMode().check(maid.getHeldItem(EnumHand.MAIN_HAND))) {
			for (int i = 0; i < maid.servantInventory.getMainInventorySize(); i++) {// +1 for active hand slot
				int slot = i + 1;
				ItemStack stack = maid.servantInventory.getStackInSlot(slot);
				if (maid.getMaidMode().check(stack)) {
					maid.servantInventory.swapStacks(0, slot);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public void updateTask() {
		super.updateTask();
	}
	
}
