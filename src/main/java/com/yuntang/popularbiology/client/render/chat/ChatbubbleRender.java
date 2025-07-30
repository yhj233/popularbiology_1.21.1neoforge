package com.yuntang.popularbiology.client.render.chat;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.yuntang.popularbiology.client.render.BaseRender;
import com.yuntang.popularbiology.entity.BasePet;
import java.util.function.Consumer;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderStateShard.ShaderStateShard;
import net.minecraft.client.renderer.RenderStateShard.TextureStateShard;
import net.minecraft.client.renderer.RenderStateShard.TransparencyStateShard;
import net.minecraft.client.renderer.RenderType.CompositeState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ChatbubbleRender {
   private static final ResourceLocation CHAT_BUBBLE_TEXTURE = ResourceLocation.fromNamespaceAndPath("popularbiology", "textures/gui/chatbubble/chatbubble.png");
   protected static final TransparencyStateShard NO_TRANSPARENCY = new TransparencyStateShard("no_transparency", RenderSystem::disableBlend, () -> {
   });

   public static void renderChatBubble(BaseRender renderer, BasePet pet, PoseStack matrixStack, MultiBufferSource buffer, int packedLight) {
      Font font = renderer.getFont();
      ChatbubbleRender.RenderChatBubbleData data = new ChatbubbleRender.RenderChatBubbleData(renderer, pet, matrixStack, buffer);
      Component TalkingMessage = Component.literal(pet.getChatbubbleMessage());
      int width = font.width(TalkingMessage);
      renderChatBubbleAndText(0, -32, width, data, (d) -> {
         renderText(TalkingMessage, 0, -32, width, packedLight, d);
      });
   }

   private static void renderChatBubbleAndText(int startX, int startY, int stringWidth, ChatbubbleRender.RenderChatBubbleData data, Consumer<ChatbubbleRender.RenderChatBubbleData> consumer) {
      float height = data.pet.getBbHeight() + 0.2F;
      int count = stringWidth / 20;
      int fullWidth = (count + 2) * 20;
      int leftStartX = startX - fullWidth / 2;
      int rightStartX = fullWidth / 2 - 20 + startX;
      int middleStartX = -fullWidth / 2 + 20 + startX;
      data.matrixStack.pushPose();
      data.matrixStack.mulPose(data.renderer.getDispatcher().cameraOrientation());
      data.matrixStack.translate(0.0F, height, 0.0F);
      data.matrixStack.scale(-0.025F, -0.025F, 0.025F);
      VertexConsumer vertexBuilder = data.buffer.getBuffer(chatBubbleRender(CHAT_BUBBLE_TEXTURE));
      DrawChatBubble(data.matrixStack, vertexBuilder, leftStartX, startY, 0.2F, 0, 0);

      for(int i = 0; i < count; ++i) {
         DrawChatBubble(data.matrixStack, vertexBuilder, middleStartX, startY, 0.2F, 1, 0);
         middleStartX += 20;
      }

      DrawChatBubble(data.matrixStack, vertexBuilder, rightStartX, startY, 0.2F, 2, 0);
      consumer.accept(data);
      data.matrixStack.popPose();
   }

   private static void renderText(Component chatText, int startX, int startY, int width, int packedLight, ChatbubbleRender.RenderChatBubbleData data) {
      Font font = data.renderer.getFont();
      font.drawInBatch(chatText, (float)(-width) / 2.0F + (float)startX, (float)(startY + 6), -16777216, false, data.matrixStack.last().pose(), data.buffer, DisplayMode.NORMAL, 0, packedLight);
   }

   private static RenderType chatBubbleRender(ResourceLocation locationIn) {
      ShaderStateShard shaderStateShard = new ShaderStateShard(GameRenderer::getPositionTexShader);
      CompositeState compositeState = CompositeState.builder().setShaderState(shaderStateShard).setTextureState(new TextureStateShard(locationIn, false, false)).setTransparencyState(NO_TRANSPARENCY).createCompositeState(true);
      return RenderType.create("chat_bubble", DefaultVertexFormat.POSITION_TEX, Mode.QUADS, 255, true, false, compositeState);
   }

   public static void DrawChatBubble(PoseStack matrixStack, VertexConsumer builder, int x, int y, float z, int uIndex, int vIndex) {
      float height = 20.0F;
      float width = 20.0F;
      Matrix4f matrix4f = matrixStack.last().pose();
      float u0 = (float)uIndex / 3.0F;
      float u1 = (float)(uIndex + 1) / 3.0F;
      float v0 = (float)vIndex / 2.0F;
      float v1 = (float)(vIndex + 1) / 2.0F;
      vertex(matrix4f, builder, (float)x, (float)y + height, z, u0, v1);
      vertex(matrix4f, builder, (float)x + width, (float)y + height, z, u1, v1);
      vertex(matrix4f, builder, (float)x + width, (float)y, z, u1, v0);
      vertex(matrix4f, builder, (float)x, (float)y, z, u0, v0);
   }

   private static void vertex(Matrix4f matrix4f, VertexConsumer builder, float x, float y, float z, float u, float v) {
      builder.addVertex(matrix4f, x, y, z).setUv(u, v);
   }

   private static class RenderChatBubbleData {
      private final BaseRender renderer;
      private final BasePet pet;
      private final PoseStack matrixStack;
      private final MultiBufferSource buffer;

      public RenderChatBubbleData(BaseRender renderer, BasePet pet, PoseStack matrixStack, MultiBufferSource buffer) {
         this.renderer = renderer;
         this.pet = pet;
         this.matrixStack = matrixStack;
         this.buffer = buffer;
      }
   }
}
