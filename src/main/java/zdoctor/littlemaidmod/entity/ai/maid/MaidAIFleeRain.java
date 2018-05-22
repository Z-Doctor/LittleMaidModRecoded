package zdoctor.littlemaidmod.entity.ai.maid;

import java.util.Random;

import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public class MaidAIFleeRain extends EntityAIBase {

	protected EntityMaid theMaid;
	protected double shelterX;
	protected double shelterY;
	protected double shelterZ;

	public MaidAIFleeRain(EntityMaid maidIn) {
		theMaid = maidIn;
		setMutexBits(1);
	}

	@Override
	public boolean shouldExecute() {
		if (!theMaid.world.isRaining()) {
			return false;
		}

		if (!theMaid.isWet()) {
			return false;
		}

		if (!theMaid.world.canBlockSeeSky(theMaid.getPosition())) {
			return false;
		}

		Vec3d vec3d = findPossibleShelter();

		if (vec3d == null) {
			return false;
		}
		shelterX = vec3d.x;
		shelterY = vec3d.y;
		shelterZ = vec3d.z;
		return true;
	}

	@Override
	public boolean shouldContinueExecuting() {
		return theMaid.getNavigator().noPath() ? false : theMaid.world.canBlockSeeSky(theMaid.getPosition());
	}

	@Override
	public void startExecuting() {
		theMaid.getNavigator().tryMoveToXYZ(shelterX, shelterY, shelterZ, theMaid.getAIMoveSpeed());
	}

	public Vec3d findPossibleShelter() {
		Random random = theMaid.getRNG();

		for (int i = 0; i < 10; i++) {
			int j = MathHelper.floor((theMaid.posX + (i - 5)));
			int k = MathHelper.floor((theMaid.getEntityBoundingBox().minY + random.nextInt(4)) - 2D);
			int l = MathHelper.floor((theMaid.posZ + (i - 5)));
			// TODO add freedom limit
			if (theMaid.getPosition().distanceSq(j, k, l) > 30
					&& (theMaid.isFreedomMode() || theMaid.isMaidMode(EnumMaidMode.WILD))) {
				continue;
			}

			if (!theMaid.world.canBlockSeeSky(new BlockPos(j, k, l))) {
				return new Vec3d(j, k, l);
			}
		}

		return null;
	}
}
