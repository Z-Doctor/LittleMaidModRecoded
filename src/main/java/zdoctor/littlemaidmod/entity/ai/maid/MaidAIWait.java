package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.ai.EntityAISit;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class MaidAIWait extends EntityAISit {

	public EntityMaid theMaid;

	public MaidAIWait(EntityMaid entity) {
		super(entity);
		theMaid = entity;
	}

	@Override
	public boolean shouldExecute() {
		if (!theMaid.hasContract())
			return false;
		// If the master is not on
		if (theMaid.getOwner() == null)
			return theMaid.isWaiting();
		// If the maid should come help if the master is attacked
		if (theMaid.shouldComeToAid())
			return theMaid.getDistanceSq(theMaid.getOwner()) < theMaid.getRespondDistance()
					&& theMaid.getOwner().getRevengeTarget() != null ? false : theMaid.isWaiting();
		return theMaid.isWaiting();
	}

}
