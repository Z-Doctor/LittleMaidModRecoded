package zdoctor.littlemaidmod.entity.ai.maid;

import zdoctor.littlemaidmod.entity.EntityCustomXP;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.servant.ServantAICollect;
import zdoctor.littlemaidmod.util.EntitySorter;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAICollectXP extends ServantAICollect<EntityCustomXP> {

	private EntityMaid theMaid;

	public MaidAICollectXP(EntityMaid maidIn, double searchRadius, int attemptTime) {
		super(maidIn, searchRadius, attemptTime, new EntitySorter<EntityCustomXP>(maidIn));
		theMaid = maidIn;
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && (theMaid.isMaidMode(EnumMaidMode.ESCORTER) || theMaid.isFreedomMode());
	}

	@Override
	public Class getTargetClass() {
		return EntityCustomXP.class;
	}

	@Override
	public boolean isValidTarget(EntityCustomXP item) {
		if (item.isDead)
			return false;
		if (theMaid.getDistance(item) > searchRadius)
			return false;

		return true;
	}

	@Override
	public boolean notValidTarget(EntityCustomXP item) {
		if (item == null || item.isDead || theMaid.getDistance(item) < 0.2D)
			return true;
		if (theMaid.getDistance(item) > searchRadius)
			return true;
		return false;
	}

}
