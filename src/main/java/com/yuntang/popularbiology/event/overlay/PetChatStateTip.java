package com.yuntang.popularbiology.event.overlay;

import com.mojang.blaze3d.systems.RenderSystem;
import com.yuntang.popularbiology.ClientListener;
import com.yuntang.popularbiology.client.aichat.ChatEditBox;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.init.InitNetwork;
import com.yuntang.popularbiology.network.SyncDataC2SPacket;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent; // 修改事件总线导入
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.lwjgl.glfw.GLFW;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent.Post;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
//import net.neoforged.neoforge.client.event
//
//import net.minecraftforge.client.event.RenderGuiOverlayEvent.Post;
//import net.minecraftforge.event.TickEvent.ClientTickEvent;
//import net.minecraftforge.event.TickEvent.Phase;


@EventBusSubscriber(
   modid = "popularbiology",
   value = Dist.CLIENT
)

public class PetChatStateTip {
   private static boolean isShowTalkTip = false;
 //  public static void sendToServer(String data) {
 //       // 从客户端发送负载到服务器
 //       PacketDistributor.send(new SyncDataC2SPacket(BasePet.getId(), data));
 //   }

   @SubscribeEvent
   public static void onRenderGuiOverlay(PlayerTickEvent.Pre event) {
  //    if (event.phase == Phase.POST) {
         Minecraft mc = Minecraft.getInstance();
         if (mc != null && mc.player != null) {
            isShowTalkTip = false;
            HitResult hitResult = mc.hitResult;
            if (hitResult != null && hitResult.getType() == Type.ENTITY) {
               if (hitResult instanceof EntityHitResult) {
                  EntityHitResult entityHit = (EntityHitResult)hitResult;
                  Entity targetEntity = entityHit.getEntity();
                  if (targetEntity instanceof BasePet) {
                     BasePet basePet = (BasePet)targetEntity;
                     if (basePet.getOwner() == mc.player) {
                        isShowTalkTip = true;
                        if (isShowTalkTip && mc.screen == null && ClientListener.TALK_KEY != null) {
                           long windowHandle = mc.getWindow().getWindow();
                           int keyCode = ClientListener.TALK_KEY.getKey().getValue();
                           if (GLFW.glfwGetKey(windowHandle, keyCode) == 1) {
                              Map<SyncDataC2SPacket.DataType, Object> data = new EnumMap(SyncDataC2SPacket.DataType.class);
                              data.put(SyncDataC2SPacket.DataType.IS_CHAT_STATE, true);
                              SyncDataC2SPacket packet = new SyncDataC2SPacket(basePet.getId(), data);
               //***!***//                         
                              PacketDistributor.sendToServer(packet);
               //***!***//                             
                              mc.setScreen(new ChatEditBox(basePet));
                              isShowTalkTip = false;
                           }
                        }

                     }
                  }
               }
            }
         }
      }


   @SubscribeEvent
   public static void onRenderGui(Post event) {
      Minecraft mc = Minecraft.getInstance();
      if (mc != null && mc.font != null && mc.getWindow() != null) {
        if (mc.screen instanceof ChatEditBox) {
            isShowTalkTip = false;
         }

         if (isShowTalkTip) {
            GuiGraphics guiGraphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
            Font font = mc.font;
            int screenWidth = mc.getWindow().getGuiScaledWidth();
            int screenHeight = mc.getWindow().getGuiScaledHeight();
            RenderSystem.enableBlend();
            String message = getMessage();
            guiGraphics.drawString(font, message, screenWidth / 2 - font.width(message) / 2, screenHeight / 2 + 20, 16777215);
            RenderSystem.disableBlend();
         }
      }
   }

   private static String getMessage() {
      if (ClientListener.TALK_KEY == null) {
         return "按 ? 键开始对话";
      } else {
         String keyName = ClientListener.TALK_KEY.getTranslatedKeyMessage().getString();
         return "按 " + keyName + " 键开始对话";
      }
   }
}
