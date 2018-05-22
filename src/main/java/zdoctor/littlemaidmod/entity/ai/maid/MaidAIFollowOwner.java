package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class MaidAIFollowOwner extends MaidAIBase {

	private EntityMaid theMaid;
	private Entity theOwner;
	private float moveSpeed;
	private PathNavigate petPathfinder;
	private int field_48310_h;
	protected double maxDist;
	protected double minDist;
	protected double sprintDist;
	protected double toDistance;
	protected boolean isEnable;

	public MaidAIFollowOwner(EntityMaid theMaidIn, double idleDistance, double maxBusy, double pSprintDistSQ) {
		theMaid = theMaidIn;
		petPathfinder = theMaidIn.getNavigator();
		minDist = idleDistance;
		maxDist = maxBusy;
		sprintDist = pSprintDistSQ;
		setMutexBits(3);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		if (theMaid.isWaiting() || theMaid.isFreedomMode() || theMaid.getAttackTarget() != null)
			return false;

		Entity entityliving = theMaid.getOwner();
		if (entityliving == null) {
			return false;
		}

		toDistance = theMaid.getDistanceSq(entityliving);
		if (toDistance < minDist && !theMaid.isInWater()) {
			return false;
		}
		theOwner = entityliving;
		return true;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean continueExecuting() {
		toDistance = theMaid.getDistanceSq(theOwner);
		if (theMaid.handleWaterMovement())
			return !theMaid.isWaiting();
		return !theMaid.getNavigator().noPath() && (toDistance > maxDist) && !theMaid.isWaiting();
	}

	public void startExecuting() {
		field_48310_h = 0;
	}

	public void resetTask() {
		theMaid.setSprinting(false);
		theOwner = null;
		petPathfinder.clearPath();
	}

	/**
	 * Updates the task
	 */
	public void updateTask() {
//		System.out.println(theMaid.getPosition());
		theMaid.getLookHelper().setLookPositionWithEntity(theOwner, 10F, theMaid.getVerticalFaceSpeed());

		if (theMaid.isSitting()) {
			return;
		}
		if (!theMaid.isInWater()) {
			theMaid.setSprinting(toDistance > sprintDist);
			if (--field_48310_h > 0) {
				return;
			}
		}

		field_48310_h = 10;

		Path entity = theMaid.getNavigator().getPathToEntityLiving(theOwner);
		theMaid.getNavigator().setPath(entity, theMaid.getAIMoveSpeed());
	}

}
