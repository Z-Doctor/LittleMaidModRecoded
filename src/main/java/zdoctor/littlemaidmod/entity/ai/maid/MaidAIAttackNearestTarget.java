package zdoctor.littlemaidmod.entity.ai.maid;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIAttackNearestTarget;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAIAttackNearestTarget<T extends EntityLivingBase> extends ServantAIAttackNearestTarget<T> {

	protected EntityMaid maid;

	public MaidAIAttackNearestTarget(EntityMaid maid, Class<T> classTarget, boolean checkSight) {
		this(maid, classTarget, checkSight, false);
	}

	public MaidAIAttackNearestTarget(EntityMaid maid, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
		this(maid, classTarget, 10, checkSight, onlyNearby, null);
	}

	public MaidAIAttackNearestTarget(EntityMaid maid, Class<T> classTarget, int chance, boolean checkSight,
			boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
		super(maid, classTarget, chance, checkSight, onlyNearby, targetSelector);
		this.maid = maid;
	}

	@Override
	public boolean shouldExecute() {
		return maid.getMaidMode().isBattleMode() && super.shouldExecute();
	}

	@Override
	public void startExecuting() {
		super.startExecuting();
		if (maid.getAttackTarget() instanceof EntityLivingBase) {
			if (maid.isMaidMode(EnumMaidMode.FENCER) && maid.getAttackTarget() instanceof EntityCreeper) {
				EntityCreeper creeper = (EntityCreeper) maid.getAttackTarget();
				if (creeper.getAttackTarget() != maid) {
					if (maid.getMaster() == null || creeper.getAttackTarget() == null) {
						maid.setAttackTarget(null);
					} else if (!maid.isOwner(creeper.getAttackTarget())) {
						maid.setAttackTarget(null);
					}
				}
			}
			maid.setAttackTarget(maid.getAttackTarget());
		}
	}

}
