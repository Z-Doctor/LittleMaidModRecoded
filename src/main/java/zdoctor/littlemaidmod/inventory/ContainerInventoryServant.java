package zdoctor.littlemaidmod.inventory;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.EntityEquipmentSlot.Type;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import zdoctor.littlemaidmod.entity.EntityServantBase;

public class ContainerInventoryServant implements IInventory {
	protected final NonNullList<ItemStack> mainInventory = NonNullList.<ItemStack>withSize(17, ItemStack.EMPTY);

	private final List<NonNullList<ItemStack>> allInventories;

	public EntityServantBase servant;

	private int timesChanged;

	public ContainerInventoryServant(EntityServantBase servantIn) {
		allInventories = Arrays.<NonNullList<ItemStack>>asList(mainInventory);
		servant = servantIn;
	}
	
	public int getTotalArmorValue() {
		Iterator<ItemStack> irr = getArmorInventory().iterator();
		int armor = 0;
		while (irr.hasNext()) {
			Item item = irr.next().getItem();
			if (item instanceof ItemArmor)
				armor += MathHelper.floor(((ItemArmor) item).damageReduceAmount);
			// System.out.println(armor);
		}
		return armor;
	}

	public Iterable<ItemStack> getArmorInventory() {
		return this.servant.getArmorInventoryList();
	}

	@Override
	public String getName() {
		return hasCustomName() ? "Inside " + servant.getCustomNameTag() + "'s skirt"
				: I18n.format("container.insideskirt");
	}

	@Override
	public boolean hasCustomName() {
		return servant.hasCustomName();
	}

	@Override
	public ITextComponent getDisplayName() {
		return (ITextComponent) (this.hasCustomName() ? new TextComponentString(this.getName())
				: new TextComponentTranslation(this.getName(), new Object[0]));
	}

	@Override
	public int getSizeInventory() {
		// In hand, armor slots, inventory
		return 1 + 3 + getMainInventorySize();
	}

	public int getMainInventorySize() {
		return mainInventory.size();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.mainInventory) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	// index - getMainInventorySize() - 1; -1 is to account for the item in hand
	@Override
	public ItemStack getStackInSlot(int index) {
		ItemStack contents = ItemStack.EMPTY;

		if (index < 0)
			return contents;
		if (index == 0) {
			contents = servant.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
		} else if (index > getMainInventorySize() && index - getMainInventorySize() - 1 < 3) {
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				if (slot.getSlotType() == Type.ARMOR && 2 - slot.getIndex() == index - getMainInventorySize() - 1)
					contents = servant.getItemStackFromSlot(slot);
		} else if (index < getMainInventorySize() + 1) {
			contents = mainInventory.get(index - 1);
		}
		return contents;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		ItemStack contents = ItemStack.EMPTY;

		if (index < 0)
			return contents;

		if (index == 0) {
			contents = servant.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND).splitStack(count);
		} else if (index > getMainInventorySize() && index - getMainInventorySize() - 1 < 3) {
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				if (slot.getSlotType() == Type.ARMOR && 2 - slot.getIndex() == index - getMainInventorySize() - 1)
					contents = servant.getItemStackFromSlot(slot).splitStack(count);
		} else if (index < getMainInventorySize() + 1) {
			contents = mainInventory.get(index - 1).splitStack(count);
		}
		this.markDirty();
		return contents;
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		ItemStack contents = ItemStack.EMPTY;

		if (index < 0)
			return contents;

		if (index == 0) {
			contents = servant.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
			servant.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		} else if (index > getMainInventorySize() && index - getMainInventorySize() - 1 < 3) { // -1 for in hand
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				if (slot.getSlotType() == Type.ARMOR && 2 - slot.getIndex() == index - getMainInventorySize() - 1) {
					contents = servant.getItemStackFromSlot(slot);
					servant.setItemStackToSlot(slot, ItemStack.EMPTY);
				}
		} else if (index < getMainInventorySize() + 1) {
			int slot = index - 1; // -1 for item in hand
			contents = mainInventory.get(slot);
			mainInventory.set(slot, ItemStack.EMPTY);
		}
		this.markDirty();
		return contents;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		// System.out.println("Set Index: " + index + ", " + stack);

