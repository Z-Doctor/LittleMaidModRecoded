package zdoctor.littlemaidmod.entity.ai;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIAttackRanged2<T extends EntityServantBase & IRangedAttackMob> extends EntityAIBase {
	public static final AttributeModifier RANGED_ATTACK_MOVEMENT_PENALTY = new AttributeModifier(
			"RANGED_ATTACK_MOVEMENT_PENALTY", -1.25F, 1).setSaved(false);

	protected final T servant;
	protected final double moveSpeedAmp;
	protected int attackCooldown;
	protected final float maxAttackDistance;
	protected int attackTime = -1;
	protected int seeTime;
	protected boolean strafingClockwise;
	protected boolean strafingBackwards;
	protected int strafingTime = -1;

	public ServantAIAttackRanged2(T servantIn, double moveSpeedAmp, int cooldown, float range) {
		this.servant = servantIn;
		this.moveSpeedAmp = moveSpeedAmp;
		this.attackCooldown = cooldown;
		this.maxAttackDistance = range * range;
		this.setMutexBits(3);

//		senses = new DebugSenses(servantIn);
	}

	public void setAttackCooldown(int p_189428_1_) {
		this.attackCooldown = p_189428_1_;
	}

	@Override
	public boolean shouldExecute() {
		return !servant.isWaiting() && !servant.isBegging() && this.servant.getAttackTarget() == null ? false
				: (this.hasRangedWeapon() && hasAmmo())
						&& servant.getDistance(servant.getAttackTarget()) <= maxAttackDistance;
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
	public void startExecuting() {
		((IRangedAttackMob) this.servant).setSwingingArms(true);
//		if (!servant.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(getAttackPenalty()))
//			servant.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(getAttackPenalty());
	}

	public AttributeModifier getAttackPenalty() {
		return RANGED_ATTACK_MOVEMENT_PENALTY;
	}

	@Override
	public void resetTask() {
		((IRangedAttackMob) this.servant).setSwingingArms(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.servant.resetActiveHand();

		this.strafingTime = -1;
		servant.getNavigator().clearPath();
		if (!shouldExecute()
				&& !servant.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(getAttackPenalty()))
			servant.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(getAttackPenalty());
	}

	@Override
	public void updateTask() {
		EntityLivingBase entitylivingbase = this.servant.getAttackTarget();

		if (entitylivingbase != null) {
			double d0 = this.servant.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY,
					entitylivingbase.posZ);
			boolean flag = this.servant.getEntitySenses().canSee(entitylivingbase);
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
				this.servant.getNavigator().clearPath();
				++this.strafingTime;
			} else {
				this.servant.getNavigator().tryMoveToEntityLiving(entitylivingbase, moveSpeedAmp);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20) {
				if ((double) this.servant.getRNG().nextFloat() < 0.3D) {
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.servant.getRNG().nextFloat() < 0.3D) {
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

				this.servant.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F,
						this.strafingClockwise ? 0.15F : -0.15F);
				this.servant.faceEntity(entitylivingbase, 30.0F, 30.0F);
			} else {
				this.servant.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			}

			if (this.servant.isHandActive()) {
				if (!flag && this.seeTime < -60) {
					this.servant.resetActiveHand();
				} else if (flag) {
					int i = this.servant.getItemInUseMaxCount();

					if (i >= 20) {
						this.servant.resetActiveHand();
						((IRangedAttackMob) this.servant).attackEntityWithRangedAttack(entitylivingbase,
								ItemBow.getArrowVelocity(i));
						this.attackTime = this.attackCooldown;
					}
				}
			} else if (!servant.findAmmo().isEmpty() && shouldShoot()) {
				if (--this.attackTime <= 0 && this.seeTime >= -60) {
					this.servant.setActiveHand(EnumHand.MAIN_HAND);
				}
			}
		}
	}

	protected boolean shouldShoot() {
		return servant.getEntitySenses().canSee(servant.getAttackTarget());
		// return true;
	}

//	public DebugSenses senses;
//	public List<EntityLiving> marked = new ArrayList<>();
//
//	public void debug() {
//		senses.getSeenEntities().forEach(entity -> {
//			if (entity instanceof EntityLiving) {
//				EntityLiving living = (EntityLiving) entity;
//				if (living.getActivePotionEffect(MobEffects.GLOWING) == null) {
//					living.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200, 0));
//					if (!marked.contains(living))
//						marked.add(living);
//				}
//			}
//		});
//
//		senses.getUnseenEntities().forEach(entity -> {
//			if (entity instanceof EntityLiving) {
//				EntityLiving living = (EntityLiving) entity;
//				if (living.getActivePotionEffect(MobEffects.GLOWING) != null) {
//					living.removeActivePotionEffect(MobEffects.GLOWING);
//					if (marked.contains(living))
//						marked.remove(living);
//				}
//			}
//		});
//
//		marked.forEach(entity -> {
//			if (senses.canSee(entity)) {
//				if (entity.getActivePotionEffect(MobEffects.GLOWING) == null)
//					entity.addPotionEffect(new PotionEffect(MobEffects.GLOWING, 200, 0));
//			} else if (entity.getActivePotionEffect(MobEffects.GLOWING) != null)
//				entity.removeActivePotionEffect(MobEffects.GLOWING);
//		});
//	}

}