package com.yuntang.popularbiology.client.render.button;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ChatSettingButton extends AbstractButton {
   private static final ResourceLocation BUTTON_TEXTURE = ResourceLocation.fromNamespaceAndPath("popularbiology", "textures/gui/button/chat_setting_button.png");
   private final OnPress onPress;

   public ChatSettingButton(int x, int y, Tooltip tooltip, OnPress onPress) {
      super(x, y, 16, 16, Component.empty());
      this.onPress = onPress;
      this.setTooltip(tooltip);
   }

   public void onPress() {
      this.onPress.onPress(this);
   }

   @FunctionalInterface
   public interface OnPress {
      void onPress(ChatSettingButton button);
   }

   protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
      this.defaultButtonNarrationText(narrationElementOutput);
   }

   protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      int v = 0;
      if (this.isHovered()) {
         v += 16;
      }

      guiGraphics.blit(BUTTON_TEXTURE, this.getX(), this.getY(), 0, v, this.width, this.height, 16, 32);
   }
}
