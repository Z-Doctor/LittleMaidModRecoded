package zdoctor.littlemaidmod.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ContainerMaidInventory extends Container {

	public final EntityPlayer owner;
	public final IInventory playerInventory;
	public final EntityServantBase servant;
	public final ContainerInventoryServant littlemaidInventory;
	public final int numRows;

	public ContainerMaidInventory(EntityServantBase servantIn) {
		owner = (EntityPlayer) servantIn.getOwner();
		playerInventory = owner.inventory;
		servant = servantIn;
		littlemaidInventory = servantIn.servantInventory;
		numRows = MathHelper.ceil(littlemaidInventory.getMainInventorySize() / 9F);

		int slotId = 0;
		// Adds items from maid inventory
		addSlotToContainer(new Slot(littlemaidInventory, slotId++, 8, 76));

		// Adds items from maid inventory
//		System.out.println(littlemaidInventory.getSizeInventory() +  " Rows: " + numRows);
		for (int ly = 0; ly < numRows; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				if (lx != 0 || ly > 0)
					addSlotToContainer(new Slot(littlemaidInventory, slotId++, 8 + lx * 18, 76 + ly * 18));
			}
		}

		// Adds items from maid armor; no helmet (yet)
		final EntityEquipmentSlot[] armorIndex = new EntityEquipmentSlot[] { EntityEquipmentSlot.FEET,
				EntityEquipmentSlot.LEGS, EntityEquipmentSlot.CHEST };
		for (int j = 2; j >= 0; j--) {
			int index = j;
			this.addSlotToContainer(new Slot(littlemaidInventory, slotId++, 8, (2 - j) * 18 + 8) {
				public int getSlotStackLimit() {
					return 1;
				}

				public boolean isItemValid(ItemStack par1ItemStack) {
					if (par1ItemStack == ItemStack.EMPTY)
						return false;
					return par1ItemStack.getItem().isValidArmor(par1ItemStack, armorIndex[index], owner);
				}

				@SideOnly(Side.CLIENT)
				public String getSlotTexture() {
					return ItemArmor.EMPTY_SLOT_NAMES[index];
				}
			});
		}

		slotId = 0;

		int lyoffset = (numRows - 4) * 18 + 59;
		// Adds items from player hot bar to hot bar of gui
		for (int lx = 0; lx < 9; lx++) {
			addSlotToContainer(new Slot(playerInventory, slotId++, 8 + lx * 18, 161 + lyoffset));
		}

		// Adds items from player backpack
		for (int ly = 0; ly < 3; ly++) {
			for (int lx = 0; lx < 9; lx++) {
				addSlotToContainer(new Slot(playerInventory, slotId++, 8 + lx * 18, 103 + ly * 18 + lyoffset));
			}
		}
	}

	@Override
	public void putStackInSlot(int slotID, ItemStack stack) {
		// System.out.println(slotID + ": " + stack.getItem());
		super.putStackInSlot(slotID, stack);
		checkAchievements();
	}

	// TODO add achievements
	protected void checkAchievements() {
		// boolean flag = true;
		// Slot slot;
		// Item item;
		// flag &= (slot = getSlot(54)).getHasStack() && (item =
		// slot.getStack().getItem()) instanceof ItemArmor &&
		// ((ItemArmor)item).getArmorMaterial() == ArmorMaterial.DIAMOND;
		// flag &= (slot = getSlot(55)).getHasStack() && (item =
		// slot.getStack().getItem()) instanceof ItemArmor &&
		// ((ItemArmor)item).getArmorMaterial() == ArmorMaterial.DIAMOND;
		// flag &= (slot = getSlot(56)).getHasStack() && (item =
		// slot.getStack().getItem()) instanceof ItemArmor &&
		// ((ItemArmor)item).getArmorMaterial() == ArmorMaterial.DIAMOND;
		//
		// if (flag && !owner.worldObj.isRemote)
		// owner.getMaidMasterEntity().triggerAchievement(LMMNX_Achievements.ac_Overprtct);
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return !servant.isDead && servant.hasContract() && servant.isOwner(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack litemstack = ItemStack.EMPTY;
		Slot slot = (Slot) inventorySlots.get(index);
		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();

			litemstack = itemstack1.copy();
			int lline = numRows * 9;
			if (index < lline) {
				if (!this.mergeItemStack(itemstack1, lline, lline + 36, true)) {
					return ItemStack.EMPTY;
				}
			} else if (index >= lline && index < lline + 36) {
				if (!this.mergeItemStack(itemstack1, 0, lline, false)) {
					return ItemStack.EMPTY;
				}
			} else {
				if (!this.mergeItemStack(itemstack1, 0, lline + 36, false)) {
					return ItemStack.EMPTY;
				}
			}
			if (itemstack1.isEmpty()) {
				slot.putStack(itemstack1);
			} else {
				slot.onSlotChanged();
			}
		}
		return litemstack;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);
		littlemaidInventory.closeInventory(playerIn);
	}

	@Override
	public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
		return true;
	}

}
