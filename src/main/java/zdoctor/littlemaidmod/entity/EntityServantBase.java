package zdoctor.littlemaidmod.entity;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityArrow.PickupStatus;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.CooldownTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import zdoctor.littlemaidmod.api.ICollideWithServant;
import zdoctor.littlemaidmod.api.IServant;
import zdoctor.littlemaidmod.inventory.ContainerInventoryServant;

public abstract class EntityServantBase extends EntityTameable implements IServant {

	public static final DataParameter<Byte> SERVANT_STATUS = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.BYTE);
	public static final DataParameter<NBTTagCompound> WATCHER_POTION_EFFECTS = EntityDataManager
			.createKey(EntityServantBase.class, DataSerializers.COMPOUND_TAG);
	public static final DataParameter<Boolean> FREEDOM = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> SENTRY = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.BOOLEAN);
	public static final DataParameter<Integer> ARMOR_VISIBLITY = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.VARINT);

	public static final DataParameter<Boolean> BEGGING = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> SNEAKING = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.BOOLEAN);
	public static final DataParameter<Boolean> SWINGING_ARMS = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.BOOLEAN);

	public static final DataParameter<Float> CURRENT_EXP = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.FLOAT);
	public static final DataParameter<Integer> EXP_BOOSTER = EntityDataManager.createKey(EntityServantBase.class,
			DataSerializers.VARINT);

	protected static final UUID maidUUID = UUID.fromString("e2361272-644a-3028-8416-8536667f0efb");
	protected static final UUID maidUUIDSneak = UUID.fromString("5649cf91-29bb-3a0c-8c31-b170a1045560");
	protected static AttributeModifier COMBAT_SPEED = (new AttributeModifier(maidUUID, "Combat speed boost", 0.07D, 0))
			.setSaved(false);
	protected static AttributeModifier AXE_AMPLIFIER = (new AttributeModifier(maidUUID, "Axe Attack boost", 0.5D, 1))
			.setSaved(false);
	protected static AttributeModifier SNEAKING_SPEED = (new AttributeModifier(maidUUIDSneak, "Sneking speed ampd",
			-0.4D, 2)).setSaved(false);

	public ContainerInventoryServant servantInventory;

	public int xpCooldown;

	protected CooldownTracker cooldownTracker = createCooldownTracker();

	public EntityServantBase(World worldIn) {
		super(worldIn);
		setTamed(false);
		setCanPickUpLoot(true);
		this.setSize(0.6F, 1.4F);
		servantInventory = new ContainerInventoryServant(this);
	}

	protected CooldownTracker createCooldownTracker() {
		return new CooldownTracker();
	}

	protected void updateSize() {
		// TODO Adjust
		float f;
		float f1;

		if (this.isElytraFlying()) {
			f = 0.6F;
			f1 = 0.6F;
		} else if (this.isPlayerSleeping()) {
			f = 0.2F;
			f1 = 0.2F;
		} else if (this.isSneaking()) {
			f = 0.6F;
			f1 = 1.15F;
		} else {
			f = 0.6F;
			f1 = 1.4F;
		}

		if (f != this.width || f1 != this.height) {
			AxisAlignedBB axisalignedbb = this.getEntityBoundingBox();
			axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ,
					axisalignedbb.minX + (double) f, axisalignedbb.minY + (double) f1, axisalignedbb.minZ + (double) f);

			if (!this.world.collidesWithAnyBlock(axisalignedbb)) {
				this.setSize(f, f1);
				this.width = f;
				this.height = f1;
			}
		}
	}

	// load
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(WATCHER_POTION_EFFECTS, new NBTTagCompound());

		this.dataManager.register(FREEDOM, Boolean.valueOf(false));
		this.dataManager.register(SENTRY, Boolean.valueOf(false));
		this.dataManager.register(BEGGING, Boolean.valueOf(false));
		this.dataManager.register(SNEAKING, Boolean.valueOf(false));
		this.dataManager.register(SWINGING_ARMS, Boolean.valueOf(false));
		this.dataManager.register(ARMOR_VISIBLITY, 15);

		this.dataManager.register(CURRENT_EXP, 0F);
		this.dataManager.register(EXP_BOOSTER, 1);

	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_SPEED);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.LUCK);
	}

	@Override
	protected void updateEquipmentIfNeeded(EntityItem itemEntity) {
		ItemStack itemstack = itemEntity.getItem();
		Item item = itemstack.getItem();

		if (item instanceof ItemArmor) {
			// TODO check if it should put on
		}

		if (!canPickupItem(itemstack))
			return;

		ItemStack itemstack1 = servantInventory.addItem(itemstack);
		onItemPickup(itemEntity, itemstack.getCount() - itemstack1.getCount());
		if (itemstack1.isEmpty()) {
			itemEntity.setDead();
		} else {
			itemstack.setCount(itemstack1.getCount());
		}

	}

	@Override
	public void notifyDataManagerChange(DataParameter<?> key) {
		super.notifyDataManagerChange(key);
		if (world.isRemote && WATCHER_POTION_EFFECTS.equals(key)) {
			NBTTagList nbttaglist = this.dataManager.get(WATCHER_POTION_EFFECTS).getTagList("ActiveEffects", 10);
			clearActivePotions();
			for (int i = 0; i < nbttaglist.tagCount(); ++i) {
				NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
				PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

				if (potioneffect != null) {
					addPotionEffect(potioneffect);
				}
			}
		}
	}

	@Override
	protected void blockUsingShield(EntityLivingBase p_190629_1_) {
		super.blockUsingShield(p_190629_1_);

		if (p_190629_1_.getHeldItemMainhand().getItem().canDisableShield(p_190629_1_.getHeldItemMainhand(),
				this.getActiveItemStack(), this, p_190629_1_)) {
			this.disableShield(true);
		}
	}

	public void disableShield(boolean p_190777_1_) {
		float f = 0.25F + (float) EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

		if (p_190777_1_) {
			f += 0.75F;
		}

		if (this.rand.nextFloat() < f) {
			this.getCooldownTracker().setCooldown(this.getActiveItemStack().getItem(), 100);
			this.resetActiveHand();
			this.world.setEntityState(this, (byte) 30);
		}
	}

	public CooldownTracker getCooldownTracker() {
		return cooldownTracker;
	}

	public float getCooldownPeriod() {
		return (float) (1.0D / this.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getAttributeValue()
				* 20.0D);
	}

	public float getCooledAttackStrength(float adjustTicks) {
		return MathHelper.clamp(((float) this.ticksSinceLastSwing + adjustTicks) / this.getCooldownPeriod(), 0.0F,
				1.0F);
	}

	public void resetCooldown() {
		this.ticksSinceLastSwing = 0;
	}

	public void spawnSweepParticles() {
		double d0 = (double) (-MathHelper.sin(this.rotationYaw * 0.017453292F));
		double d1 = (double) MathHelper.cos(this.rotationYaw * 0.017453292F);

		if (this.world instanceof WorldServer) {
			((WorldServer) this.world).spawnParticle(EnumParticleTypes.SWEEP_ATTACK, this.posX + d0,
					this.posY + (double) this.height * 0.5D, this.posZ + d1, 0, d0, 0.0D, d1, 0.0D);
		}
	}

	public void onCriticalHit(Entity entityHit) {
	}

	public void onEnchantmentCritical(Entity entityHit) {
	}

	@Override
	public ItemStack findAmmo() {
		for (int i = 0; i < servantInventory.getInventory().size(); ++i) {
			ItemStack itemstack = servantInventory.getStackInSlot(i);

			if (itemstack.getItem() instanceof ItemArrow) {
				return itemstack;
			}
		}

		return ItemStack.EMPTY;
	}

	@Override
	public boolean attackEntityAsMob(Entity entityIn) {
		if (entityIn.canBeAttackedWithItem()) {
			if (!entityIn.hitByEntity(this)) {
				float f = (float) this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
				float f1;

				if (entityIn instanceof EntityLivingBase) {
					f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(),
							((EntityLivingBase) entityIn).getCreatureAttribute());
				} else {
					f1 = EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(),
							EnumCreatureAttribute.UNDEFINED);
				}

				float f2 = this.getCooledAttackStrength(0.5F);
				f = f * (0.2F + f2 * f2 * 0.8F);
				f1 = f1 * f2;
				this.resetCooldown();

				if (f > 0.0F || f1 > 0.0F) {
					boolean flag = f2 > 0.9F;
					boolean flag1 = false;
					int i = 0;
					i = i + EnchantmentHelper.getKnockbackModifier(this);

					if (this.isSprinting() && flag) {
						this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
								SoundEvents.ENTITY_PLAYER_ATTACK_KNOCKBACK, this.getSoundCategory(), 1.0F, 1.0F);
						++i;
						flag1 = true;
					}

					boolean flag2 = flag && this.fallDistance > 0.0F && !this.onGround && !this.isOnLadder()
							&& !this.isInWater() && !this.isPotionActive(MobEffects.BLINDNESS) && !this.isRiding()
							&& entityIn instanceof EntityLivingBase;
					flag2 = flag2 && !this.isSprinting();

					// net.minecraftforge.event.entity.player.CriticalHitEvent hitResult =
					// net.minecraftforge.common.ForgeHooks
					// .getCriticalHit(this, entityIn, flag2, flag2 ? 1.5F : 1.0F);
					// flag2 = hitResult != null;
					// if (flag2) {
					// f *= hitResult.getDamageModifier();
					// }

					f = f + f1;
					boolean flag3 = false;
					double d0 = (double) (this.distanceWalkedModified - this.prevDistanceWalkedModified);

					if (flag && !flag2 && !flag1 && this.onGround && d0 < (double) this.getAIMoveSpeed()) {
						ItemStack itemstack = this.getHeldItem(EnumHand.MAIN_HAND);

						if (itemstack.getItem() instanceof ItemSword) {
							flag3 = true;
						}
					}

					float f4 = 0.0F;
					boolean flag4 = false;
					int j = EnchantmentHelper.getFireAspectModifier(this);

					if (entityIn instanceof EntityLivingBase) {
						f4 = ((EntityLivingBase) entityIn).getHealth();

						if (j > 0 && !entityIn.isBurning()) {
							flag4 = true;
							entityIn.setFire(1);
						}
					}

					double d1 = entityIn.motionX;
					double d2 = entityIn.motionY;
					double d3 = entityIn.motionZ;
					boolean flag5 = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

					if (flag5) {
						if (i > 0) {
							if (entityIn instanceof EntityLivingBase) {
								((EntityLivingBase) entityIn).knockBack(this, (float) i * 0.5F,
										(double) MathHelper.sin(this.rotationYaw * 0.017453292F),
										(double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
							} else {
								entityIn.addVelocity(
										(double) (-MathHelper.sin(this.rotationYaw * 0.017453292F) * (float) i * 0.5F),
										0.1D,
										(double) (MathHelper.cos(this.rotationYaw * 0.017453292F) * (float) i * 0.5F));
							}

							this.motionX *= 0.6D;
							this.motionZ *= 0.6D;
							this.setSprinting(false);
						}

						if (flag3) {
							float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(this) * f;

							for (EntityLivingBase entitylivingbase : this.world.getEntitiesWithinAABB(
									EntityLivingBase.class, entityIn.getEntityBoundingBox().grow(1.0D, 0.25D, 1.0D))) {
								if (entitylivingbase != this && entitylivingbase != entityIn
										&& !this.isOnSameTeam(entitylivingbase)
										&& this.getDistanceSq(entitylivingbase) < 9.0D) {
									entitylivingbase.knockBack(this, 0.4F,
											(double) MathHelper.sin(this.rotationYaw * 0.017453292F),
											(double) (-MathHelper.cos(this.rotationYaw * 0.017453292F)));
									entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this), f3);
								}
							}

							this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
									SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, this.getSoundCategory(), 1.0F, 1.0F);
							this.spawnSweepParticles();
						}

						if (entityIn instanceof EntityPlayerMP && entityIn.velocityChanged) {
							((EntityPlayerMP) entityIn).connection.sendPacket(new SPacketEntityVelocity(entityIn));
							entityIn.velocityChanged = false;
							entityIn.motionX = d1;
							entityIn.motionY = d2;
							entityIn.motionZ = d3;
						}

						if (flag2) {
							this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
									SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, this.getSoundCategory(), 1.0F, 1.0F);
							this.onCriticalHit(entityIn);
						}

						if (!flag2 && !flag3) {
							if (flag) {
								this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
										SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, this.getSoundCategory(), 1.0F, 1.0F);
							} else {
								this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
										SoundEvents.ENTITY_PLAYER_ATTACK_WEAK, this.getSoundCategory(), 1.0F, 1.0F);
							}
						}

						if (f1 > 0.0F) {
							this.onEnchantmentCritical(entityIn);
						}

						this.setLastAttackedEntity(entityIn);

						if (entityIn instanceof EntityLivingBase) {
							EnchantmentHelper.applyThornEnchantments((EntityLivingBase) entityIn, this);
						}

						EnchantmentHelper.applyArthropodEnchantments(this, entityIn);
						ItemStack itemstack1 = this.getHeldItemMainhand();
						Entity entity = entityIn;

						if (entityIn instanceof MultiPartEntityPart) {
							IEntityMultiPart ientitymultipart = ((MultiPartEntityPart) entityIn).parent;

							if (ientitymultipart instanceof EntityLivingBase) {
								entity = (EntityLivingBase) ientitymultipart;
							}
						}

						if (!itemstack1.isEmpty() && entity instanceof EntityLivingBase) {
							ItemStack beforeHitCopy = itemstack1.copy();
							itemstack1.getItem().hitEntity(itemstack1, (EntityLivingBase) entity, this);

							if (itemstack1.isEmpty()) {
								// net.minecraftforge.event.ForgeEventFactory.onPlayerDestroyItem(this,
								// beforeHitCopy,
								// EnumHand.MAIN_HAND);
								this.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
							}
						}

						if (entityIn instanceof EntityLivingBase) {
							float f5 = f4 - ((EntityLivingBase) entityIn).getHealth();
							// this.addStat(StatList.DAMAGE_DEALT, Math.round(f5 * 10.0F));

							if (j > 0) {
								entityIn.setFire(j * 4);
							}

							if (this.world instanceof WorldServer && f5 > 2.0F) {
								int k = (int) ((double) f5 * 0.5D);
								((WorldServer) this.world).spawnParticle(EnumParticleTypes.DAMAGE_INDICATOR,
										entityIn.posX, entityIn.posY + (double) (entityIn.height * 0.5F), entityIn.posZ,
										k, 0.1D, 0.0D, 0.1D, 0.2D);
							}
						}

						// this.addExhaustion(0.1F);
					} else {
						this.world.playSound((EntityPlayer) null, this.posX, this.posY, this.posZ,
								SoundEvents.ENTITY_PLAYER_ATTACK_NODAMAGE, this.getSoundCategory(), 1.0F, 1.0F);

						if (flag4) {
							entityIn.extinguish();
						}
					}
				}
			}
		}
		return super.attackEntityAsMob(entityIn);
	}

	@Override
	protected void resetPotionEffectMetadata() {
		super.resetPotionEffectMetadata();
		// So the data will sync
		this.dataManager.set(WATCHER_POTION_EFFECTS, new NBTTagCompound());
	}

	@Override
	public void clearActivePotions() {
		// Overidden because be default, only runs server side
		Iterator<PotionEffect> iterator = getActivePotionMap().values().iterator();

		while (iterator.hasNext()) {
			this.onFinishedPotionEffect(iterator.next());
			iterator.remove();
		}
	}

	@Override
	protected void updatePotionMetadata() {
		super.updatePotionMetadata();
		// So only server can send updates, would cause recursion otherwise
		// Would potentially allow the client to add effects, and we wouldn't want that
		if (world.isRemote)
			return;

		NBTTagList nbttaglist = new NBTTagList();

		for (PotionEffect potioneffect : getActivePotionMap().values()) {
			nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
		}

		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("ActiveEffects", nbttaglist);

		this.dataManager.set(WATCHER_POTION_EFFECTS, compound);
	}

	@Override
	public float getAIMoveSpeed() {
		return (float) getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		updateSize();
		if (this.xpCooldown > 0) {
			--this.xpCooldown;
		}
		++ticksSinceLastSwing;
		cooldownTracker.tick();
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.getHealth() > 0.0F) {
			AxisAlignedBB axisalignedbb;

			if (this.isRiding() && !this.getRidingEntity().isDead) {
				axisalignedbb = this.getEntityBoundingBox().union(this.getRidingEntity().getEntityBoundingBox())
						.grow(1.0D, 0.0D, 1.0D);
			} else {
				axisalignedbb = this.getEntityBoundingBox().grow(0.5D, 0.5D, 0.5D);
			}

			List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, axisalignedbb);

			for (int i = 0; i < list.size(); ++i) {
				Entity entity = list.get(i);

				if (!entity.isDead && !world.isRemote) {
					this.collideWithServant(entity);
				}
			}
		}
		updateArmSwingProgress();
	}

	@Override
	public void collideWithServant(Entity entity) {
		if (entity instanceof ICollideWithServant) {
			((ICollideWithServant) entity).onCollideWithServant(this);
		} else if (entity instanceof EntityArrow) {
			EntityArrow arrow = (EntityArrow) entity;
			boolean inGround = (boolean)ReflectionHelper.getPrivateValue(EntityArrow.class, arrow, "inGround");
			if (arrow.pickupStatus == PickupStatus.ALLOWED && inGround) {
				System.out.println("Pickup");
				servantInventory.addItem(new ItemStack(Items.ARROW));
				onItemPickup(arrow, 1);
				arrow.setDead();
			}

		}

	}

	// Servant Setters
	@Override
	public void setExperience(float exp) {
		this.dataManager.set(CURRENT_EXP, exp);
	}

	@Override
	public void setArmorVisibility(int option, boolean visible) {
		if (option > 4 || option < 1) {
			System.out.println("Invalid set armor option");
			return;
		}
		option = 1 << (option - 1);
		int visiblity = this.dataManager.get(ARMOR_VISIBLITY);
		if ((visiblity & option) == (visible ? 0 : option)) {
			int result = visiblity ^ option;
			this.dataManager.set(ARMOR_VISIBLITY, result);
		}
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setFloat("XpP", this.getExperience());
		compound.setInteger("XpLevel", this.getLevel());
		compound.setBoolean("freedom_mode", isFreedomMode());
		compound.setBoolean("sneaking", isSneaking());
		compound.setTag("servant_inventory", servantInventory.writeToNBT(new NBTTagList()));
		int v = this.dataManager.get(ARMOR_VISIBLITY);
		compound.setInteger("visual_options", v);
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		this.dataManager.set(CURRENT_EXP, compound.getFloat("XpP"));
		setFreedomMode(compound.getBoolean("freedom_mode"));
		setSneaking(compound.getBoolean("sneaking"));
		servantInventory.readFromNBT(compound.getTagList("servant_inventory", 10));
		this.dataManager.set(ARMOR_VISIBLITY, compound.getInteger("visual_options"));
	}

}
