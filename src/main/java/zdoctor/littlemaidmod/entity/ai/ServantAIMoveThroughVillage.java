package zdoctor.littlemaidmod.entity.ai;

import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIMoveThroughVillage extends EntityAIMoveThroughVillage {

	protected EntityServantBase servant;

	public ServantAIMoveThroughVillage(EntityServantBase entityIn, boolean isNocturnal) {
		super(entityIn, entityIn.getAIMoveSpeed(), isNocturnal);
		servant = entityIn;
	}

	@Override
	public boolean shouldExecute() {
		return (!servant.hasContract() || servant.isFreedomMode()) && super.shouldExecute();
	}

}
