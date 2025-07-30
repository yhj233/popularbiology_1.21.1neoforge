package com.yuntang.popularbiology.client.aichat;

import com.yuntang.popularbiology.client.render.button.ChatSettingButton;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.init.InitNetwork;
import com.yuntang.popularbiology.network.SyncDataC2SPacket;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class ChatEditBox extends Screen {
   protected EditBox input;
   private final BasePet pet;
   private Component hintText = Component.translatable("chat.editBox.hint").withStyle((style) -> {
      return style.withColor(-2139193730).withItalic(true);
   });

   public ChatEditBox(BasePet pet) {
      super(Component.translatable("chat_screen.title"));
      this.pet = pet;
   }

   protected void init() {
      int chatWidth = (int)((double)this.width * 0.5D);
      int chatHeight = 10;
      int offsetFromHotbar = 40;
      int posX = (this.width - chatWidth) / 2;
      int posY = this.height - 45 - offsetFromHotbar;
      this.input = new EditBox(this.font, posX, posY, chatWidth, chatHeight, Component.translatable("chat.editBox"));
      this.input.setMaxLength(256);
      this.input.setBordered(false);
      this.input.setCanLoseFocus(true);
      this.addWidget(this.input);
      this.setInitialFocus(this.input);
      int buttonX = posX + chatWidth + 4;
      int buttonY = posY - 3;
      this.addRenderableWidget(new ChatSettingButton(buttonX, buttonY, Tooltip.create(Component.translatable("button.popularbiology.chat_setting")), (button) -> {
         Minecraft.getInstance().setScreen(new ChatSettingScreen(Minecraft.getInstance().screen, this.pet));
      }));
   }

   public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
      if (keyCode == 256) {
         Minecraft.getInstance().setScreen((Screen)null);
         return true;
      } else if (keyCode != 257 && keyCode != 335) {
         return this.input.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
      } else {
         String chatText = this.input.getValue();
         this.sendChatData(chatText);
         Minecraft.getInstance().setScreen((Screen)null);
         return true;
      }
   }

   private void sendChatData(String text) {
      this.pet.getChatMannager().addMessage("user", text);
      this.pet.getChatMannager().sendRequest();
   }

   public void tick() {
      this.input.canConsumeInput();
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
      int bgColor = 1426063360;
      guiGraphics.fill(this.input.getX() - 2, this.input.getY() - 2, this.input.getX() + this.input.getWidth() + 2, this.input.getY() + this.input.getHeight() + 2, bgColor);
      this.input.render(guiGraphics, mouseX, mouseY, partialTick);
      if (this.hintText != null && this.input.getValue().isEmpty()) {
         int textWidth = this.font.width(this.hintText);
         int centerX = this.input.getX() + (this.input.getWidth() - textWidth) / 2;
         int centerY = this.input.getY() + (this.input.getHeight() - 8) / 2;
         guiGraphics.drawString(this.font, this.hintText, centerX, centerY, -1);
      }

      super.render(guiGraphics, mouseX, mouseY, partialTick);
   }

   public void removed() {
      Map<SyncDataC2SPacket.DataType, Object> data = new EnumMap(SyncDataC2SPacket.DataType.class);
      data.put(SyncDataC2SPacket.DataType.IS_CHAT_STATE, false);
      SyncDataC2SPacket packet = new SyncDataC2SPacket(this.pet.getId(), data);
      // 发送数据包到服务器
      PacketDistributor.sendToServer(packet);
      //
      this.input.setValue("");
   }

   protected void updateNarrationState(NarrationElementOutput narrationOutput) {
      narrationOutput.add(NarratedElementType.TITLE, this.getTitle());
   }

   public boolean isPauseScreen() {
      return false;
   }
}
