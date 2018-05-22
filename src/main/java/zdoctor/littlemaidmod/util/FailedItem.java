package zdoctor.littlemaidmod.util;

import net.minecraft.entity.Entity;

public class FailedItem<T extends Entity> {
	private int penality;
	private int startTick;
	private T item;

	public FailedItem(T item, int penality) {
		this.penality = penality * 20;
		this.startTick = item.ticksExisted;
		this.item = item;
	}

	public boolean penalityServed() {
		return item.isDead || item.ticksExisted - startTick >= penality;
	}

	public boolean match(T item2) {
		return item.isEntityEqual(item2);
	}
}