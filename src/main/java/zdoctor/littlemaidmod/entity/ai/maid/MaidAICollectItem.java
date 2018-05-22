package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.item.EntityItem;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.servant.ServantAICollect;
import zdoctor.littlemaidmod.util.EntitySorter;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAICollectItem extends ServantAICollect<EntityItem> {

	private EntityMaid theMaid;

	public MaidAICollectItem(EntityMaid maidIn, double searchRadius, int attemptTime) {
		super(maidIn, searchRadius, attemptTime, new EntitySorter<EntityItem>(maidIn));
		theMaid = maidIn;
	}

	@Override
	public boolean shouldExecute() {
		return super.shouldExecute() && (theMaid.isMaidMode(EnumMaidMode.ESCORTER) || theMaid.isFreedomMode());
	}

	@Override
	public Class getTargetClass() {
		return EntityItem.class;
	}

	@Override
	public boolean isValidTarget(EntityItem item) {
		if (item.isDead || item.cannotPickup())
			return false;
		if (theMaid.getDistance(item) > searchRadius)
			return false;

		return true;
	}

	@Override
	public boolean notValidTarget(EntityItem item) {
		if (item == null || item.isDead || item.cannotPickup())
			return true;
		if (theMaid.getDistance(item) > searchRadius)
			return true;
		return false;
	}

}
