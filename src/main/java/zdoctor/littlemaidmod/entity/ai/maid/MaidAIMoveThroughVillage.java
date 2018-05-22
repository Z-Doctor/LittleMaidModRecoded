package zdoctor.littlemaidmod.entity.ai.maid;

import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIMoveThroughVillage;

public class MaidAIMoveThroughVillage extends ServantAIMoveThroughVillage {

	protected EntityMaid maid;

	public MaidAIMoveThroughVillage(EntityMaid entityIn, boolean isNocturnal) {
		super(entityIn, isNocturnal);
		maid = entityIn;
	}

}
