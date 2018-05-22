package zdoctor.littlemaidmod.entity.ai;

import net.minecraft.entity.ai.EntityAIHurtByTarget;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ServantAIHurtByTarget extends EntityAIHurtByTarget {

	protected EntityServantBase servant;

	public ServantAIHurtByTarget(EntityServantBase servant, boolean entityCallsForHelpIn) {
		this(servant, entityCallsForHelpIn, new Class[0]);
	}

	public ServantAIHurtByTarget(EntityServantBase servant, boolean entityCallsForHelpIn,
			Class<?>[] excludedReinforcementTypes) {
		super(servant, entityCallsForHelpIn, excludedReinforcementTypes);
		this.servant = servant;
	}

}
