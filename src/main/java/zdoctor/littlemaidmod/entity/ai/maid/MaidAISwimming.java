package zdoctor.littlemaidmod.entity.ai.maid;

import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import zdoctor.littlemaidmod.entity.EntityMaid;

public class MaidAISwimming extends EntityAISwimming {

	protected EntityMaid entity;
	private Vec3d land;
	private Vec3d lastLand;
	int coolDown = 0;

	public MaidAISwimming(EntityMaid maidIn) {
		super(maidIn);
		entity = maidIn;
	}

	@Override
	public boolean shouldExecute() {
		return this.entity.isInWater() || this.entity.isInLava() || this.entity.isInsideOfMaterial(Material.WATER) || this.entity.isInsideOfMaterial(Material.LAVA);
	}
	
	@Override
	public boolean isInterruptible() {
		return !(this.entity.isInLava() || this.entity.isInsideOfMaterial(Material.LAVA));
	}

	@Override
	public void updateTask() {
		// First Swims up
		entity.getJumpHelper().setJumping();
		
		PathNavigate navigator = entity.getNavigator();
		coolDown++;
		System.out.println(coolDown);
		if(lastLand != null && coolDown < 3*20)
			return;
		coolDown = 0;
		
		double speed = entity.isInLava() || entity.isInsideOfMaterial(Material.LAVA) ? .7 : .5;
		Vec3d land = RandomPositionGenerator.getLandPos(entity, MathHelper.floor(navigator.getPathSearchRange()), 3);
		Path landPos;
		if(land != null) {
			landPos = navigator.getPathToPos(new BlockPos(land));
			if(lastLand == null)
				navigator.setPath(landPos, speed);
			else if(navigator.getPath().getCurrentPos().distanceTo(land) < navigator.getPath().getCurrentPos().distanceTo(lastLand))
				navigator.setPath(landPos, speed);
		} else if(entity.hasContract() && !entity.isWaiting())
			navigator.tryMoveToEntityLiving(entity.getMaster(), speed);
		else {
			land = RandomPositionGenerator.findRandomTarget(entity, 5, 1);
			landPos = navigator.getPathToPos(new BlockPos(land));
			if(lastLand == null)
				navigator.setPath(landPos, speed);
			else if(navigator.getPath().getCurrentPos().distanceTo(land) < navigator.getPath().getCurrentPos().distanceTo(lastLand))
				navigator.setPath(landPos, speed);
		}
		lastLand = land;
	}
	
	@Override
	public void resetTask() {
	}
	
}
