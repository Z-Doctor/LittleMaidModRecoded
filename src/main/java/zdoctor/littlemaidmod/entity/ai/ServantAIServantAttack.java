package zdoctor.littlemaidmod.entity.ai;

import net.minecraft.entity.ai.EntityAIAttackMelee;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIServantAttack extends EntityAIAttackMelee {

	protected EntityServantBase servant;

	public ServantAIServantAttack(EntityServantBase servant, boolean useLongMemory) {
		super(servant, servant.getAIMoveSpeed(), useLongMemory);
		this.servant = servant;
	}

}
