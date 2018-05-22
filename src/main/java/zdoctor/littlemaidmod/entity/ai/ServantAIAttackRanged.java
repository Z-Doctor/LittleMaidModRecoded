package zdoctor.littlemaidmod.entity.ai;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIAttackRanged;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.MathHelper;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIAttackRanged<T extends EntityServantBase & IRangedAttackMob> extends EntityAIBase {
	public static final AttributeModifier RANGED_ATTACK_MOVEMENT_PENALTY = new AttributeModifier(
			"RANGED_ATTACK_MOVEMENT_PENALTY", -1.25F, 1).setSaved(false);
	public T servant;

	/** The entity the AI instance has been applied to */
	private final EntityLiving entityHost;
	/** The entity (as a RangedAttackMob) the AI instance has been applied to. */
	private final IRangedAttackMob rangedAttackEntityHost;
	private EntityLivingBase attackTarget;
	/**
	 * A decrementing tick that spawns a ranged attack once this value reaches 0. It
	 * is then set back to the maxRangedAttackTime.
	 */
	private int rangedAttackTime;
	private final double entityMoveSpeed;
	private int seeTime;
	private final int attackIntervalMin;
	/**
	 * The maximum time the AI has to wait before peforming another ranged attack.
	 */
	private final int maxRangedAttackTime;
	private final float attackRadius;
	private final float maxAttackDistance;

	public ServantAIAttackRanged(T servantIn, double movespeed, int maxAttackTime, float maxAttackDistanceIn) {
		this(servantIn, movespeed, maxAttackTime, maxAttackTime, maxAttackDistanceIn);
	}

	public ServantAIAttackRanged(T servantIn, double movespeed, int cooldown, int maxAttackTime,
			float maxAttackDistanceIn) {
		this.servant = servantIn;

		this.rangedAttackEntityHost = servantIn;
		this.entityHost = (EntityLiving) servantIn;
		this.entityMoveSpeed = movespeed;
		this.attackIntervalMin = cooldown;
		this.maxRangedAttackTime = maxAttackTime;
		this.attackRadius = maxAttackDistanceIn;
		this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		return !servant.isWaiting() && !servant.isBegging() && hasRangedWeapon() && hasAmmo()
				&& (this.attackTarget = this.entityHost.getAttackTarget()) != null && !this.attackTarget.isDead;
		// this.attackTarget = entitylivingbase;
	}

	protected boolean hasAmmo() {
		return !servant.findAmmo().isEmpty();
	}

	protected boolean hasRangedWeapon() {
		return !this.servant.getHeldItemMainhand().isEmpty()
				&& this.servant.getHeldItemMainhand().getItem() == Items.BOW;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	@Override
	public void resetTask() {
		this.attackTarget = null;
		this.seeTime = 0;
		this.rangedAttackTime = -1;
	}

	@Override
	public void updateTask() {
		double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, this.attackTarget.getEntityBoundingBox().minY,
				this.attackTarget.posZ);
		boolean flag = this.entityHost.getEntitySenses().canSee(this.attackTarget);

		if (flag) {
			++this.seeTime;
		} else {
			this.seeTime = 0;
		}

		if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
			this.entityHost.getNavigator().clearPath();
		} else {
			this.entityHost.getNavigator().tryMoveToEntityLiving(this.attackTarget, this.entityMoveSpeed);
		}

		this.entityHost.getLookHelper().setLookPositionWithEntity(this.attackTarget, 30.0F, 30.0F);

		// if (--this.rangedAttackTime == 0) {
		if (this.servant.isHandActive()) {
			if (!flag && seeTime <= -60)
				servant.resetActiveHand();
			else if (flag) {
				int i = this.servant.getItemInUseMaxCount();

				if (i >= 20) {
					this.servant.resetActiveHand();
					((IRangedAttackMob) this.servant).attackEntityWithRangedAttack(this.attackTarget,
							ItemBow.getArrowVelocity(i));
				}
			}
		} else if (!servant.findAmmo().isEmpty() && shouldShoot()) {
			if (--this.rangedAttackTime <= 0 && this.seeTime >= -60) {
				this.servant.setActiveHand(EnumHand.MAIN_HAND);
			}
		}

		// float f = MathHelper.sqrt(d0) / this.attackRadius;
		// float lvt_5_1_ = MathHelper.clamp(f, 0.1F, 1.0F);
		// this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget,
		// lvt_5_1_);
		// this.rangedAttackTime = MathHelper.floor(
		// f * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float)
		// this.attackIntervalMin);
		// } else if (this.rangedAttackTime < 0) {
		// float f2 = MathHelper.sqrt(d0) / this.attackRadius;
		// this.rangedAttackTime = MathHelper.floor(
		// f2 * (float) (this.maxRangedAttackTime - this.attackIntervalMin) + (float)
		// this.attackIntervalMin);
		// }

	}

	protected boolean shouldShoot() {
		return true;
	}

}