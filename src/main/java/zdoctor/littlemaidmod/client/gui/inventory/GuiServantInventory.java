package zdoctor.littlemaidmod.client.gui.inventory;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.IInventory;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import zdoctor.lazylibrary.client.util.TextureLocation;
import zdoctor.littlemaidmod.MaidModMain;
import zdoctor.littlemaidmod.client.gui.GuiButtonBoostChange;
import zdoctor.littlemaidmod.client.gui.GuiButtonFreedomToggle;
import zdoctor.littlemaidmod.client.gui.GuiButtonSwimToggle;
import zdoctor.littlemaidmod.client.gui.GuiServantButton;
import zdoctor.littlemaidmod.entity.EntityServantBase;
import zdoctor.littlemaidmod.inventory.ContainerMaidInventory;
import zdoctor.littlemaidmod.util.ServantExperienceHelper;

public class GuiServantInventory extends InventoryEffectRenderer {

	private EntityServantBase servant;
	protected boolean maidHasActivePotionEffects = false;

	protected int ySizebk;
	protected int updateCounter;
	protected Random rand;

	// Maid Gui
	protected IInventory upperChestInventory;
	protected IInventory lowerChestInventory;

	// protected GuiButtonNextPage txbutton[] = new GuiButtonNextPage[4];
	// protected GuiButton selectbutton;
	protected GuiServantButton optionButton[] = new GuiServantButton[4];
	protected GuiButtonSwimToggle swimbutton;
	protected GuiButtonFreedomToggle frdmbutton;
	protected GuiButtonBoostChange boostMinus;
	protected GuiButtonBoostChange boostPlus;
	// public boolean isChangeTexture;

	public static final ResourceLocation SERVANT_INVENTORY_GUI = new TextureLocation.GUITextureLocation(
			MaidModMain.MODID, "container/littlemaidinventory");

	public GuiServantInventory(EntityServantBase servantIn) {
		super(new ContainerMaidInventory(servantIn));
		servant = servantIn;

		upperChestInventory = ((EntityPlayer) servant.getOwner()).inventory;
		lowerChestInventory = servant.servantInventory;
		allowUserInput = false;
		updateCounter = 0;
		ySizebk = ySize;
		ySize = 207;

		rand = new Random();
		// isChangeTexture = true;
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		updateCounter++;
	}

