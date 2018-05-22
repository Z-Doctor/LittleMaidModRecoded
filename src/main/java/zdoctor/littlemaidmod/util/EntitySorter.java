package zdoctor.littlemaidmod.util;

import java.util.Comparator;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public class EntitySorter<T extends Entity> implements Comparator<T> {
	protected EntityLivingBase entity;

	public EntitySorter(EntityLivingBase entity) {
		this.entity = entity;
	}

	@Override
	public int compare(T o1, T o2) {
		float dist1 = entity.getDistance(o1);
		float dist2 = entity.getDistance(o2);
		return dist1 == dist2 ? 0 : dist1 < dist2 ? -1 : 1;
	}

}
