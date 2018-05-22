package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import zdoctor.littlemaidmod.entity.EntityMaid;

// May just replace this with a modified EntityAIFollowOwner
public class MaidAIJumpToMaster extends MaidAIBase {

	protected EntityMaid theMaid;
	protected boolean jumpTarget;
	protected float threshold;

	public MaidAIJumpToMaster(EntityMaid entity, float threshold) {
		theMaid = entity;
		this.threshold = threshold;
		this.setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (!theMaid.hasContract() || theMaid.isWaiting() || theMaid.getLeashed() || theMaid.isFreedomMode()
				|| theMaid.isSentryMode())
			return false;
		if (theMaid.getOwner() == null)
			return false;
		if (theMaid.getDistance(theMaid.getOwner())
				/ theMaid.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getAttributeValue() < threshold)
			return false;

		// if (theMaid.isFreedomMode()) {
		// if (theMaid.getHomeDimension().getId() != theMaid.dimension) {
		// theMaid.setHomePosAndDistance(theMaid.getPosition(), (int)
		// theMaid.getMaximumHomeDistance());
		// return false;
		// }
		//
		// if (!theMaid.isWithinHomeDistanceCurrentPosition()) {
		// jumpTarget = false;
		// return true;
		// }
		//
		// } else {
		// jumpTarget = true;
		// if (theMaid.getAttackTarget() == null) {
		// if (theMaid.getDistanceSq(theMaid.getOwner()) < 144F)
		// return false;
		// } else if (theMaid.getDistanceSq(
		// theMaid.getOwner()) < (theMaid.getMaidMode() == EnumMaidMode.BLOODSUCKER ?
		// 1024F : 256F))
		// return false;
		// return true;
		// }
		return true;
	}

	@Override
	public void startExecuting() {
		System.out.println("Jump");
		// if (jumpTarget) {
		int i = theMaid.getOwner().getPosition().getX() - 2;
		int j = theMaid.getOwner().getPosition().getZ() - 2;
		int k = MathHelper.floor(theMaid.getOwner().getEntityBoundingBox().minY);

		for (int l = 0; l <= 4; l++) {
			for (int i1 = 0; i1 <= 4; i1++) {
				if ((l < 1 || i1 < 1 || l > 3 || i1 > 3)
						&& theMaid.getOwner().world.getBlockState(new BlockPos(i + l, k - 1, j + i1)).isNormalCube()
						&& !theMaid.getOwner().world.getBlockState(new BlockPos(i + l, k, j + i1)).isNormalCube()
						&& !theMaid.getOwner().world.getBlockState(new BlockPos(i + l, k + 1, j + i1)).isNormalCube()) {
					double dd = theMaid.getOwner().getDistanceSq(
							i + l + 0.5D + MathHelper.sin(theMaid.getOwner().rotationYaw * 0.01745329252F) * 2.0D, k,
							j + i1 - MathHelper.cos(theMaid.getOwner().rotationYaw * 0.01745329252F) * 2.0D);
					if (dd > 8D) {
						theMaid.setAttackTarget(null);
						theMaid.setRevengeTarget(null);
						theMaid.getNavigator().clearPath();
						theMaid.setLocationAndAngles(i + l + 0.5F, k, j + i1 + 0.5F, theMaid.rotationYaw,
								theMaid.rotationPitch);
						return;
					}
				}
			}
			// }
			// } else {
			// int lx = theMaid.getHomePosition().getX();
			// int ly = theMaid.getHomePosition().getY();
			// int lz = theMaid.getHomePosition().getZ();
			// if (!(isCanJump(lx, ly, lz))) {
			// int a;
			// int b;
			// boolean f = false;
			// for (a = 1; a < 6 && !f; a++) {
			// if (isCanJump(lx, ly + a, lz)) {
			// f = true;
			// ly += a;
			// break;
			// }
			// }
			// for (a = -1; a > -6 && !f; a--) {
			// if (isCanJump(lx, ly + a, lz)) {
			// f = true;
			// ly += a;
			// break;
			// }
			// }
			//
			// loop_search: for (a = 2; a < 18 && !f; a += 2) {
			// lx--;
			// lz--;
			// for (int c = 0; c < 4; c++) {
			// for (b = 0; b <= a; b++) {
			// if (isCanJump(lx, ly + a, lz)) {
			// f = true;
			// break loop_search;
			// }
			// if (c == 0)
			// lx++;
			// else if (c == 1)
			// lz++;
			// else if (c == 2)
			// lx--;
			// else if (c == 3)
			// lz--;
			// }
			// }
			// }
			// if (f) {
			// // theMaid.func_110171_b(lx, ly, lz, (int) theMaid.func_110174_bM());
			// // theMaid.func_175449_a(new BlockPos(lx, ly, lz), (int)
			// // theMaid.getMaximumHomeDistance());
			// } else {
			// if (isCanJump(lx, ly - 6, lz)) {
			// ly -= 6;
			// }
			// }
			// } else {
			// }
			//
			// theMaid.setAttackTarget(null);
			// theMaid.setRevengeTarget(null);
			// theMaid.getNavigator().clearPath();
			//
			// theMaid.setLocationAndAngles(lx + 0.5D, ly, lz + 0.5D, theMaid.rotationYaw,
			// theMaid.rotationPitch);
			// // theMaid.onJump();
			// return;
		}

		theMaid.setAttackTarget(null);
		theMaid.setRevengeTarget(null);
		theMaid.getNavigator().clearPath();
	}

	protected boolean isCanJump(double px, double py, double pz) {
		return theMaid.world.getBlockState(new BlockPos(px, py - 1, pz)).getMaterial() == Material.AIR;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return false;
	}

	@Override
	public boolean isInterruptible() {
		return true;
	}

}
