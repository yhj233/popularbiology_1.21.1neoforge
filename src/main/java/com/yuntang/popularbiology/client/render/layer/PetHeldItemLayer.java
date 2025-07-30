package com.yuntang.popularbiology.client.render.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yuntang.popularbiology.entity.BasePet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;

public class PetHeldItemLayer<T extends BasePet> extends BlockAndItemGeoLayer<T> {
   private ItemStack mainHandItem;

   public PetHeldItemLayer(GeoRenderer<T> renderer) {
      super(renderer);
   }

   public ItemStack getStackForBone(GeoBone bone, T animatable) {
      return "RightHandLocator".equals(bone.getName()) ? this.mainHandItem : null;
   }

   protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, T animatable) {
      return "RightHandLocator".equals(bone.getName()) ? ItemDisplayContext.THIRD_PERSON_RIGHT_HAND : ItemDisplayContext.NONE;
   }

   protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
      float[] scales = this.getScalingFactors(bone, animatable);
      poseStack.scale(scales[0], scales[1], scales[2]);
      if (stack.getItem() instanceof SwordItem || stack.getItem() instanceof HoeItem) {
         poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
      }

      if (stack.getItem() instanceof BowItem) {
         poseStack.translate(0.1F, -0.2F, -0.1F);
         poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
      }

      super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight, packedOverlay);
   }

   public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
      super.preRender(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
      this.mainHandItem = animatable.getMainHandItem();
   }

   private float[] getScalingFactors(GeoBone bone, T animatable) {
      return new float[]{0.8F, 0.8F, 0.8F};
   }
}
