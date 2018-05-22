package zdoctor.littlemaidmod.entity.ai.maid;

import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.ai.ServantAIPanic;

public class MaidAIPanic extends ServantAIPanic {

	protected EntityMaid maid;

	public MaidAIPanic(EntityMaid maid) {
		super(maid);
		this.maid = maid;
	}
	
}
