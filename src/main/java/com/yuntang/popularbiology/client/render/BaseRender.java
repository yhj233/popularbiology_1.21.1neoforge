package com.yuntang.popularbiology.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuntang.popularbiology.client.render.chat.ChatbubbleRender;
import com.yuntang.popularbiology.client.render.layer.PetHeldItemLayer;
import com.yuntang.popularbiology.entity.BasePet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BaseRender<T extends BasePet> extends GeoEntityRenderer<T> {
   public BaseRender(Context ctx, GeoModel<T> model) {
      super(ctx, model);
      this.shadowRadius = 0.5F;
      this.addRenderLayer(new PetHeldItemLayer(this));
   }

   public void render(T pet, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
      if (pet.getChatbubbleMessageTime() > 0) {
         ChatbubbleRender.renderChatBubble(this, pet, poseStack, bufferSource, packedLight);
      }

      super.render(pet, entityYaw, partialTick, poseStack, bufferSource, packedLight);
   }

   public EntityRenderDispatcher getDispatcher() {
      return this.entityRenderDispatcher;
   }
}
