package com.yuntang.popularbiology.client.render.button;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class TextureChangeButton extends AbstractButton {
   private static final ResourceLocation WIDGETS_LOCATION = ResourceLocation.fromNamespaceAndPath("minecraft", "textures/gui/widgets.png");
   protected final TextureChangeButton.OnPress onPress;

   public TextureChangeButton(int x, int y, int width, int height, Component text, Tooltip tooltip, TextureChangeButton.OnPress onPress) {
      super(x, y, width, height, text);
      this.onPress = onPress;
      this.setTooltip(tooltip);
   }

   public void onPress() {
      this.onPress.onPress(this);
   }

   protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
      this.defaultButtonNarrationText(pNarrationElementOutput);
   }

   protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
      Minecraft minecraft = Minecraft.getInstance();
      pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
      RenderSystem.enableBlend();
      RenderSystem.enableDepthTest();
      // Manually implement nine-sliced rendering
      int x = this.getX();
      int y = this.getY();
      int width = this.getWidth();
      int height = this.getHeight();
      int u = 0;
      int v = this.getTextureY();
      int cornerSize = 20;
      int borderSize = 4;
      int textureWidth = 200;
      int textureHeight = 20;
      
      // Top-left corner
      pGuiGraphics.blit(WIDGETS_LOCATION, x, y, cornerSize, cornerSize, u, v, cornerSize, cornerSize, 256, 256);
      // Top-right corner
      pGuiGraphics.blit(WIDGETS_LOCATION, x + width - cornerSize, y, cornerSize, cornerSize, u + textureWidth - cornerSize, v, cornerSize, cornerSize, 256, 256);
      // Bottom-left corner
      pGuiGraphics.blit(WIDGETS_LOCATION, x, y + height - cornerSize, cornerSize, cornerSize, u, v + textureHeight - cornerSize, cornerSize, cornerSize, 256, 256);
      // Bottom-right corner
      pGuiGraphics.blit(WIDGETS_LOCATION, x + width - cornerSize, y + height - cornerSize, cornerSize, cornerSize, u + textureWidth - cornerSize, v + textureHeight - cornerSize, cornerSize, cornerSize, 256, 256);
      
      // Top border
      pGuiGraphics.blit(WIDGETS_LOCATION, x + cornerSize, y, width - 2 * cornerSize, borderSize, u + cornerSize, v, textureWidth - 2 * cornerSize, borderSize, 256, 256);
      // Bottom border
      pGuiGraphics.blit(WIDGETS_LOCATION, x + cornerSize, y + height - borderSize, width - 2 * cornerSize, borderSize, u + cornerSize, v + textureHeight - borderSize, textureWidth - 2 * cornerSize, borderSize, 256, 256);
      // Left border
      pGuiGraphics.blit(WIDGETS_LOCATION, x, y + cornerSize, borderSize, height - 2 * cornerSize, u, v + cornerSize, borderSize, textureHeight - 2 * cornerSize, 256, 256);
      // Right border
      pGuiGraphics.blit(WIDGETS_LOCATION, x + width - borderSize, y + cornerSize, borderSize, height - 2 * cornerSize, u + textureWidth - borderSize, v + cornerSize, borderSize, textureHeight - 2 * cornerSize, 256, 256);
      
      // Center
      pGuiGraphics.blit(WIDGETS_LOCATION, x + cornerSize, y + cornerSize, width - 2 * cornerSize, height - 2 * cornerSize, u + cornerSize, v + cornerSize, textureWidth - 2 * cornerSize, textureHeight - 2 * cornerSize, 256, 256);
      
      pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
      int i = this.getFGColor();
      this.renderString(pGuiGraphics, minecraft.font, i | Mth.ceil(this.alpha * 255.0F) << 24);
   }

   private int getTextureY() {
      int i = 1;
      if (!this.active) {
         i = 0;
      } else if (this.isHovered()) {
         i = 2;
      }

      return 46 + i * 20;
   }

   @OnlyIn(Dist.CLIENT)
   public interface OnPress {
      void onPress(TextureChangeButton var1);
   }
}
