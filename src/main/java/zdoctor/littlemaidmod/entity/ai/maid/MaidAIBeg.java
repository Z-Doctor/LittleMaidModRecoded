package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.util.EnumMood;

public class MaidAIBeg extends MaidAIBase {

	protected EntityMaid theMaid;
	protected float effectiveRange;

	private EntityPlayer player;
	protected int teaseTicks = 0;
	protected int angryTicks = 0;

	public MaidAIBeg(EntityMaid maidIn, float effectiveRangeIn) {
		theMaid = maidIn;
		effectiveRange = effectiveRangeIn;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		this.player = theMaid.world.getClosestPlayerToEntity(theMaid, effectiveRange);
		EntityPlayer owner = (EntityPlayer) theMaid.getOwner();
		if (owner != null && theMaid.getDistance(owner) <= effectiveRange
				&& theMaid.isFavorite(owner.getHeldItemMainhand()))
			this.player = owner;

		if (angryTicks > 0) {
			angryTicks--;
			if (angryTicks % 20 == 0)
				theMaid.playMoodEffect(EnumMood.ANGRY);
			if (teaseTicks > 0)
				teaseTicks--;
			if (!theMaid.isOwner(player))
				return false;
		}

		boolean hasTemptation = player == null ? false : theMaid.isFavorite(player.getHeldItemMainhand());

		if (!hasTemptation) {
			if (teaseTicks > 0)
				teaseTicks--;
			return false;
		}

		if (!theMaid.hasContract())
			return true;
		if (theMaid.isOwner(player))
			return true;

		if (!theMaid.acceptGiftsFromStangers() || theMaid.getAttackTarget() != null) {
			if (teaseTicks > 0)
				teaseTicks--;
			return false;
		}
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		boolean hasTemptation = theMaid.isFavorite(player.getHeldItemMainhand());

		if (!hasTemptation) {
			if (teaseTicks > 0)
				teaseTicks--;
			return false;
		}
		
		if(theMaid.isOwner(player))
			return true;

		if (!theMaid.isOwner(player) && !theMaid.acceptGiftsFromStangers()) {
			if (teaseTicks > 0)
				teaseTicks--;
			return false;
		}

		if (theMaid.getOwner() != null && theMaid.getOwner().getRevengeTarget() != null && !theMaid.acceptGiftsFromStangers())
			return false;

		if (angryTicks > 0)
			return false;

		return true;
	}

	@Override
	public void startExecuting() {
		 theMaid.setBegging(true);
	}

	@Override
	public void resetTask() {
		theMaid.setBegging(false);
	}

	@Override
	public void updateTask() {
		// theMaid.getLookHelper().setLookPositionWithEntity(player,
		// theMaid.getHorizontalFaceSpeed(),
		// theMaid.getVerticalFaceSpeed());
		// updateTicks--;
		//
		//
		// if (!coolDown && !theMaid.isWaiting()
		// && (player == null || theMaid.getDistance(player) < (theMaid.isOwner(player)
		// ? 2 : 2.5)))
		// theMaid.getNavigator().clearPath();
		// else if (!coolDown && !theMaid.isWaiting())
		// theMaid.getNavigator().tryMoveToEntityLiving(player, moveSpeed);
		//
		// if (!coolDown && updateTicks <= 0) {
		// theMaid.playMoodEffect(EnumMood.ANGRY);
		// coolDown = true;
		// updateTicks = 10 * 20 + theMaid.getRNG().nextInt(40);
		// } else if (coolDown && updateTicks <= 0) {
		// coolDown = false;
		// }
		teaseTicks++;
		if (!theMaid.isOwner(player) && angryTicks <= 0 && teaseTicks > (theMaid.getRNG().nextInt(10) + 5) * 20) {
			angryTicks = (theMaid.getRNG().nextInt(10) + 3) * 20;
			teaseTicks = 0;
		}

		if (!theMaid.isWaiting()
				&& (player == null || theMaid.getDistance(player) < (theMaid.isOwner(player) ? 2 : 2.5)))
			theMaid.getNavigator().clearPath();
		else if (!theMaid.isWaiting())
			theMaid.getNavigator().tryMoveToEntityLiving(player,
					theMaid.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
		theMaid.getLookHelper().setLookPositionWithEntity(player, theMaid.getHorizontalFaceSpeed(),
				theMaid.getVerticalFaceSpeed());
	}

}
