package zdoctor.littlemaidmod.entity;

import java.util.List;

import com.google.common.base.Predicates;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import zdoctor.littlemaidmod.api.ICollideWithServant;

public class EntityCustomXP extends EntityXPOrb implements ICollideWithServant {

	public EntityCustomXP(World worldIn) {
		super(worldIn);
		System.out.println("New World exp");
		this.setSize(0.25F, 0.25F);
	}

	public EntityCustomXP(World worldIn, double x, double y, double z, int expValue) {
		super(worldIn, x, y, z, expValue);
		System.out.println("New World exp value");
	}

	public EntityCustomXP(EntityXPOrb orb) {
		super(orb.getEntityWorld(), orb.posX, orb.posY, orb.posZ, orb.xpValue);
		System.out.println("New World exp orb");
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		EntityPlayer closestPlayer = this.world.getClosestPlayerToEntity(this, 8.0D);

		if (closestPlayer != null && (closestPlayer.isSpectator() || closestPlayer.isCreative())) {
			closestPlayer = null;
		}

		EntityLivingBase closestServant = getClosestServant(this, 8D, true);

		if (closestPlayer == null) {
			if (closestServant != null) {
				this.motionX = 0;
				this.motionY = 0;
				this.motionZ = 0;
				double d1 = (closestServant.posX - this.posX) / 8.0D;
				double d2 = (closestServant.posY + (double) closestServant.getEyeHeight() / 2.0D - this.posY) / 8.0D;
				double d3 = (closestServant.posZ - this.posZ) / 8.0D;
				double d4 = Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3);
				double d5 = 1.0D - d4;
				if (d5 > 0.0D) {
					d5 = d5 * d5;
					this.motionX += d1 / d4 * d5 * 0.1D;
					this.motionY += d2 / d4 * d5 * 0.1D;
					this.motionZ += d3 / d4 * d5 * 0.1D;
				}
			}

			float f = 0.98F;

			if (this.onGround) {
				BlockPos underPos = new BlockPos(MathHelper.floor(this.posX),
						MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ));
				net.minecraft.block.state.IBlockState underState = this.world.getBlockState(underPos);
				f = underState.getBlock().getSlipperiness(underState, this.world, underPos, this) * 0.98F;
			}

			this.motionX *= (double) f;
			this.motionY *= 0.9800000190734863D;
			this.motionZ *= (double) f;

			if (this.onGround) {
				this.motionY *= -0.8999999761581421D;
			}
		}

	}

	public EntityServantBase getClosestServant(EntityCustomXP entityCustomXP, double range, boolean needsContract) {
		double d0 = -1.0D;
		List<EntityServantBase> entitiesList = world.getEntities(EntityServantBase.class,
				needsContract ? EntityServantBase::hasContract : Predicates.alwaysTrue());
		EntityServantBase servant = null;

		for (int j2 = 0; j2 < entitiesList.size(); ++j2) {
			EntityServantBase servant1 = entitiesList.get(j2);

			double d1 = servant1.getDistanceSq(posX, posY, posZ);

			if ((range < 0.0D || d1 < range * range) && (d0 == -1.0D || d1 < d0)) {
				d0 = d1;
				servant = servant1;
			}
		}

		return servant;
	}

	@Override
	public void onCollideWithServant(Entity entityIn) {
		if (!this.world.isRemote && entityIn instanceof EntityServantBase && !entityIn.isDead) {
			EntityServantBase servant = (EntityServantBase) entityIn;
			if (servant.hasContract() && this.delayBeforeCanPickup == 0 && servant.xpCooldown == 0) {
				// TODO Add event
				servant.xpCooldown = 2;
				servant.onItemPickup(this, 1);
				ItemStack itemstack = EnchantmentHelper.getEnchantedItem(Enchantments.MENDING, servant);

				if (!itemstack.isEmpty() && itemstack.isItemDamaged()) {
					int i = Math.min(this.xpToDurability(this.xpValue), itemstack.getItemDamage());
					this.xpValue -= this.durabilityToXp(i);
					itemstack.setItemDamage(itemstack.getItemDamage() - i);
				}

				if (this.xpValue > 0) {
					servant.addExperience(this.xpValue);
				}
				this.setDead();
			}
		}
	}

	private int xpToDurability(int xp) {
		return xp * 2;
	}

	private int durabilityToXp(int durability) {
		return durability / 2;
	}

}
