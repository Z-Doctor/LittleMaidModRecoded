package zdoctor.littlemaidmod.util;

import java.util.Comparator;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;

public class TileEntitySorter<T extends TileEntity> implements Comparator<T> {
	protected EntityLivingBase entity;

	public TileEntitySorter(EntityLivingBase entity) {
		this.entity = entity;
	}

	@Override
	public int compare(T o1, T o2) {
		double dist1 = o1.getDistanceSq(entity.posX, entity.posY, entity.posZ);
		double dist2 = o2.getDistanceSq(entity.posX, entity.posY, entity.posZ);
		return dist1 == dist2 ? 0 : dist1 < dist2 ? -1 : 1;
	}

}
