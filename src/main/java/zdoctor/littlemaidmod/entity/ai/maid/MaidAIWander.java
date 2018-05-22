package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class MaidAIWander extends EntityAIWanderAvoidWater {

	protected EntityMaid theMaid;

	public MaidAIWander(EntityMaid maidIn, float speedIn) {
		super(maidIn, speedIn);
		theMaid = maidIn;
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && !theMaid.isWaiting();
	}

}
