package zdoctor.littlemaidmod.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public enum EnumMaidMode {
	WILD,
	FENCER(true, item -> item.getItem() instanceof ItemSword),
	ARCHER(true, true, item -> item.getItem() instanceof ItemBow),
	BLOODSUCKER(true, item -> item.getItem() instanceof ItemAxe),
	RIPPER(false, item -> item.getItem() instanceof ItemShears),
	DEMOLISHER(false, item -> item.getItem() == Item.getItemFromBlock(Blocks.TNT)),
	TORCHER(false, item -> item.getItem() == Item.getItemFromBlock(Blocks.TORCH)),
	COOK(false, item -> item.getItem() == Items.COAL),
	ALCHEMIST(true, true, item -> (item.getItem() == Items.LINGERING_POTION || item.getItem() == Items.SPLASH_POTION)),
	BREWER(false, item -> item.getItem() == Items.POTIONITEM),
	ESCORTER(false, Predicates.alwaysTrue());

	private Predicate<ItemStack> check;
	private boolean battleMode;
	private boolean rangedMode;

	private EnumMaidMode() {
		this(false, false, Predicates.alwaysFalse());
	}

	private EnumMaidMode(boolean battleMode, Predicate<ItemStack> predicate) {
		this(battleMode, false, predicate);
	}

	private EnumMaidMode(boolean battleMode, boolean rangedMode, Predicate<ItemStack> predicate) {
		check = predicate;
		this.battleMode = battleMode;
		this.rangedMode = rangedMode;
	}

	public static EnumMaidMode getValidModeFor(ItemStack input) {
		for (EnumMaidMode mode : EnumMaidMode.values()) {
			// System.out.println("Checking " + mode + ": " + input + " -> " +
			// mode.check.apply(input));
			if (mode.check.apply(input)) {
				// System.out.println("test: " + (input.getItem() == Items.POTIONITEM));
				return mode;
			}
		}
		return EnumMaidMode.ESCORTER;
	}

	public boolean check(ItemStack input) {
		return check.apply(input);
	}

	public boolean isBattleMode() {
		return battleMode;
	}

	public boolean isRangedMode() {
		return rangedMode;
	}

}
