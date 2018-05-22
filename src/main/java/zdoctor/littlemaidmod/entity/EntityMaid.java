package zdoctor.littlemaidmod.entity;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import zdoctor.littlemaidmod.Config;
import zdoctor.littlemaidmod.api.IMaid;
import zdoctor.littlemaidmod.api.IMaidUniform;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIAttack;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIAttackNearestTarget;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIAttackRangedBow;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIBeg;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAICollectArrows;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAICollectItem;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAICollectXP;
import zdoctor.littlemaidmod.entity.ai.maid.ServantAICook;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIEatSugar;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIFollowOwner;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIHurtByTarget;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIJumpToMaster;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIMoveIndoors;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIMoveThroughVillage;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIPanic;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAISwimming;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIWait;
import zdoctor.littlemaidmod.entity.ai.maid.MaidAIWander;
import zdoctor.littlemaidmod.init.LMMItems;
import zdoctor.littlemaidmod.library.UniformRegistry;
import zdoctor.littlemaidmod.util.EnumMaidMode;
import zdoctor.littlemaidmod.util.EnumMood;

public class EntityMaid extends EntityServant implements IMaid {

	public static final DataParameter<Byte> MAID_MODE = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.BYTE);
	public static final DataParameter<Boolean> MAID_PROTEST = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.BOOLEAN);

	// Cosmetic
	public static final DataParameter<Integer> SKIN_COLOR = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);
	public static final DataParameter<Integer> UNDERGARMENTS = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);
	
	public static final DataParameter<Integer> MAID_MODEL = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);

	public static final DataParameter<Integer> UNIFORM = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);

	public static final DataParameter<Integer> HAIR_COLOR = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);
	public static final DataParameter<Integer> HAIR_STYLE = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);

	public static final DataParameter<Integer> EYE_TYPE = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);
	public static final DataParameter<Integer> IRIS_COLOR = EntityDataManager.createKey(EntityMaid.class,
			DataSerializers.VARINT);

	protected int statusUpdate;

	protected MaidAIPanic panic;

	public EntityMaid(World worldIn) {
		super(worldIn);
		statusUpdate = getRNG().nextInt(120) + 60;
		this.setSize(0.6F, 1.4F);
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

	@Override
	public float getEyeHeight() {
		return this.height * .77f;
	}

	@Override
	protected void initEntityAI() {
		this.tasks.addTask(0, new MaidAISwimming(this));
		this.tasks.addTask(0, new MaidAIBeg(this, 10f));
		this.tasks.addTask(2, new MaidAIEatSugar(this, stack -> stack.getItem() == LMMItems.SUGAR_CUBE));
		// this.tasks.addTask(21, new MaidAIFleeRain(this));

		this.tasks.addTask(31, panic = new MaidAIPanic(this));
		this.tasks.addTask(32, new MaidAIWander(this, 0.3F));
		this.tasks.addTask(33, new MaidAIMoveIndoors(this));
		this.tasks.addTask(34, new MaidAIMoveIndoors(this));
		this.tasks.addTask(35, new MaidAIMoveThroughVillage(this, false));
		this.tasks.addTask(36, new EntityAIOpenDoor(this, true));
		this.tasks.addTask(51, new EntityAILookIdle(this));
		this.tasks.addTask(52, new EntityAIWatchClosest(this, EntityPlayer.class, 8, 0.1F));
		this.tasks.addTask(53, new EntityAIWatchClosest(this, EntityCreature.class, 8, 0.25F));

	}

	@Override
	protected void setupTamedAI() {
		this.tasks.addTask(0, new MaidAIWait(this));
		this.tasks.addTask(2, new MaidAIJumpToMaster(this, .75F));
		this.tasks.addTask(3, new MaidAIAttack(this, false));
		this.tasks.addTask(3, new MaidAIAttackRangedBow(this, getAIMoveSpeed(), 20, 15.0F));
		this.tasks.addTask(21, new ServantAICook(this));
		this.tasks.addTask(22, new MaidAICollectItem(this, 30D, 15));
		this.tasks.addTask(23, new MaidAICollectXP(this, 30D, 10));
		this.tasks.addTask(24, new MaidAICollectArrows(this, 30D, 10));
		this.tasks.addTask(30, new MaidAIFollowOwner(this, 36D, 25D, 81D));

		this.targetTasks.addTask(1, new MaidAIHurtByTarget(this, false));
		this.targetTasks.addTask(2, new MaidAIAttackNearestTarget(this, EntityLivingBase.class, 0, true, false,
				entity -> entity instanceof IMob));
		this.targetTasks.addTask(5, new EntityAILeapAtTarget(this, 0.3F));

	}

	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(MAID_MODE, (byte) EnumMaidMode.WILD.ordinal());

		this.dataManager.register(UNIFORM, Config.Options.SPAWN_WITH_UNIFORM.getValue() ? 0 : -1);
		this.dataManager.register(HAIR_STYLE, Integer.valueOf(getRNG().nextInt(Config.Variables.HAIR_VARIANTS.length)));
		this.dataManager.register(SKIN_COLOR, Integer.valueOf(getRNG().nextInt(Config.Variables.SKIN_VARIANTS.length)));
		this.dataManager.register(UNDERGARMENTS,
				Integer.valueOf(getRNG().nextInt(Config.Variables.UG_VARIANTS.length)));
		this.dataManager.register(HAIR_COLOR, Integer
				.valueOf(EnumDyeColor.values()[getRNG().nextInt(EnumDyeColor.values().length - 1)].getDyeDamage()));

		this.dataManager.register(EYE_TYPE, Integer.valueOf(getRNG().nextInt(Config.Variables.EYE_VARIANTS.length)));
		this.dataManager.register(IRIS_COLOR, Integer
				.valueOf(EnumDyeColor.values()[getRNG().nextInt(EnumDyeColor.values().length - 1)].getDyeDamage()));
	}

	@Override
	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(20);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.45D);
	}

	@Override
	public void setTamedBy(EntityPlayer player) {
		super.setTamedBy(player);
		this.tasks.removeTask(panic);
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		statusUpdate--;

		if (!world.isRemote) {
			if (isSentryMode() && !isFreedomMode())
				setSentryMode(false);
		}

		if (statusUpdate <= 0) {
			if (isSentryMode())
				playMoodEffect(EnumMood.BUSY);
			statusUpdate = getRNG().nextInt(120) + 60;
		}

	}

	@Override
	public void onContractFormed(EntityPlayer player) {
		super.onContractFormed(player);
		setMaidMode(EnumMaidMode.ESCORTER);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (hasContract() && isOwner(player) && player.isSneaking() && hasUniform()) {
			ItemStack itemstack = player.getHeldItem(hand);
			if (itemstack.getItem() == Items.SUGAR) {
				if (!player.capabilities.isCreativeMode)
					itemstack.shrink(1);
				ItemStack uniform = new ItemStack(LMMItems.DEFAULT_UNIFORM, 1, getUniform());
				setUniform(-1);
				player.addItemStackToInventory(uniform);
				return true;
			}
		}
		return super.processInteract(player, hand);
	}

	@Override
	public boolean shouldAttackEntity(EntityLivingBase target, EntityLivingBase owner) {
		if (target instanceof EntityPlayer)
			return !isOwner((EntityPlayer) target);
		if (target instanceof EntityTameable)
			return isOwner(((EntityTameable) target).getOwner());
		return super.shouldAttackEntity(target, owner);
	}

	@Override
	public boolean isModeChanger(ItemStack itemstack) {
		return super.isModeChanger(itemstack) || itemstack.getItem() == Items.FEATHER
				|| (isFreedomMode() && itemstack.getItem() == Items.REDSTONE) || itemstack.getItem() == Items.DYE
				|| itemstack.getItem() instanceof IMaidUniform;
	}

	@Override
	public boolean updateMode(ItemStack changer, ItemStack activeItem) {
		if (changer.getItem() == Items.FEATHER) {
			setFreedomMode(!isFreedomMode());
			playMoodEffect(isFreedomMode() ? EnumMood.RELAXED : EnumMood.HAPPY);
			return true;
		} else if (isFreedomMode() && changer.getItem() == Items.REDSTONE) {
			setSentryMode(!isSentryMode());
			playMoodEffect(isSentryMode() ? EnumMood.BUSY : EnumMood.RELAXED);
			return true;
		} else if (changer.getItem() == Items.DYE) {
			setHairColor(EnumDyeColor.byDyeDamage(changer.getItemDamage()));
			return true;
		} else if (changer.getItem() instanceof IMaidUniform) {
			if (hasUniform()) {
				ItemStack uniform = new ItemStack(LMMItems.DEFAULT_UNIFORM, 1, getUniform());
				setUniform(-1);
				((EntityPlayer) getOwner()).addItemStackToInventory(uniform);
			}

			setUniform(((IMaidUniform) changer.getItem()).getUniformID(changer));
			return true;
		}

		if (isWaiting()) {
			EnumMaidMode mode = EnumMaidMode.getValidModeFor(activeItem);
			if (mode != null)
				setMaidMode(mode);
		}
		super.updateMode(changer, activeItem);
		playMoodEffect(isWaiting() ? EnumMood.CONFUSED : EnumMood.HAPPY);
		return true;
	}

	@Override
	public void setWaiting(boolean b) {
		super.setWaiting(b);
	}

	@Override
	public EnumDyeColor getHairColor() {
		return EnumDyeColor.byDyeDamage(((Integer) this.dataManager.get(HAIR_COLOR)).intValue() & 15);
	}

	@Override
	public void setHairColor(EnumDyeColor hairColor) {
		this.dataManager.set(HAIR_COLOR, Integer.valueOf(hairColor.getDyeDamage()));
	}

	@Override
	public boolean canPickupItem(ItemStack itemstack) {
		return super.canPickupItem(itemstack) && getMaidMode() == EnumMaidMode.ESCORTER;
	}

	@Override
	public boolean shouldComeToAid() {
		return getMaidMode().isBattleMode();
	}

	@Override
	public EnumMaidMode getMaidMode() {
		return EnumMaidMode.values()[this.dataManager.get(MAID_MODE)];
	}

	@Override
	public void setMaidMode(EnumMaidMode mode) {
		this.dataManager.set(MAID_MODE, (byte) mode.ordinal());
	}

	@Override
	public boolean isMaidMode(EnumMaidMode mode) {
		return getMaidMode() == mode;
	}

	@Override
	public String getStatus() {
		return getMaidMode().toString();
	}

	@Override
	public boolean isFavorite(ItemStack itemstack) {
		return super.isFavorite(itemstack) || itemstack.getItem() == Items.CAKE
				|| itemstack.getItem() == LMMItems.SUGAR_CUBE;
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
		super.writeEntityToNBT(compound);
		compound.setInteger("maid_mode", getMaidMode().ordinal());
		compound.setByte("maid_hair_color", (byte) this.getHairColor().getDyeDamage());
		compound.setInteger("maid_skin_color", getSkinTone());
		compound.setInteger("maid_undergarments", getUndergarments());
		compound.setInteger("maid_hairstyle", getHairStyle());
		compound.setInteger("maid_uniform", getUniform());

		compound.setInteger("maid_eye_type", getEyeType());
		compound.setByte("maid_iris_color", (byte) getEyeColor().getDyeDamage());
	}

	@Override
	public void readEntityFromNBT(NBTTagCompound compound) {
		super.readEntityFromNBT(compound);
		setMaidMode(EnumMaidMode.values()[compound.getInteger("maid_mode")]);
		if (compound.hasKey("maid_hair_color", 99)) {
			this.setHairColor(EnumDyeColor.byDyeDamage(compound.getByte("maid_hair_color")));
		}

		if (compound.hasKey("maid_iris_color", 99)) {
			this.dataManager.set(IRIS_COLOR,
					Integer.valueOf(EnumDyeColor.byDyeDamage(compound.getInteger("maid_iris_color")).getDyeDamage()));
		}
		this.dataManager.set(EYE_TYPE, compound.getInteger("maid_eye_type"));

		this.dataManager.set(SKIN_COLOR, compound.getInteger("maid_skin_color"));
		this.dataManager.set(UNDERGARMENTS, compound.getInteger("maid_undergarments"));
		setUniform(compound.getInteger("maid_uniform"));
		this.dataManager.set(HAIR_STYLE, compound.getInteger("maid_hairstyle"));

	}

	@Override
	public int getSkinTone() {
		return this.dataManager.get(SKIN_COLOR);
	}

	@Override
	public int getUndergarments() {
		return this.dataManager.get(UNDERGARMENTS);
	}

	@Override
	public int getHairStyle() {
		return this.dataManager.get(HAIR_STYLE);
	}

	@Override
	public int getEyeType() {
		return this.dataManager.get(EYE_TYPE);
	}

	@Override
	public boolean hasUniform() {
		return this.dataManager.get(UNIFORM) != -1;
	}

	@Override
	public void setUniform(int uniform) {
		if (UniformRegistry.isValidUniform(uniform))
			this.dataManager.set(UNIFORM, uniform);
	}

	@Override
	public int getUniform() {
		return this.dataManager.get(UNIFORM);
	}

	@Override
	public EnumDyeColor getEyeColor() {
		return EnumDyeColor.byDyeDamage(((Integer) this.dataManager.get(IRIS_COLOR)).intValue() & 15);
	}

}