		if (index < 0)
			return;

		if (index == 0) {
			servant.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
		} else if (index > getMainInventorySize() && index - getMainInventorySize() - 1 < 3) {
			for (EntityEquipmentSlot slot : EntityEquipmentSlot.values())
				if (slot.getSlotType() == Type.ARMOR && 2 - slot.getIndex() == index - getMainInventorySize() - 1)
					servant.setItemStackToSlot(slot, stack);
		} else if (index < getMainInventorySize() + 1)
			mainInventory.set(index - 1, stack);
		this.markDirty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void markDirty() {
		++this.timesChanged;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return !servant.isDead && servant.hasContract() && servant.getOwnerID().equals(player.getUniqueID());
	}

	@Override
	public void openInventory(EntityPlayer player) {
		servant.openInventory(player);
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		servant.closeInventory(player);
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {

	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		for (List<ItemStack> list : this.allInventories) {
			list.clear();
		}
	}

	public NBTTagList writeToNBT(NBTTagList nbtTagListIn) {
		for (int i = 0; i < this.mainInventory.size(); ++i) {
			if (!((ItemStack) this.mainInventory.get(i)).isEmpty()) {
				NBTTagCompound nbttagcompound = new NBTTagCompound();
				nbttagcompound.setByte("Slot", (byte) i);
				((ItemStack) this.mainInventory.get(i)).writeToNBT(nbttagcompound);
				nbtTagListIn.appendTag(nbttagcompound);
			}
		}

		return nbtTagListIn;
	}

	public void readFromNBT(NBTTagList nbtTagListIn) {
		clear();

		for (int i = 0; i < nbtTagListIn.tagCount(); ++i) {
			NBTTagCompound nbttagcompound = nbtTagListIn.getCompoundTagAt(i);
			int j = nbttagcompound.getByte("Slot") & 255;
			ItemStack itemstack = new ItemStack(nbttagcompound);

			if (!itemstack.isEmpty() && j >= 0 && j < this.mainInventory.size())
				this.mainInventory.set(j, itemstack);

		}
	}

	public ItemStack addItem(ItemStack stack) {
		ItemStack itemstack = stack.copy();

		for (int i = 0; i <= mainInventory.size(); ++i) {
			ItemStack itemstack1 = this.getStackInSlot(i);

			if (itemstack1.isEmpty()) {
				this.setInventorySlotContents(i, itemstack);
				this.markDirty();
				return ItemStack.EMPTY;
			}

			if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
				int j = Math.min(this.getInventoryStackLimit(), itemstack1.getMaxStackSize());
				int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

				if (k > 0) {
					itemstack1.grow(k);
					itemstack.shrink(k);

					if (itemstack.isEmpty()) {
						this.markDirty();
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (itemstack.getCount() != stack.getCount()) {
			this.markDirty();
		}

		return itemstack;
	}

	public void damageArmor(float damage) {
		damage = damage / 3.0F;

		if (damage < 1.0F) {
			damage = 1.0F;
		}
		final float finalDamage = damage;

		servant.getArmorInventoryList().forEach(itemstack -> {
			if (itemstack.getItem() instanceof ItemArmor)
				itemstack.damageItem((int) finalDamage, servant);
		});
		this.markDirty();

	}

	public NonNullList<ItemStack> getInventory() {
		return mainInventory;
	}

	public void swapStacks(int index, int index1) {
		ItemStack slot = removeStackFromSlot(index);
		ItemStack slot1 = removeStackFromSlot(index1);
		
		setInventorySlotContents(index, slot1);
		setInventorySlotContents(index1, slot);
		
		if(index == 0 || index1 == 0)
			servant.resetActiveHand();
	}
	
	public void setActiveStack(int index) {
		if(index != 0) {
			swapStacks(0, index);
			servant.resetActiveHand();
		}
		servant.setActiveHand(EnumHand.MAIN_HAND);
	}

}
