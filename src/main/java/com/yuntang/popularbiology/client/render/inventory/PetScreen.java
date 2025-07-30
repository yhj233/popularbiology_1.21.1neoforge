package com.yuntang.popularbiology.client.render.inventory;

import com.yuntang.popularbiology.client.render.button.TextureChangeButton;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.pet.JiyiPet;
import com.yuntang.popularbiology.entity.pet.WusaqiPet;
import com.yuntang.popularbiology.entity.pet.XiaobaPet;
import com.yuntang.popularbiology.init.InitNetwork;
import net.neoforged.neoforge.network.PacketDistributor; // 导入PacketDistributor
import com.yuntang.popularbiology.network.SyncDataC2SPacket;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class PetScreen extends AbstractContainerScreen<PetContainerMenu> {
   private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath("popularbiology", "textures/gui/entity_gui.png");

   public PetScreen(PetContainerMenu container, Inventory inventory, Component component) {
      super(container, inventory, component);
   }

   protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
      guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
      InventoryScreen.renderEntityInInventoryFollowsMouse(guiGraphics, this.leftPos + 51, this.topPos + 60, this.leftPos + 51, this.topPos + 60, 30, 0.0f, (float)(this.leftPos + 51) - (float)mouseX, (float)(this.topPos + 60 - 50) - (float)mouseY, ((PetContainerMenu)this.menu).getPet());
   }

   public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
      this.renderBackground(guiGraphics, mouseX, mouseY, delta);
      super.render(guiGraphics, mouseX, mouseY, delta);
      this.renderTooltip(guiGraphics, mouseX, mouseY);
   }

   protected void init() {
      super.init();
      BasePet pet = ((PetContainerMenu)this.menu).getPet();
      if (pet instanceof WusaqiPet || pet instanceof XiaobaPet || pet instanceof JiyiPet) {
         this.addRenderableWidget(new TextureChangeButton(this.leftPos + 7, this.topPos + 48, 18, 18, Component.literal("C"), Tooltip.create(Component.translatable("button.popularbiology.texture_change")), (button) -> {
            Map<SyncDataC2SPacket.DataType, Object> data = new EnumMap(SyncDataC2SPacket.DataType.class);
            data.put(SyncDataC2SPacket.DataType.PET_TEXTURE, ((PetContainerMenu)this.menu).getPet().getNextPetTexture());
            SyncDataC2SPacket packet = new SyncDataC2SPacket(pet.getId(), data);
            //InitNetwork.INSTANCE.sendToServer(packet);
             PacketDistributor.sendToServer(packet);
             //
         }));
      }

   }
}
