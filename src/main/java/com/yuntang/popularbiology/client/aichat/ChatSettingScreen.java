package com.yuntang.popularbiology.client.aichat;

import com.yuntang.popularbiology.config.ClientConfig;
import com.yuntang.popularbiology.entity.BasePet;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.Font;

public class ChatSettingScreen extends Screen {
   private EditBox apiKeyInput;
   private EditBox appIdInput;
   private EditBox apiUrlInput;
   private CycleButton<String> modelButton;
   private Checkbox contextCheckbox;
   private BasePet pet;
   private final Screen lastScreen;
   private Button saveButton;
   private static final List<String> MODELS = Arrays.asList("deepseek-v3", "deepseek-r1", "ernie-4.0-turbo-8k");

   public ChatSettingScreen(Screen lastScreen, BasePet pet) {
      super(Component.translatable("chat.setting.title"));
      this.lastScreen = lastScreen;
      this.pet = pet;
   }

   protected void init() {
      int centerY = this.height / 4;
      int spacing = 35;
      this.contextCheckbox = Checkbox.builder(Component.literal("开启上下文(可能会消耗更多token，如果是免费的无所谓啦(*^▽^*))"), this.font)
               .pos(width / 2 - 100, centerY - 40)
               .maxWidth(20)
               .selected((Boolean) ClientConfig.ENABLE_CONTEXT.get())
               .build();
      this.apiKeyInput = new EditBox(this.font, this.width / 2 - 100, centerY, 200, 15, Component.literal("API Key"));
      this.apiKeyInput.setMaxLength(512);
      this.apiKeyInput.setValue((String)ClientConfig.CHAT_API_KEY.get());
      this.apiUrlInput = new EditBox(this.font, this.width / 2 - 100, centerY + spacing, 200, 15, Component.literal("请求的URL"));
      this.apiUrlInput.setMaxLength(512);
      this.apiUrlInput.setValue((String)ClientConfig.CHAT_API_URL.get());
      this.appIdInput = new EditBox(this.font, this.width / 2 - 100, centerY + spacing * 2, 200, 15, Component.literal("App ID"));
      this.appIdInput.setMaxLength(128);
      this.appIdInput.setValue((String)ClientConfig.CHAT_APP_ID.get());
this.modelButton = CycleButton.<String>builder((value) -> {
    return Component.literal(String.valueOf(value));
}).withValues(MODELS).withInitialValue(
    MODELS.contains(ClientConfig.CHAT_MODEL.get()) ? 
    ClientConfig.CHAT_MODEL.get() : MODELS.get(0)
).create(this.width / 2 - 100, centerY + spacing * 3, 200, 15, Component.literal("模型选择:"));
      Button clearContextButton = Button.builder(Component.translatable("chat.setting.clear_context"), (button) -> {
         this.pet.getChatMannager().clearMessages();
      }).bounds(this.width / 2 - 100, centerY + spacing * 4, 200, 15).build();
      this.saveButton = Button.builder(Component.translatable("gui.done"), (button) -> {
         this.saveConfig();
         this.minecraft.setScreen(this.lastScreen);
      }).bounds(this.width / 2 - 100, centerY + spacing * 5 - 10, 200, 15).build();
      this.addWidget(this.apiKeyInput);
      this.addWidget(this.apiUrlInput);
      this.addWidget(this.appIdInput);
      this.addWidget(this.saveButton);
      this.addRenderableWidget(clearContextButton);
      this.addRenderableWidget(this.modelButton);
      this.addRenderableWidget(this.contextCheckbox);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private void saveConfig() {
      ClientConfig.CHAT_API_KEY.set(this.apiKeyInput.getValue());
      ClientConfig.CHAT_APP_ID.set(this.appIdInput.getValue());
      ClientConfig.ENABLE_CONTEXT.set(this.contextCheckbox.selected());
      ClientConfig.CHAT_MODEL.set((String)this.modelButton.getValue());
      ClientConfig.CHAT_API_URL.set(this.apiUrlInput.getValue());
      ClientConfig.SPEC.save();
   }

   public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
      this.renderBackground(graphics, mouseX, mouseY, partialTick);
      int centerY = this.height / 4;
      int spacing = 35;
      graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 16777215);
      graphics.drawString(this.font, "API Key:", this.width / 2 - 100, centerY - 15, 16777215);
      graphics.drawString(this.font, "请求的URL:", this.width / 2 - 100, centerY + spacing - 15, 16777215);
      graphics.drawString(this.font, "App ID:", this.width / 2 - 100, centerY + spacing * 2 - 15, 16777215);
      this.apiKeyInput.render(graphics, mouseX, mouseY, partialTick);
      this.appIdInput.render(graphics, mouseX, mouseY, partialTick);
      this.saveButton.render(graphics, mouseX, mouseY, partialTick);
      this.apiUrlInput.render(graphics, mouseX, mouseY, partialTick);
      this.modelButton.render(graphics, mouseX, mouseY, partialTick);
      super.render(graphics, mouseX, mouseY, partialTick);
   }
}
