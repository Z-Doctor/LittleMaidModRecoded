package zdoctor.littlemaidmod.entity.ai.servant;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import zdoctor.littlemaidmod.entity.EntityServantBase;
import zdoctor.littlemaidmod.util.FailedItem;

public abstract class ServantAICollect<T extends Entity> extends EntityAIBase {
	protected EntityServantBase servant;
	protected double searchRadius;

	protected List<T> itemQueue = new ArrayList<>();
	protected HashMap<T, FailedItem<T>> failedQueue = new HashMap<>();
	protected PathNavigate navigator;
	protected Path currentPath;
	protected Comparator<T> sorter;
	protected T currentTarget;
	protected int attemptTick = 0;
	protected int attemptTime;
	private double orginalValue;

	/**
	 * @param attemptTime
	 *            - in seconds
	 */
	public ServantAICollect(EntityServantBase servant, double searchRadius, int attemptTime, Comparator<T> sorter) {
		this.servant = servant;
		this.searchRadius = searchRadius;
		this.orginalValue = servant.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue();
		this.attemptTime = attemptTime * 20; // 20 ticks per second
		this.navigator = servant.getNavigator();
		this.sorter = sorter;
		setMutexBits(3);
	}

	@Override
	public boolean shouldExecute() {
		if (servant.servantInventory == null || !servant.hasContract() || servant.isWaiting())
			return false;
		return parseInventory() && findTargets();
	}

	@Override
	public void startExecuting() {
		attemptTick = attemptTime;
		if (searchRadius > servant.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).getBaseValue())
			servant.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(searchRadius);
	}

	@Override
	public boolean shouldContinueExecuting() {
		return shouldExecute();
	}

	@Override
	public void resetTask() {
		itemQueue.clear();
		navigator.clearPath();
		currentPath = null;
		currentTarget = null;
		servant.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(orginalValue);
		failedQueue.entrySet().removeIf(set -> set.getValue().penalityServed());
	}

	@Override
	public void updateTask() {
//		System.out.println("Collecting: " + getTargetClass());
		findTargets();
		failedQueue.entrySet().removeIf(set -> set.getValue().penalityServed());

		if (itemQueue.isEmpty()) {
			resetTask();
			return;
		} else if (currentTarget != null) {
			attemptTick--;
			if (currentPath != null)
				navigator.setPath(currentPath, servant.getAIMoveSpeed());

		}

		if (currentTarget != null && (notValidTarget(currentTarget) || attemptTick <= 0)) {
			if (attemptTick <= 0)
				failedQueue.put(currentTarget, new FailedItem<T>(currentTarget, servant.world.rand.nextInt(10) + 10));
			if (!itemQueue.isEmpty())
				itemQueue.remove(0);
			navigator.clearPath();
			currentPath = null;
			currentTarget = null;

			attemptTick = attemptTime;
		}

		if (!itemQueue.isEmpty() && (currentTarget == null || currentPath == null || currentPath.isFinished())) {
			currentTarget = itemQueue.get(0);
			currentPath = navigator.getPathToEntityLiving(currentTarget);
			if (currentPath != null) {
				navigator.setPath(currentPath, servant.getAIMoveSpeed());
				attemptTick = attemptTime;
			}
		}
	}

	public boolean findTargets() {
		ArrayList found = new ArrayList<>(servant.world.getEntities(getTargetClass(), this::isValidTarget));
		found.removeAll(itemQueue);
		itemQueue.addAll(found);
		itemQueue.sort(sorter);
		itemQueue.removeAll(failedQueue.keySet());
		itemQueue.removeIf(this::notValidTarget);
		return !itemQueue.isEmpty();
	}

	public abstract Class getTargetClass();

	public boolean parseInventory() {
		// emptySlots.clear();
		int i = 0;
		for (ItemStack slot : servant.servantInventory.getInventory()) {
			if (slot.isEmpty() || slot.getMaxStackSize() > slot.getCount())
				return true;
		}
		return false;
	}

	public abstract boolean isValidTarget(T item);

	public abstract boolean notValidTarget(T item);

}
