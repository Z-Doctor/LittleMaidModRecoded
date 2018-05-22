package zdoctor.littlemaidmod.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import zdoctor.littlemaidmod.client.gui.inventory.GuiServantInventory;

public class GuiServantButton extends GuiButton {
	public static final ResourceLocation GUI_TOPBUTTON_RESOURCE = GuiServantInventory.SERVANT_INVENTORY_GUI;

	protected int xOffset;
	protected int yOffset;

	protected String showText;

	protected boolean toggle = true;

	public GuiServantButton(int buttonId, int posX, int posY, int xOffset, int yOffset, String hoverText) {
		this(buttonId, posX, posY, xOffset, yOffset, 16, 16, hoverText);
	}

	public GuiServantButton(int buttonId, int x, int y, int xOffset, int yOffset, int widthIn, int heightIn,
			String hoverText) {
		super(buttonId, x, y, widthIn, heightIn, "");
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		showText = hoverText;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			mc.getTextureManager().bindTexture(GUI_TOPBUTTON_RESOURCE);
			 GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;
			int i = this.getHoverState(this.hovered);

			// GlStateManager.enableBlend();
			// GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
			// GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
			// GlStateManager.SourceFactor.ONE,
			// GlStateManager.DestFactor.ZERO);
			// GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
			// GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.x, this.y, this.xOffset + this.width * (toggle ? 0 : 1), this.yOffset, this.width, this.height);
//			this.drawTexturedModalRect(this.x, this.y, this.xOffset, this.yOffset, this.width / 2, this.height);
			// this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width
			// / 2, 46 + i * 20,
			// this.width / 2, this.height);
			// this.mouseDragged(mc, mouseX, mouseY);
//			int j = 14737632;

//			if (packedFGColour != 0) {
//				j = packedFGColour;
//			} else if (!this.enabled) {
//				j = 10526880;
//			} else if (this.hovered) {
//				j = 16777120;
//			}

		}
	}

	public boolean toggle() {
		toggle = !toggle;
		return toggle;
	}

	public boolean toggle(boolean b) {
		toggle = b;
		return b;
	}

	public boolean getToggle() {
		return toggle;
	}

	@Override
	public void drawButtonForegroundLayer(int mouseX, int mouseY) {
		super.drawButtonForegroundLayer(mouseX, mouseY);
		showHoverText(Minecraft.getMinecraft(), mouseX, mouseY);
	}

	protected void showHoverText(Minecraft mcMinecraft, int mx, int my) {
		if (hovered) {
			GlStateManager.disableLighting();
			GlStateManager.disableDepth();
			GlStateManager.colorMask(true, true, true, false);
			FontRenderer fRenderer = mcMinecraft.getRenderManager().getFontRenderer();
			int lcolor = 0xc0000000;
			String viewString = I18n.format(showText + getTaleString());
			int fx = fRenderer.getStringWidth(viewString);
			drawGradientRect(mx + 4, my + 4, mx + 4 + fx + 4, my + 4 + 8 + 4, lcolor, lcolor);
			drawCenteredString(fRenderer, viewString, mx + fx / 2 + 6, my + 6, 0xffffffff);
			GlStateManager.enableLighting();
			GlStateManager.enableDepth();
			GlStateManager.colorMask(true, true, true, true);
		}
	}

	protected String getTaleString() {
		return toggle ? ".hide" : ".show";
	}

}
