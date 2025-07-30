package com.yuntang.popularbiology.client.model;

import com.yuntang.popularbiology.entity.BasePet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BaseModel<T extends BasePet> extends GeoModel<T> {
   private static final Logger LOGGER = LogManager.getLogger();

   public ResourceLocation getModelResource(T animatable) {
      return ResourceLocation.fromNamespaceAndPath("popularbiology", String.format("geo/%s.geo.json", animatable.getPetName()));
   }

   public ResourceLocation getTextureResource(T animatable) {
      return animatable.getPetTexture() == 0 ? ResourceLocation.fromNamespaceAndPath("popularbiology", String.format("textures/entity/%s.png", animatable.getPetName())) : ResourceLocation.fromNamespaceAndPath("popularbiology", String.format("textures/entity/%s_night.png", animatable.getPetName()));
   }

   public ResourceLocation getAnimationResource(T animatable) {
      return ResourceLocation.fromNamespaceAndPath("popularbiology", String.format("animations/%s.animation.json", animatable.getPetName()));
   }

   public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
      super.setCustomAnimations(animatable, instanceId, animationState);
      EntityModelData modelData = (EntityModelData)animationState.getData(DataTickets.ENTITY_MODEL_DATA);
      GeoBone headBone = this.getAnimationProcessor().getBone("AllHead");
      if (headBone != null) {
         float netHeadYaw = modelData.netHeadYaw();
         float headPitch = modelData.headPitch();
         headBone.setRotY(netHeadYaw * 0.017453292F);
         headBone.setRotX(headPitch * 0.017453292F);
      }

      GeoBone leftEarBone = this.getAnimationProcessor().getBone("LeftEar");
      GeoBone rightEarBone = this.getAnimationProcessor().getBone("RightEar");
      GeoBone tailBone = this.getAnimationProcessor().getBone("tail");
      double ageInTicks = (Double)animationState.getData(DataTickets.TICK);
      float limbSwingAmount = animationState.getLimbSwingAmount();
      float breathingSpeed = 0.1F;
      float earSwingAmount = 0.1F;
      float earTwistAmount = 0.1F;
      float earBackwardSwing = -limbSwingAmount * 1.0F;
      if (leftEarBone != null) {
         leftEarBone.setRotY(Mth.cos((float)ageInTicks * breathingSpeed) * earSwingAmount - earBackwardSwing);
         leftEarBone.setRotZ(Mth.sin((float)ageInTicks * breathingSpeed) * earTwistAmount);
      }

      if (rightEarBone != null) {
         rightEarBone.setRotY(-Mth.cos((float)ageInTicks * breathingSpeed) * earSwingAmount + earBackwardSwing);
         rightEarBone.setRotZ(-Mth.sin((float)ageInTicks * breathingSpeed) * earTwistAmount);
      }

      if (tailBone != null) {
         tailBone.setRotY(Mth.cos((float)ageInTicks * breathingSpeed) * 0.15F);
      }

   }
}