	@Override
	public void initGui() {
		this.buttonList.clear();
		super.initGui();

		// buttonList.add(txbutton[0] = new GuiButtonNextPage(100, guiLeft + 25, guiTop
		// + 7, false));
		// buttonList.add(txbutton[1] = new GuiButtonNextPage(101, guiLeft + 55, guiTop
		// + 7, true));
		// buttonList.add(txbutton[2] = new GuiButtonNextPage(110, guiLeft + 25, guiTop
		// + 47, false));
		// buttonList.add(txbutton[3] = new GuiButtonNextPage(111, guiLeft + 55, guiTop
		// + 47, true));
		// buttonList.add(selectbutton = new GuiButton(200, guiLeft + 25, guiTop + 25,
		// 53, 17, "select"));

		// buttonList.add(visarmorbutton[0] = new GuiButtonArmorToggle(300, guiLeft,
		// guiTop - 14,
		// "littleMaidMob.gui.toggle.inner", true).setNode(0).setLight(0));
		// buttonList.add(visarmorbutton[1] = new GuiButtonArmorToggle(301, guiLeft +
		// 16, guiTop - 14,
		// "littleMaidMob.gui.toggle.innerlight", true).setNode(0).setLight(1));
		// buttonList.add(visarmorbutton[2] = new GuiButtonArmorToggle(302, guiLeft +
		// 32, guiTop - 14,
		// "littleMaidMob.gui.toggle.outer", true).setNode(1).setLight(0));
		buttonList.add(optionButton[3] = new GuiServantButton(303, guiLeft + 48, guiTop - 15, 0, 208,
				"servant.acceptgifts")); // .setNode(1).setLight(1));

		buttonList.add(frdmbutton = new GuiButtonFreedomToggle(311, guiLeft + 64, guiTop - 16,
				"littleMaidMob.gui.toggle.freedom", servant.swimmingEnabled()));
		buttonList.add(swimbutton = new GuiButtonSwimToggle(310, guiLeft + 80, guiTop - 16,
				"littleMaidMob.gui.toggle.swim", servant.swimmingEnabled()));
		buttonList.add(boostMinus = new GuiButtonBoostChange(320, guiLeft + 96, guiTop - 16,
				"littleMaidMob.gui.button.minusboost").setInverse(true).setEnabled(false));
		buttonList.add(boostPlus = new GuiButtonBoostChange(321, guiLeft + xSize - 16, guiTop - 16,
				"littleMaidMob.gui.button.plusboost"));

	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		// Draws maid and text
		mc.fontRenderer.drawString(I18n.format(lowerChestInventory.getName()), 8, 64, 0x404040);
		mc.fontRenderer.drawString(I18n.format(upperChestInventory.getName()), 8, 114, 0x404040);

		mc.fontRenderer.drawString(I18n.format("lmm.mode.".concat(servant.getStatus().toLowerCase())), 83, 53,
				0x404040);

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		int lj = 0;
		int lk = 0;

		// Not sure what this do TODO look into
		GlStateManager.enableRescaleNormal();
		GlStateManager.enableColorMaterial();
		// Handles rendering maid in inventory gui
		GlStateManager.pushMatrix();
		GlStateManager.translate(lj + 51, lk + 57, 50F);
		float f1 = 30F;
		GlStateManager.scale(-f1, f1, f1);
		GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
		// The orignal values
		float f2 = servant.renderYawOffset;
		float f3 = servant.rotationYaw;
		float f4 = servant.rotationYawHead;
		float f5 = servant.rotationPitch;

		float f8 = guiLeft + 51 - mouseX;
		float f9 = guiTop + 22 - mouseY;
		GlStateManager.rotate(135F, 0.0F, 1.0F, 0.0F);
		// So the maid won't be bright
		RenderHelper.enableStandardItemLighting();

		GlStateManager.rotate(-135F, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(-(float) Math.atan(f9 / 40F) * 20F, 1.0F, 0.0F, 0.0F);
		servant.renderYawOffset = (float) Math.atan(f8 / 40F) * 20F;
		servant.rotationYawHead = servant.rotationYaw = (float) Math.atan(f8 / 40F) * 40F;
		servant.rotationPitch = -(float) Math.atan(f9 / 40F) * 20F;

		GlStateManager.translate(0.0F, 0.0F, 0.0F);
		Minecraft.getMinecraft().getRenderManager().setRenderShadow(false);
		Minecraft.getMinecraft().getRenderManager().renderEntity(servant, 0, 0, 0, 0, 1, true);
		Minecraft.getMinecraft().getRenderManager().setRenderShadow(true);

		// Resets values of model. Looks funny when not reset
		servant.renderYawOffset = f2;
		servant.rotationYaw = f3;
		servant.rotationYawHead = f4;
		servant.rotationPitch = f5;

		GlStateManager.popMatrix();

		// Renders hover text
		Iterator iterator = buttonList.iterator();
		while (iterator.hasNext()) {
			GuiButton gButton = (GuiButton) iterator.next();
			gButton.drawButtonForegroundLayer(mouseX - guiLeft, mouseY - guiTop);
		}

	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		drawDefaultBackground();
		ResourceLocation maidTexture = new TextureLocation.GUITextureLocation(MaidModMain.MODID,
				"container/littlemaidinventory");

		zdoctor.lazylibrary.client.util.RenderHelper.bindTexture(maidTexture);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int lj = guiLeft;
		int lk = guiTop;
		drawTexturedModalRect(lj, lk, 0, 0, xSize, ySize);

		displayDebuffEffects();
		drawHealthArmor(0, 0);

		int booster = servant.getExpBooster();
		if (booster >= ServantExperienceHelper.getBoosterLimit(servant.getLevel()))
			boostPlus.setEnabled(false);
		else
			boostPlus.setEnabled(true);
		if (booster <= 1)
			boostMinus.setEnabled(false);
		else
			boostMinus.setEnabled(true);

		GlStateManager.colorMask(true, true, true, false);
		GlStateManager.disableLighting();
		GlStateManager.disableDepth();
		int level = servant.getLevel();
		if (level >= ServantExperienceHelper.EXP_FUNCTION_MAX) {
			level--;
		}
		float currentxp = servant.getExperience() - ServantExperienceHelper.getRequiredExpToLevel(level);
		float nextxp = ServantExperienceHelper.getRequiredExpToLevel(level + 1)
				- ServantExperienceHelper.getRequiredExpToLevel(level);
		drawGradientRect(guiLeft + 86, guiTop + 4, guiLeft + 166, guiTop + 5 + mc.fontRenderer.FONT_HEIGHT, 0x80202020,
				0x80202020);
		drawGradientRect(guiLeft + 86, guiTop + 4, guiLeft + 86 + (int) (80 * currentxp / nextxp),
				guiTop + 5 + mc.fontRenderer.FONT_HEIGHT, 0xf0008000, 0xf000f000);

		drawGradientRect(guiLeft + 112, guiTop - 16, guiLeft + xSize - 16, guiTop, 0x80202020, 0x80202020);
		drawCenteredString(fontRenderer, String.format("x%d", booster), guiLeft + 112 + (xSize - 128) / 2, guiTop - 12,
				0xffffff);

		GlStateManager.pushMatrix();
		GlStateManager.translate(guiLeft, guiTop, 0);
		String lvString = String.format("Lv. %d", servant.getLevel());
		mc.fontRenderer.drawString(lvString, 90, 6, 0xff404040);
		mc.fontRenderer.drawString(lvString, 89, 5, 0xfff0f0f0);
		GlStateManager.popMatrix();
		GlStateManager.enableLighting();
		GlStateManager.enableDepth();
		GlStateManager.colorMask(true, true, true, true);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		// Handles toggling animation in gui, makes sure they are visible
		for (int cnt = 0; cnt < 4; cnt++) {
			if(optionButton[cnt] != null) {
				optionButton[cnt].visible = true;
				optionButton[cnt].toggle(servant.isVisiblityEnabled(cnt + 1));
			}
		}

		swimbutton.visible = true;
		swimbutton.toggle = servant.swimmingEnabled();
		frdmbutton.visible = true;
		frdmbutton.toggle = servant.isFreedomMode();

		int ii = mouseX - guiLeft;
		int jj = mouseY - guiTop;

		if (ii > 25 && ii < 78 && jj > 7 && jj < 60) {
			// txbutton[0].visible = true;
			// txbutton[1].visible = true;
			// txbutton[2].visible = true;
			// txbutton[3].visible = true;
			// selectbutton.visible = true;

			GL11.glPushMatrix();
			GL11.glTranslatef(mouseX - ii, mouseY - jj, 0.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			GL11.glPopMatrix();
		} else {
			// txbutton[0].visible = false;
			// txbutton[1].visible = false;
			// txbutton[2].visible = false;
			// txbutton[3].visible = false;
			// selectbutton.visible = false;
		}
		// Displays text by exp booster
		if (ii > 96 && ii < xSize && jj > -16 && jj < 0) {
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.colorMask(true, true, true, false);
			String str = I18n.format("littleMaidMob.gui.text.expboost");
			int width = fontRenderer.getStringWidth(str);
			int centerx = guiLeft + 48 + xSize / 2;
			drawGradientRect(centerx - width / 2 - 4, guiTop, centerx + width / 2 + 4,
					guiTop + fontRenderer.FONT_HEIGHT, 0xc0202020, 0xc0202020);
			drawCenteredString(fontRenderer, str, centerx, guiTop, 0xffffff);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.colorMask(true, true, true, true);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		// TODO I think implement changing of maid skin
		/*
		 * // 26,8-77,59 int ii = i - guiLeft; int jj = j - guiTop;
		 * 
		 * // TODO:メイドアセンブル画面を作る if (ii > 25 && ii < 78 && jj > 7 && jj < 60) { //
		 * 伽羅表示領域 if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54)) { // Shift+で逆周り
		 * LMM_Client.setPrevTexturePackege(entitylittlemaid, k); } else {
		 * LMM_Client.setNextTexturePackege(entitylittlemaid, k); }
		 * LMM_Client.setTextureValue(entitylittlemaid); }
		 */
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		System.out.println("Button: " + button.id);
		int booster = servant.getExpBooster();
		switch (button.id) {
		case 100:
			// theMaid.setNextTexturePackege(0);
			// theMaid.setTextureNames();
			// theMaid.requestChangeRenderParamTextureName();
			break;
		case 101:
			// theMaid.setPrevTexturePackege(0);
			// theMaid.setTextureNames();
			// theMaid.requestChangeRenderParamTextureName();
			break;
		case 110:
			// theMaid.setNextTexturePackege(1);
			// theMaid.setTextureNames();
			// theMaid.requestChangeRenderParamTextureName();
			break;
		case 111:
			// theMaid.setPrevTexturePackege(1);
			// theMaid.setTextureNames();
			// theMaid.requestChangeRenderParamTextureName();
			break;
		case 200:
			// int ldye = 0;
			// if (mc.player.capabilities.isCreativeMode) {
			// ldye = 0xffff;
			// } else {
			// for (ItemStack lis : mc.player.inventory.mainInventory) {
			// if (lis != null && lis.getItem() == Items.DYE) {
			// ldye |= (1 << (15 - lis.getItemDamage()));
			// }
			// }
			// }
			// isChangeTexture = false;
			// mc.displayGuiScreen(new LMM_GuiTextureSelect(this, theMaid, ldye, true));
			break;
		case 300:
			// visarmorbutton[0].toggle = !visarmorbutton[0].toggle;
			// setArmorVisible();
			break;
		case 301:
			// visarmorbutton[1].toggle = !visarmorbutton[1].toggle;
			// setArmorVisible();
			break;
		case 302:
			// visarmorbutton[2].toggle = !visarmorbutton[2].toggle;
			// setArmorVisible();
			break;
		case 303:
			servant.setArmorVisibility(4, ((GuiServantButton) button).toggle());
			break;
		case 310:
			swimbutton.toggle = !swimbutton.toggle;
			break;
		case 311:
			frdmbutton.toggle = !frdmbutton.toggle;
			servant.setFreedomMode(frdmbutton.toggle);
			break;
		case 320:
			booster -= 2;
		case 321:
			// theMaid.setExpBooster(++booster);
			// theMaid.recallExpBoost();
			break;
		}
	}

	protected void setArmorVisible() {
		// TODO implement armor visibility toggle
		
		// servant.setArmorVisibility(2, visarmorbutton[1].toggle);
		// servant.setArmorVisibility(3, visarmorbutton[2].toggle);
		// servant.setArmorVisibility(4, visarmorbutton[3].toggle);
	}

	// Draws the health and air of the entity
	protected void drawHealthArmor(int par1, int par2) {
		zdoctor.lazylibrary.client.util.RenderHelper.bindTexture(Gui.ICONS);

		boolean var3 = servant.hurtResistantTime / 3 % 2 == 1;

		if (servant.hurtResistantTime < 10) {
			var3 = false;
		}

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		this.rand.setSeed(updateCounter * 312871);
		// FoodStats var7 = entitylittlemaid.getFoodStats();
		// int var8 = var7.getFoodLevel();
		// int var9 = var7.getPrevFoodLevel();
		IAttributeInstance var10 = servant.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);
		float var14 = (float) var10.getAttributeValue();
		float var15 = servant.getAbsorptionAmount();
		int var16 = MathHelper.ceil((var14 + var15) / 2.0F / 10.0F);
		int var17 = Math.max(10 - (var16 - 2), 3);
		float var19 = var15;
		int var21 = -1;

		if (servant.isPotionActive(Potion.getPotionFromResourceLocation("regeneration"))) {
			var21 = updateCounter % MathHelper.ceil(var14 + 5.0F);
		}

		int ldrawx;
		int ldrawy;

		// LP
		// Health
		int lhealth = MathHelper.ceil(servant.getHealth());
		int llasthealth = lhealth;
		for (int li = MathHelper.ceil((var14 + var15) / 2.0F) - 1; li >= 0; --li) {
			int var23 = 16;
			if (servant.isPotionActive(MobEffects.POISON)) {
				var23 += 36;
			} else if (servant.isPotionActive(Potion.getPotionFromResourceLocation("wither"))) {
				var23 += 72;
			}

			int var25 = MathHelper.ceil((li + 1) / 10.0F);
			ldrawx = guiLeft + li % 10 * 8 + 86;
			ldrawy = guiTop + 5 + var25 * var17;

			if (lhealth <= 4) {
				ldrawy += this.rand.nextInt(2);
			}
			if (li == var21) {
				ldrawy -= 2;
			}

			this.drawTexturedModalRect(ldrawx, ldrawy, var3 ? 25 : 16, 0, 9, 9);

			if (var3) {
				if (li * 2 + 1 < llasthealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 54, 0, 9, 9);
				}
				if (li * 2 + 1 == llasthealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 63, 0, 9, 9);
				}
			}

			if (var19 > 0.0F) {
				if (var19 == var15 && var15 % 2.0F == 1.0F) {
					this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 153, 0, 9, 9);
				} else {
					this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 144, 0, 9, 9);
				}

				var19 -= 2.0F;
			} else {
				if (li * 2 + 1 < lhealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 36, 0, 9, 9);
				}
				if (li * 2 + 1 == lhealth) {
					this.drawTexturedModalRect(ldrawx, ldrawy, var23 + 45, 0, 9, 9);
				}
			}
		}

		boolean flag = false;
		// Armor Points
		int larmor = servant.getTotalArmorValue();
		ldrawy = guiTop + 26;
		for (int li = 0; li < 10; ++li) {
			if (larmor > 0) {
				flag = true;
				ldrawx = guiLeft + li * 8 + 86;

				if (li * 2 + 1 < larmor) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 34, 9, 9, 9);
				}
				if (li * 2 + 1 == larmor) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 25, 9, 9, 9);
				}
				if (li * 2 + 1 > larmor) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 16, 9, 9, 9);
				}
			}
		}

		if (flag)
			ldrawy += 10;

		// Air
		if (servant.isInsideOfMaterial(Material.WATER)) {
			int var23 = servant.getAir();
			int var35 = MathHelper.ceil((var23 - 2) * 10.0D / 300.0D);
			int var25 = MathHelper.ceil(var23 * 10.0D / 300.0D) - var35;

			for (int var26 = 0; var26 < var35 + var25; ++var26) {
				ldrawx = guiLeft + var26 * 8 + 86;
				if (var26 < var35) {
					this.drawTexturedModalRect(ldrawx, ldrawy, 16, 18, 9, 9);
				} else {
					this.drawTexturedModalRect(ldrawx, ldrawy, 25, 18, 9, 9);
				}
			}
		}
	}

	@Override
	protected void updateActivePotionEffects() {
		this.maidHasActivePotionEffects = false;
		boolean hasVisibleEffect = false;
		for (PotionEffect potioneffect : this.servant.getActivePotionEffects()) {
			Potion potion = potioneffect.getPotion();
			if (potion.shouldRender(potioneffect)) {
				hasVisibleEffect = true;
				break;
			}
		}
		if (this.servant.getActivePotionEffects().isEmpty() || !hasVisibleEffect) {
			this.maidHasActivePotionEffects = false;
		} else {
			this.maidHasActivePotionEffects = true;
		}
	}

	private void displayDebuffEffects() {
		updateActivePotionEffects();
		int lx = guiLeft - 124;
		int ly = guiTop;

		if (maidHasActivePotionEffects == false)
			return;
		Collection collection = servant.getActivePotionEffects();
		int lh = 33;
		if (collection.size() > 5) {
			lh = 132 / (collection.size() - 1);
		}
		for (Iterator iterator = servant.getActivePotionEffects().iterator(); iterator.hasNext();) {
			PotionEffect potioneffect = (PotionEffect) iterator.next();
			Potion potion = potioneffect.getPotion();
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			zdoctor.lazylibrary.client.util.RenderHelper
					.bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
			drawTexturedModalRect(lx, ly, 0, ySizebk, 140, 32);

			if (potion.hasStatusIcon()) {
				int i1 = potion.getStatusIconIndex();
				drawTexturedModalRect(lx + 6, ly + 7, 0 + (i1 % 8) * 18, ySizebk + 32 + (i1 / 8) * 18, 18, 18);
			}
			String ls = I18n.format(potion.getName());
			if (potioneffect.getAmplifier() > 0) {
				ls = (new StringBuilder()).append(ls).append(" ").append(I18n.format(
						(new StringBuilder()).append("potion.potency.").append(potioneffect.getAmplifier()).toString()))
						.toString();
			}
			mc.fontRenderer.drawString(ls, lx + 10 + 18, ly + 6, 0xffffff);
			String s1 = Potion.getPotionDurationString(potioneffect, 1);
			mc.fontRenderer.drawString(s1, lx + 10 + 18, ly + 6 + 10, 0x7f7f7f);
			ly += lh;
		}
	}
}
