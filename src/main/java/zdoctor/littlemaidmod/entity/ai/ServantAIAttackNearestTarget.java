package zdoctor.littlemaidmod.entity.ai;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.util.math.Vec3d;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIAttackNearestTarget<T extends EntityLivingBase> extends EntityAINearestAttackableTarget<T> {

	protected Vec3d lastPos = null;
	protected boolean wasWaiting = false;

	protected EntityServantBase servant;

	public ServantAIAttackNearestTarget(EntityServantBase servant, Class<T> classTarget, boolean checkSight) {
		this(servant, classTarget, checkSight, false);
	}

	public ServantAIAttackNearestTarget(EntityServantBase servant, Class<T> classTarget, boolean checkSight,
			boolean onlyNearby) {
		this(servant, classTarget, 10, checkSight, onlyNearby, null);
	}

	public ServantAIAttackNearestTarget(EntityServantBase servant, Class<T> classTarget, int chance, boolean checkSight,
			boolean onlyNearby, @Nullable final Predicate<? super T> targetSelector) {
		super(servant, classTarget, chance, checkSight, onlyNearby, targetSelector);
		this.servant = servant;
	}

	@Override
	public boolean shouldExecute() {
		return servant.hasContract() && !servant.isWaiting() && super.shouldExecute();
	}

}
