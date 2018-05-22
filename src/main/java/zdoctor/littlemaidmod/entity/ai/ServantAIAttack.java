package zdoctor.littlemaidmod.entity.ai;

import net.minecraft.entity.ai.EntityAIAttackMelee;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIAttack extends EntityAIAttackMelee {

	protected EntityServantBase servant;

	public ServantAIAttack(EntityServantBase servant, boolean useLongMemory) {
		super(servant, servant.getAIMoveSpeed(), useLongMemory);
		this.servant = servant;
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
	}

	@Override
	public boolean shouldContinueExecuting() {
		return !servant.isWaiting() && !servant.isBegging() && super.shouldContinueExecuting();
	}

	@Override
	public void resetTask() {
		super.resetTask();
		servant.setSwingingArms(false);
	}

	@Override
	public void updateTask() {
		super.updateTask();
		servant.setSwingingArms(servant.isSwingInProgress);
	}

}
