package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.util.math.Vec3d;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIHurtByTarget;

public class MaidAIHurtByTarget extends ServantAIHurtByTarget {

	protected EntityMaid theMaid;
	protected Vec3d lastPos = null;
	protected boolean wasWaiting = false;

	public MaidAIHurtByTarget(EntityMaid maidIn, boolean entityCallsForHelpIn) {
		super(maidIn, entityCallsForHelpIn);
		theMaid = maidIn;
	}

	public MaidAIHurtByTarget(EntityMaid maidIn, boolean entityCallsForHelpIn, Class<?>[] excludedReinforcementTypes) {
		super(maidIn, entityCallsForHelpIn, excludedReinforcementTypes);
		theMaid = maidIn;
	}

	@Override
	public boolean shouldExecute() {
		if (!theMaid.getMaidMode().isBattleMode())
			return false;

		if (!super.shouldExecute())
			return false;

		EntityLivingBase target = theMaid.getAttackingEntity();

		if (theMaid.hasContract()) {
			if (theMaid.isWaiting() && theMaid.shouldComeToAid()) {
				wasWaiting = true;
				lastPos = theMaid.getPositionVector();
			}
			return true;
		}
		return false;
	}

	@Override
	public void resetTask() {
		super.resetTask();
		if (wasWaiting && lastPos != null) {
			theMaid.getNavigator().tryMoveToXYZ(lastPos.x, lastPos.y, lastPos.z, theMaid.getAIMoveSpeed());
			wasWaiting = false;
			lastPos = null;
		}
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public void updateTask() {
		super.updateTask();
	}

	@Override
	protected boolean isSuitableTarget(EntityLivingBase target, boolean includeInvincibles) {
		boolean goodTarget = super.isSuitableTarget(target, includeInvincibles);
		if (!goodTarget || theMaid.isOwner(target))
			return false;
		if (target instanceof EntityTameable && ((EntityTameable) target).getOwnerId() == theMaid.getOwnerId())
			return false;

		return true;
	}
}
