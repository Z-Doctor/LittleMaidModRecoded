package zdoctor.littlemaidmod.entity.ai;

import com.google.common.base.Predicate;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import zdoctor.littlemaidmod.entity.EntityServantBase;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIBase;

public abstract class ServantAIEat<T extends ItemFood> extends MaidAIBase {

	protected EntityServantBase servant;
	protected Predicate<ItemStack> foodCondition;
	protected ItemStack eatingStack;
	protected ItemStack activeStack;
	protected int stackSlot = -1;

	public ServantAIEat(EntityServantBase servant, Predicate<ItemStack> foodCondition) {
		this.servant = servant;
		this.foodCondition = foodCondition;
		setMutexBits(8); // Eating
	}

	@Override
	public void startExecuting() {
		if (parseInventory()) {
			servant.servantInventory.setActiveStack(stackSlot);
			activeStack = servant.getActiveItemStack();
		}
	}

	@Override
	public boolean shouldContinueExecuting() {
		if (activeStack == null || activeStack.isEmpty())
			return false;
		return shouldExecute() && servant.getActiveItemStack() == activeStack;
	}
	
	@Override
	public void resetTask() {
		if (activeStack == servant.getActiveItemStack())
			servant.resetActiveHand();
		activeStack = null;
	}

	public boolean parseInventory() {
		stackSlot = -1;
		for (int i = 0; i < servant.servantInventory.getSizeInventory(); i++) {
			ItemStack stack = servant.servantInventory.getStackInSlot(i);
			if (foodCondition.apply(stack)) {
				stackSlot = i;
				return true;
			}
		}
		return false;
	}

}
