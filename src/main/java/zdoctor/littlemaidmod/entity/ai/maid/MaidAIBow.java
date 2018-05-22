package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAIBow extends EntityAIBase {
	private final EntityMaid maid;
	private final double moveSpeedAmp;
	private int attackCooldown;
	private final float maxAttackDistance;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public MaidAIBow(EntityMaid maidIn, double moveSpeedAmpIn, int attackCoolDownIn,
			float maxAttackDistanceIn) {
		this.maid = maidIn;
		this.moveSpeedAmp = moveSpeedAmpIn;
		this.attackCooldown = attackCoolDownIn;
		this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
		this.setMutexBits(3);
	}

	public void setAttackCooldown(int p_189428_1_) {
		this.attackCooldown = p_189428_1_;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	public boolean shouldExecute() {
		return !maid.isWaiting() && maid.getMaidMode() == EnumMaidMode.ARCHER && this.maid.getAttackTarget() == null ? false : this.isBowInMainhand();
	}

	protected boolean isBowInMainhand() {
//		System.out.println("Test: " + this.maid.getHeldItemMainhand().getItem());
		return !this.maid.getHeldItemMainhand().isEmpty() && this.maid.getHeldItemMainhand().getItem() == Items.BOW;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean shouldContinueExecuting() {
		return this.shouldExecute();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void startExecuting() {
		super.startExecuting();
		this.maid.setSwingingArms(true);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void resetTask() {
		super.resetTask();
		((IRangedAttackMob) this.maid).setSwingingArms(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.maid.resetActiveHand();
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.maid.getAttackTarget();

		if (entitylivingbase != null) {
			double d0 = this.maid.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
					entitylivingbase.posZ);
			boolean flag = this.maid.getEntitySenses().canSee(entitylivingbase);
			boolean flag1 = this.seeTime > 0;

			if (flag != flag1) {
				this.seeTime = 0;
			}

			if (flag) {
				++this.seeTime;
			} else {
				--this.seeTime;
			}

			if (d0 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
				this.maid.getNavigator().clearPath();
				++this.strafingTime;
			} else {
				this.maid.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.moveSpeedAmp);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.maid.getRNG().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.maid.getRNG().nextFloat() < 0.3D) {
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1) {
				if (d0 > (double) (this.maxAttackDistance * 0.75F)) {
					this.strafingBackwards = false;
				} else if (d0 < (double) (this.maxAttackDistance * 0.25F)) {
					this.strafingBackwards = true;
				}

				this.maid.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F,
						this.strafingClockwise ? 0.5F : -0.5F);
				this.maid.faceEntity(entitylivingbase, 30.0F, 30.0F);
			} else {
				this.maid.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			}

			if (this.maid.isHandActive()) {
				if (!flag && this.seeTime < -60) {
					this.maid.resetActiveHand();
				} else if (flag) {
					int i = this.maid.getItemInUseMaxCount();

					if (i >= 20) {
						this.maid.resetActiveHand();
						((IRangedAttackMob) this.maid).attackEntityWithRangedAttack(entitylivingbase,
								ItemBow.getArrowVelocity(i));
						this.attackTime = this.attackCooldown;
					}
				}
			} else if (--this.attackTime <= 0 && this.seeTime >= -60) {
				this.maid.setActiveHand(EnumHand.MAIN_HAND);
			}
		}
	}
}
