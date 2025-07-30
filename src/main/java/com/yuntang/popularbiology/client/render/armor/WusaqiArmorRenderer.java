package com.yuntang.popularbiology.client.render.armor;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuntang.popularbiology.item.WusaqiArmorItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WusaqiArmorRenderer extends GeoArmorRenderer<WusaqiArmorItem> {
   public WusaqiArmorRenderer() {
      super(new DefaultedItemGeoModel(ResourceLocation.fromNamespaceAndPath("popularbiology", "armor/wusaqi_armor"))); 
   }

   public void actuallyRender(PoseStack poseStack, WusaqiArmorItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
      poseStack.scale(1.5F, 1.5F, 1.5F);
      poseStack.translate(0.0D, -0.48D, 0.0D);
      super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, colour);
   }
}
