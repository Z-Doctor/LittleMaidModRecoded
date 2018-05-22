package zdoctor.littlemaidmod.entity;

import java.util.UUID;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import zdoctor.littlemaidmod.MaidModMain;
import zdoctor.littlemaidmod.client.gui.handler.MaidGuiHandler;
import zdoctor.littlemaidmod.util.EnumMood;
import zdoctor.littlemaidmod.util.ServantExperienceHelper;

public class EntityServant extends EntityServantBase {

	protected boolean waitingForGui = false;

	public EntityServant(World worldIn) {
		super(worldIn);
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5300002D);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (!hasContract()) {
			if (isBindingItem(itemstack))
				return formContract(player, hand);
		}

		if (!player.isSneaking() && (!hasContract() || (!isOwner(player) && canTalkToStangers()))) {
			if (isFavorite(itemstack)) {
				give(player, itemstack, 1);
				return true;
			}
		}

		if (isOwner(player)) {
			if (player.isSneaking())
				return super.processInteract(player, hand);
			else if (isModeChanger(itemstack)) {
				boolean consumeItem = updateMode(itemstack, getItemStackFromSlot(EntityEquipmentSlot.MAINHAND));
				if (!player.capabilities.isCreativeMode) {
					itemstack.shrink(1);
				}
				return true;
			} else if (isFavorite(itemstack)) {
				give(player, itemstack, 1);
				return true;
			}
			openInventory(player);
			return true;
		}
		return true;
	}

	@Override
	public boolean isBindingItem(ItemStack itemstack) {
		return itemstack.getItem() == Items.CAKE;
	}

	@Override
	public boolean isModeChanger(ItemStack itemstack) {
		return itemstack.getItem() == Items.SUGAR;
	}

	/**
	 * You should change your mode here and call.Boolean is whether or not the
	 * change is consumed.
	 */
	@Override
	public boolean updateMode(ItemStack changer, ItemStack activeItem) {
		setWaiting(!isWaiting());
		return true;
	}

	@Override
	public boolean canTalkToStangers() {
		return true;
	}

	@Override
	public boolean swimmingEnabled() {
		return true;
	}

	@Override
	public boolean acceptGiftsFromStangers() {
		return true;
	}

	@Override
	public boolean give(EntityPlayer player, ItemStack itemstack, int count) {
		if (count > itemstack.getCount())
			count = itemstack.getCount();

		ItemStack copy = servantInventory.addItem(itemstack.splitStack(count));
		int amountTaken = Math.abs(count - copy.getCount());

		if (!player.capabilities.isCreativeMode) {
			itemstack.shrink(amountTaken);
		}

		copy.setCount(count);

		if (player != null && amountTaken > 0)
			onPlayerGift(player, copy);

		// TODO Event when player gives gift

		return amountTaken > 0;
	}

	@Override
	public void onPlayerGift(EntityPlayer player, ItemStack gift) {
		playMoodEffect(EnumMood.HAPPY);
	}

	@Override
	public void addToInventory(ItemStack itemstack) {
		servantInventory.addItem(itemstack);
	}

	@Override
	public boolean isFavorite(ItemStack itemstack) {
		return itemstack.getItem() == Items.SUGAR;
	}

	@Override
	public boolean formContract(EntityPlayer player, EnumHand hand) {
		ItemStack itemstack = player.getHeldItem(hand);
		if (hasContract() || !isBindingItem(itemstack))
			return false;

		if (!player.capabilities.isCreativeMode) {
			itemstack.shrink(1);
		}

		if (!this.world.isRemote) {
			if (!net.minecraftforge.event.ForgeEventFactory.onAnimalTame(this, player)) {
				this.setTamedBy(player);
				this.navigator.clearPath();
				this.setAttackTarget((EntityLivingBase) null);
				this.setHealth(getMaxHealth());
				this.world.setEntityState(this, (byte) 7);
				onContractFormed(player);
			} else {
				this.playTameEffect(false);
				this.world.setEntityState(this, (byte) 6);
			}
		}

		return true;
	}

	@Override
	public void onContractFormed(EntityPlayer player) {
		this.playTameEffect(true);

	}

	@Override
	public boolean canPickupItem(ItemStack itemstack) {
		return hasContract();
	}

	@Override
	public int getTotalArmorValue() {
		return super.getTotalArmorValue();
	}

	@Override
	protected void damageArmor(float damage) {
		servantInventory.damageArmor(damage);
	}

	@Override
	public EnumHand getActiveHand() {
		return EnumHand.MAIN_HAND;
	}

	@Override
	public void onLevelUp(int currentLevel) {
		if (currentLevel % 20 == 0)
			addToInventory(new ItemStack(Items.NAME_TAG));

		if (currentLevel % 50 == 0)
			addToInventory(new ItemStack(Items.EMERALD, currentLevel / 50));

		if (currentLevel % 100 == 0)
			addToInventory(new ItemStack(Items.DIAMOND, currentLevel / 100));

		if (currentLevel % 150 == 0)
			addToInventory(new ItemStack(Items.NETHER_STAR, 1));
		if (currentLevel % 5 == 0) {
			float f = getExperience() > 30 ? 1.0F : getExperience() / 30.0F;
			this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_PLAYER_LEVELUP,
					this.getSoundCategory(), f * 0.75F, 1.0F);
		}
	}

	@Override
	public boolean shouldShowArmor(int slot) {
		// TODO Implement a listner
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		if (isDead || !hasContract() || !isOwner(player))
			return;
		if (!isWaiting())
			waitingForGui = true;
		setWaiting(true);
		if (world.isRemote)
			return;
		net.minecraftforge.fml.common.network.internal.FMLNetworkHandler.openGui((EntityPlayer) getOwner(),
				MaidModMain.INSTANCE, MaidGuiHandler.MAID_INVENTORY, world, this.getEntityId(), -1, -1);
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		if (waitingForGui)
			setWaiting((waitingForGui = false));
	}

	@Override
	public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem) {
		if (droppedItem.isEmpty()) {
			return null;
		} else {
			double d0 = this.posY - 0.30000001192092896D + (double) this.getEyeHeight();
			EntityItem entityitem = new EntityItem(this.world, this.posX, d0, this.posZ, droppedItem);
			entityitem.setPickupDelay(40);

			if (traceItem) {
				entityitem.setThrower(this.getName());
			}

			if (dropAround) {
				float f = this.rand.nextFloat() * 0.5F;
				float f1 = this.rand.nextFloat() * ((float) Math.PI * 2F);
				entityitem.motionX = (double) (-MathHelper.sin(f1) * f);
				entityitem.motionZ = (double) (MathHelper.cos(f1) * f);
				entityitem.motionY = 0.20000000298023224D;
			} else {
				float f2 = 0.3F;
				entityitem.motionX = (double) (-MathHelper.sin(this.rotationYaw * 0.017453292F)
						* MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
				entityitem.motionZ = (double) (MathHelper.cos(this.rotationYaw * 0.017453292F)
						* MathHelper.cos(this.rotationPitch * 0.017453292F) * f2);
				entityitem.motionY = (double) (-MathHelper.sin(this.rotationPitch * 0.017453292F) * f2 + 0.1F);
				float f3 = this.rand.nextFloat() * ((float) Math.PI * 2F);
				f2 = 0.02F * this.rand.nextFloat();
				entityitem.motionX += Math.cos((double) f3) * (double) f2;
				entityitem.motionY += (double) ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
				entityitem.motionZ += Math.sin((double) f3) * (double) f2;
			}

			this.world.spawnEntity(entityitem);

			return entityitem;
		}
	}

	@Override
	public boolean shouldComeToAid() {
		return false;
	}

	@Override
	public double getRespondDistance() {
		return 144D;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
	}

	@Override
	public void onDeath(DamageSource cause) {
		super.onDeath(cause);
		if (!world.isRemote)
			InventoryHelper.dropInventoryItems(world, this, servantInventory);
	}

	// Is Methods
	@Override
	public boolean isWaiting() {
		return isSitting();
	}

	@Override
	public boolean hasContract() {
		return isTamed();
	}

	@Override
	public boolean isSentryMode() {
		return this.dataManager.get(SENTRY);
	}

	@Override
	public boolean isBegging() {
		return this.dataManager.get(BEGGING);
	}

	@Override
	public boolean isFreedomMode() {
		return this.dataManager.get(FREEDOM);
	}

	@Override
	public boolean isSneaking() {
		return this.dataManager.get(SNEAKING);
	}

	@Override
	public boolean isVisiblityEnabled(int option) {
		if (option > 4 || option < 1) {
			System.out.println("Invalid armor option");
			return false;
		}
		option = 1 << (option - 1);
		return (this.dataManager.get(ARMOR_VISIBLITY) & option) == option;
	}

	// Servant Getters
	@Override
	public EntityLivingBase getMaster() {
		return getOwner();
	}

	@Override
	public UUID getOwnerID() {
		return getOwnerId();
	}

	@Override
	protected int getExperiencePoints(EntityPlayer player) {
		return 0;
	}

	@Override
	public int getLevel() {
		return ServantExperienceHelper.getLevelFromExp(getExperience());
	}

	@Override
	public float getExperience() {
		return this.dataManager.get(CURRENT_EXP);
	}

	@Override
	public int getExpBooster() {
		return this.dataManager.get(EXP_BOOSTER);
	}

	@Override
	public int getExpBoosterLimit() {
		return ServantExperienceHelper.getBoosterLimit(getLevel());
	}

	@Override
	public DimensionType getHomeDimension() {
		return DimensionType.OVERWORLD;
	}

	@Override
	public void setSentryMode(boolean b) {
		this.dataManager.set(SENTRY, b);
	}

	@Override
	public void setFreedomMode(boolean b) {
		this.dataManager.set(FREEDOM, b);
	}

	@Override
	public void setExpBooster(int booster) {
		if (world.isRemote)
			return;
		this.dataManager.set(EXP_BOOSTER, booster);
	}

	@Override
	public void setWaiting(boolean b) {
		if (b) {
			setSneaking(!b);
			resetActiveHand();
		}
		this.setSitting(b);
	}

	@Override
	public void setSneaking(boolean sneaking) {
		this.dataManager.set(SNEAKING, sneaking);
		if (sneaking && !getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SNEAKING_SPEED))
			getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(SNEAKING_SPEED);
		else if (!sneaking && getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).hasModifier(SNEAKING_SPEED))
			getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(SNEAKING_SPEED);
	}

	@Override
	public void setBegging(boolean b) {
		this.dataManager.set(BEGGING, b);
	}

	@Override
	public void setSwingingArms(boolean swingingArms) {
		this.dataManager.set(SWINGING_ARMS, swingingArms);
	}

	@Override
	public void setTamedBy(EntityPlayer player) {
		// To stop mc achievement
		this.setTamed(true);
		this.setOwnerId(player.getUniqueID());
	}

	// Vanilla Overrides
	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return null;
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return false;
	}

	@Override
	protected void damageEntity(DamageSource damageSrc, float damageAmount) {

		if (!this.isEntityInvulnerable(damageSrc)) {
			if (damageSrc.getTrueSource() instanceof EntityTameable) {
				if (isOwner(((EntityTameable) damageSrc.getTrueSource()).getOwner()))
					return;
			}
			damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
			if (damageAmount <= 0)
				return;
			damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
			damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
			float f = damageAmount;
			damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
			this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
			damageAmount = net.minecraftforge.common.ForgeHooks.onLivingDamage(this, damageSrc, damageAmount);

			if (damageAmount != 0.0F) {
				float f1 = this.getHealth();
				this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
				this.setHealth(f1 - damageAmount); // Forge: moved to fix MC-121048
				this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);

				if (damageSrc.getImmediateSource() instanceof EntityLivingBase
						&& isOwner((EntityLivingBase) damageSrc.getTrueSource()))
					onOwnerDamage(damageSrc, damageAmount);
				else
					onDamage(damageSrc, damageAmount);
			}
		}
	}

	@Override
	public void travel(float strafe, float vertical, float forward) {

		super.travel(strafe, vertical, forward);
		if (this.isOnLadder()) {
			float f9 = 0.15F;
			this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
			this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
			this.fallDistance = 0.0F;

			if (this.motionY < -0.15D) {
				this.motionY = -0.15D;
			}

			if (isSneaking() && this.motionY < 0.0D) {
				this.motionY = 0.0D;
			}
		}
	}

	@Override
	public void addExperience(float amount) {
		if (!hasContract() || world.isRemote) {
			return;
		}

		int currentLevel = getLevel();
		float maidExperience = getExperience();
		if (maidExperience > 0)
			amount *= getExpBooster();

		maidExperience += amount;

		if (maidExperience < ServantExperienceHelper.getRequiredExpToLevel(currentLevel)) {
			maidExperience = ServantExperienceHelper.getRequiredExpToLevel(currentLevel);
		}

		if (maidExperience > ServantExperienceHelper.getRequiredExpToLevel(ServantExperienceHelper.EXP_FUNCTION_MAX)) {
			maidExperience = ServantExperienceHelper.getRequiredExpToLevel(ServantExperienceHelper.EXP_FUNCTION_MAX);
		}

		setExperience(maidExperience);
		while (maidExperience >= ServantExperienceHelper.getRequiredExpToLevel(currentLevel + 1)) {
			setExperience(maidExperience);
			onLevelUp(++currentLevel);
		}

	}

	@Override
	public void onOwnerDamage(DamageSource damageSrc, float damageAmount) {

	}

	@Override
	public void onDamage(DamageSource damageSrc, float damageAmount) {

	}

	@Override
	public void playMoodEffect(EnumMood mood) {
		EnumParticleTypes enumparticletypes = null;
		switch (mood) {
		case LOVE:
			enumparticletypes = EnumParticleTypes.HEART;
			break;
		case HAPPY:
			enumparticletypes = EnumParticleTypes.NOTE;
			break;
		case ANGRY:
			enumparticletypes = EnumParticleTypes.VILLAGER_ANGRY;
			break;
		case RELAXED:
			enumparticletypes = EnumParticleTypes.VILLAGER_HAPPY;
			break;
		case CONFUSED:
			enumparticletypes = EnumParticleTypes.SMOKE_NORMAL;
			break;
		case THINKING:
			enumparticletypes = EnumParticleTypes.ENCHANTMENT_TABLE;
			break;
		case BUSY:
			enumparticletypes = EnumParticleTypes.REDSTONE;
			break;
		default:
			break;

		}

		for (int i = 0; i < 1 + getRNG().nextInt(7); ++i) {
			double d0 = getRNG().nextGaussian() * 0.02D;
			double d1 = getRNG().nextGaussian() * 0.02D;
			double d2 = getRNG().nextGaussian() * 0.02D;
			this.world.spawnParticle(enumparticletypes,
					this.posX + (double) (getRNG().nextFloat() * this.width * 2.0F) - (double) this.width,
					this.posY + (double) this.height,
					this.posZ + (double) (getRNG().nextFloat() * this.width * 2.0F) - (double) this.width, d0, d1, d2);
		}
	}

	@Override
	public String getStatus() {
		return "Idle";
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float distanceFactor) {
		// ItemStack arrow =
		// if (arrow.isEmpty())
		// return;
		// ItemArrow itemarrow = (ItemArrow) arrow.getItem();
		//
		// EntityArrow entityarrow = itemarrow.createArrow(world, arrow, this);
		//
		// double d0 = target.posX - this.posX;
		// double d1 = target.getEntityBoundingBox().minY + (double) (target.height /
		// 3.0F) - entityarrow.posY;
		// double d2 = target.posZ - this.posZ;
		// double d3 = (double) MathHelper.sqrt(d0 * d0 + d2 * d2);
		// entityarrow.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F,
		// (float) (14 - this.world.getDifficulty().getDifficultyId() * 4));
		// this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F /
		// (this.getRNG().nextFloat() * 0.4F + 0.8F));
		// this.world.spawnEntity(entityarrow);
		//
		// ItemStack inHand = getHeldItemMainhand();
		// inHand.getItem().onItemUseFinish(inHand, world, this);
		// inHand.attemptDamageItem(1, rand, null);
		//
		// int enchant =
		// net.minecraft.enchantment.EnchantmentHelper.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY,
		// inHand);
		// return enchant <= 0 ? false : this.getClass() == ItemArrow.class;
		// ItemBow
		// arrow.shrink(1);
		ItemStack inHand = getHeldItemMainhand();
		// inHand.getItem().onItemUseFinish(inHand, world, this);
		// inHand.attemptDamageItem(1, rand, null);

		ItemStack arrow = findAmmo();

		if (!arrow.isEmpty()) {
			float f = distanceFactor;

			if ((double) f >= 0.1D) {
				int enchant = net.minecraft.enchantment.EnchantmentHelper
						.getEnchantmentLevel(net.minecraft.init.Enchantments.INFINITY, inHand);

				boolean flag1 = arrow.getItem() instanceof ItemArrow && enchant <= 0 ? false
						: arrow.getItem().getClass() == ItemArrow.class;

				if (!world.isRemote) {
					ItemArrow itemarrow = (ItemArrow) (arrow.getItem() instanceof ItemArrow ? arrow.getItem()
							: Items.ARROW);
					EntityArrow entityarrow = itemarrow.createArrow(world, arrow, this);
					entityarrow.pickupStatus = PickupStatus.ALLOWED;
					entityarrow.shoot(this, this.rotationPitch, this.rotationYaw, 0.0F, f * 3.0F, 0.0F);

					if (f == 1.0F) {
						entityarrow.setIsCritical(true);
					}

					int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, inHand);

					if (j > 0) {
						entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
					}

					int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, inHand);

					if (k > 0) {
						entityarrow.setKnockbackStrength(k);
					}

					if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, inHand) > 0) {
						entityarrow.setFire(100);
					}

					inHand.damageItem(1, this);

					if (flag1) {// && (arrow.getItem() == Items.SPECTRAL_ARROW || arrow.getItem() ==
								// Items.TIPPED_ARROW)) {
						entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
					}

					world.spawnEntity(entityarrow);
				}

				world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ARROW_SHOOT,
						SoundCategory.PLAYERS, 1.0F, 1.0F / (rand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

				if (!flag1) {
					arrow.shrink(1);
				}

				// this.addStat(StatList.getObjectUseStats(this));
			}
		}

	}

}
