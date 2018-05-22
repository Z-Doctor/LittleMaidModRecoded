package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIAttack;

public class MaidAIAttack extends ServantAIAttack {

	protected EntityMaid maid;

	public MaidAIAttack(EntityMaid maid, boolean useLongMemory) {
		super(maid, useLongMemory);
		this.maid = maid;
		setMutexBits(getMutexBits() | 8);
	}

	@Override
	public boolean shouldExecute() {
		return maid.getMaidMode().isBattleMode() && !maid.getMaidMode().isRangedMode() && hasWeapon()
				&& super.shouldExecute();
	}

	@Override
	public void startExecuting() {
		hasWeapon();
		super.startExecuting();
	}

	public boolean hasWeapon() {
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

}
