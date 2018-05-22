package zdoctor.littlemaidmod.api;

import net.minecraft.item.EnumDyeColor;
import zdoctor.littlemaidmod.util.EnumMaidMode;

public interface IMaid extends IServant {

	public EnumMaidMode getMaidMode();
	
	public void setMaidMode(EnumMaidMode mode);
	
	boolean isMaidMode(EnumMaidMode mode);
	
	EnumDyeColor getHairColor();

	void setHairColor(EnumDyeColor hairColor);

	int getSkinTone();

	int getUndergarments();

	int getHairStyle();

	EnumDyeColor getEyeColor();

	int getEyeType();

	boolean hasUniform();

	void setUniform(int uniform);

	int getUniform();

}
