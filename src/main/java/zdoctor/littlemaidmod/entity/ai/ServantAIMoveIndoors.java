package zdoctor.littlemaidmod.entity.ai;

import net.minecraft.entity.ai.EntityAIMoveIndoors;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIMoveIndoors extends EntityAIMoveIndoors {

	protected EntityServantBase servant;

	public ServantAIMoveIndoors(EntityServantBase entityIn) {
		super(entityIn);
		servant = entityIn;
	}

	@Override
	public boolean shouldExecute() {
		return (!servant.hasContract() || servant.isFreedomMode()) && super.shouldExecute();
	}

}
