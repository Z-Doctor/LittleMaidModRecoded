package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.ai.EntityAIBase;

public abstract class MaidAIBase extends EntityAIBase {
	@Override
	public void updateTask() {
//		System.out.println("Running Task: " + getClass());
		super.updateTask();
	}
}
