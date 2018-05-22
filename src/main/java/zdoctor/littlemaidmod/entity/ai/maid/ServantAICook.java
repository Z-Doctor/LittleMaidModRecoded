package zdoctor.littlemaidmod.entity.ai.maid;

import java.util.ArrayList;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.math.MathHelper;
import zdoctor.littlemaidmod.entity.EntityCustomXP;
import zdoctor.littlemaidmod.entity.EntityMaid;
import zdoctor.littlemaidmod.entity.EntityServantBase;
import zdoctor.littlemaidmod.inventory.ContainerInventoryServant;
import zdoctor.littlemaidmod.util.TileEntitySorter;

public class ServantAICook extends EntityAIBase {

	private EntityServantBase servant;
	private ContainerInventoryServant inventory;
	protected ArrayList<TileEntityFurnace> furnaces = new ArrayList<>();
	protected TileEntityFurnace currentTarget;

	public EnumCookAIMode currentTask = EnumCookAIMode.IDLE;
	protected TileEntitySorter<TileEntityFurnace> sorter;

	public ServantAICook(EntityServantBase servantIn) {
		this.servant = servantIn;
		this.inventory = servant.servantInventory;
		sorter = new TileEntitySorter<TileEntityFurnace>(servant);
		PathNavigate navigator = servant.getNavigator();
	}

	@Override
	public boolean shouldExecute() {
		return findTargets();
	}

	public boolean findTargets() {
		furnaces.clear();
		servant.world.loadedTileEntityList.forEach(tile -> {
			if (tile instanceof TileEntityFurnace)
				if (Math.sqrt(servant.getDistanceSq(tile.getPos())) <= servant
						.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue())
					furnaces.add((TileEntityFurnace) tile);
		});
		return !furnaces.isEmpty();
	}
	
	@Override
	public void startExecuting() {
		currentTask = EnumCookAIMode.THINKING;
		furnaces.sort(sorter);
	}

	@Override
	public void resetTask() {
		currentTask = EnumCookAIMode.IDLE;
		currentTarget = null;
		furnaces.clear();
	}

	@Override
	public void updateTask() {
		switch (currentTask) {
		case IDLE:
			
			break;
		case THINKING:
			
			break;
		case WALKING:
			
			break;
		case INTERACTING:
			
			break;
		default:
			break;
		}
	}

	public void setToMainHand(int slot) {
		ItemStack oldMain = inventory.removeStackFromSlot(0);
		ItemStack newMain = inventory.removeStackFromSlot(slot);
		inventory.setInventorySlotContents(0, newMain);
		inventory.setInventorySlotContents(slot, oldMain);
	}

	public boolean hasMoreOf(ItemStack stack) {
		for (int i = 0; i < inventory.getSizeInventory(); i++) {
			if (inventory.getStackInSlot(i).isItemEqual(stack))
				return true;
		}
		return false;
	}

	protected void gainExperience(ItemStack removed) {
		if (!servant.world.isRemote) {
			int i = removed.getCount();
			float f = FurnaceRecipes.instance().getSmeltingExperience(removed);

			if (f == 0.0F) {
				i = 0;
			} else if (f < 1.0F) {
				int j = MathHelper.floor((float) i * f);

				if (j < MathHelper.ceil((float) i * f) && Math.random() < (double) ((float) i * f - (float) j)) {
					++j;
				}

				i = j;
			}

			while (i > 0) {
				int k = EntityXPOrb.getXPSplit(i);
				i -= k;
				servant.world.spawnEntity(
						new EntityCustomXP(servant.world, servant.posX, servant.posY + 0.5D, servant.posZ + 0.5D, k));
			}
		}

	}
	
	public static enum EnumCookAIMode {
		IDLE,
		THINKING,
		WALKING,
		INTERACTING;
	}

}
