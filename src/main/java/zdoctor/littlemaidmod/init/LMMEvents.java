package zdoctor.littlemaidmod.init;

import net.minecraft.entity.item.EntityXPOrb;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import zdoctor.littlemaidmod.api.IServant;
import zdoctor.littlemaidmod.entity.EntityCustomXP;

public class LMMEvents {
	@SubscribeEvent
	public void mobGreifingEvent(EntityMobGriefingEvent e) {
		if (e.getEntity() instanceof IServant)
			e.setResult(Result.ALLOW);
	}

	@SubscribeEvent
	public void entitySpawned(EntityJoinWorldEvent e) {
		spawnCustomXP(e);
	}

	private void spawnCustomXP(EntityJoinWorldEvent e) {
		if (!e.getWorld().isRemote && !e.getEntity().forceSpawn && e.getEntity() instanceof EntityXPOrb
				&& !(e.getEntity() instanceof EntityCustomXP)) {
			e.setCanceled(true);
			EntityXPOrb orbs = (EntityXPOrb) e.getEntity();
			EntityCustomXP newOrb = new EntityCustomXP(orbs);
			newOrb.forceSpawn = true;
			e.getWorld().spawnEntity(newOrb);
		}
	}
	
	@SubscribeEvent
	public void modelSpawn(ModelBakeEvent e) {
		System.out.println("Reloading assets");
	}

}
