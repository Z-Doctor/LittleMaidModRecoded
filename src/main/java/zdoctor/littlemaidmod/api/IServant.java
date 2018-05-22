package zdoctor.littlemaidmod.api;

import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.DimensionType;
import zdoctor.littlemaidmod.util.EnumMood;

public interface IServant extends IRangedAttackMob {
	// TODO go through and fix and remoce unused ones
	public EntityLivingBase getMaster();

	public UUID getOwnerID();

	public boolean hasContract();

	public void setWaiting(boolean b);

	public boolean isWaiting();

	public DimensionType getHomeDimension();

	void closeInventory(EntityPlayer player);

	void openInventory(EntityPlayer player);

	boolean canPickupItem(ItemStack itemstack);

	EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem);

	int getExpBooster();

	float getExperience();

	int getLevel();

	boolean shouldShowArmor(int slot);

	boolean shouldComeToAid();

	double getRespondDistance();

	boolean formContract(EntityPlayer player, EnumHand hand);

	void setBegging(boolean b);

	boolean isBegging();

	boolean isFavorite(ItemStack itemstack);

	boolean canTalkToStangers();

	boolean give(EntityPlayer player, ItemStack itemstack, int count);

	boolean swimmingEnabled();

	boolean acceptGiftsFromStangers();

	void onOwnerDamage(DamageSource damageSrc, float damageAmount);

	void onDamage(DamageSource damageSrc, float damageAmount);

	void addExperience(float amount);

	int getExpBoosterLimit();

	void setExpBooster(int booster);

	void onLevelUp(int currentLevel);

	void onPlayerGift(EntityPlayer player, ItemStack gift);

	void playMoodEffect(EnumMood mood);

	void addToInventory(ItemStack itemstack);

	boolean isModeChanger(ItemStack itemstack);

	boolean isBindingItem(ItemStack itemstack);

	void onContractFormed(EntityPlayer player);

	boolean updateMode(ItemStack changer, ItemStack activeItem);

	String getStatus();

	boolean isFreedomMode();

	void setFreedomMode(boolean b);

	boolean isSentryMode();

	void setSentryMode(boolean b);

	void setArmorVisibility(int option, boolean visible);

	boolean isVisiblityEnabled(int option);

	void collideWithServant(Entity entity);

	void setExperience(float exp);
	
	ItemStack findAmmo();

}
