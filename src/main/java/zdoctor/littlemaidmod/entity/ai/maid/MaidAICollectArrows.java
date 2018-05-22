package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.servant.ServantAICollect;
import zdoctor.littlemaidmod.util.EntitySorter;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAICollectArrows extends ServantAICollect<EntityArrow> {

	private EntityMaid theMaid;

	public MaidAICollectArrows(EntityMaid maidIn, double searchRadius, int attemptTime) {
		super(maidIn, searchRadius, attemptTime, new EntitySorter<EntityArrow>(maidIn));
		theMaid = maidIn;
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && (theMaid.isMaidMode(EnumMaidMode.ESCORTER) || theMaid.isFreedomMode());
	}

	@Override
	public Class getTargetClass() {
		return EntityArrow.class;
	}

	@Override
	public boolean isValidTarget(EntityArrow item) {
		if (item.isDead || item.pickupStatus != PickupStatus.ALLOWED)
			return false;
		if (theMaid.getDistance(item) > searchRadius)
			return false;

		return true;
	}

	@Override
	public boolean notValidTarget(EntityArrow item) {
		if (item == null || item.isDead || theMaid.getDistance(item) < 0.2D || item.pickupStatus != PickupStatus.ALLOWED)
			return true;
		if (theMaid.getDistance(item) > searchRadius)
			return true;
		return false;
	}

}
