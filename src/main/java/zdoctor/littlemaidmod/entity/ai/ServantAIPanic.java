package zdoctor.littlemaidmod.entity.ai;

import net.minecraft.entity.ai.EntityAIPanic;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIPanic extends EntityAIPanic {

	protected EntityServantBase servant;

	public ServantAIPanic(EntityServantBase servant) {
		super(servant, servant.getAIMoveSpeed());
		this.servant = servant;
	}
	
	@Override
	public boolean shouldExecute() {
		return !servant.hasContract() && super.shouldExecute();
	}
	
}
