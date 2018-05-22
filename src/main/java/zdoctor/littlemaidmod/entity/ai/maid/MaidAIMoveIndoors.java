package zdoctor.littlemaidmod.entity.ai.maid;

import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIMoveIndoors;

public class MaidAIMoveIndoors extends ServantAIMoveIndoors {

	protected EntityMaid maid;

	public MaidAIMoveIndoors(EntityMaid entityIn) {
		super(entityIn);
		maid = entityIn;
	}

}
