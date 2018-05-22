package zdoctor.littlemaidmod.client.gui.handler;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zdoctor.littlemaidmod.client.gui.inventory.GuiServantInventory;
import zdoctor.littlemaidmod.entity.EntityServantBase;
import zdoctor.littlemaidmod.inventory.ContainerMaidInventory;

public class MaidGuiHandler implements IGuiHandler {

	public static final int MAID_INVENTORY = 0;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int entityID, int y, int z) {
		EntityServantBase servant = (EntityServantBase) world.getEntityByID(entityID);
		if (servant == null || !servant.isOwner(player))
			return null;
		switch (ID) {
		case MAID_INVENTORY:
			return new ContainerMaidInventory(servant);

		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int entityID, int y, int z) {
		EntityServantBase servant = (EntityServantBase) world.getEntityByID(entityID);
		if (servant == null || !servant.isOwner(player))
			return null;
		switch (ID) {
		case MAID_INVENTORY:
			return new GuiServantInventory(servant);

		default:
			return null;
		}
	}

}
