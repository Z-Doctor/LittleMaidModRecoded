package zdoctor.littlemaidmod.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import zdoctor.littlemaidmod.items.ItemMaidUniform;
import zdoctor.littlemaidmod.items.ItemSugarCube;

public class LMMItems {
	public static void init() {};
	
	public static final Item DEFAULT_UNIFORM;
	public static final ItemFood SUGAR_CUBE;
	
	static {
		DEFAULT_UNIFORM = new ItemMaidUniform();
		SUGAR_CUBE = new ItemSugarCube();
	}
}
