package com.yuntang.popularbiology.effect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.entity.ai.attributes.AttributeMap;

import java.util.HashMap;
import java.util.Map;

public class WulayahaEffect extends MobEffect {
   private static final Logger LOGGER = LogManager.getLogger();
   private final Map<LivingEntity, BlockPos> lastLightPosMap = new HashMap<>();

   public WulayahaEffect() {
      super(MobEffectCategory.BENEFICIAL, 16776960);
   }

   @Override
   public boolean applyEffectTick(LivingEntity entity, int amplifier) {
      Level level = entity.level();
      if (level instanceof ServerLevel) {
         ServerLevel serverLevel = (ServerLevel) level;

         BlockPos currentPos = entity.blockPosition().above();  // 获取当前光源位置
         BlockPos lastPos = this.lastLightPosMap.get(entity);  // 获取上一个光源位置

         if (lastPos == null || !lastPos.equals(currentPos)) {
            if (lastPos != null) {
               serverLevel.setBlock(lastPos, Blocks.AIR.defaultBlockState(), 3);  // 清除旧光源
            }

            BlockState lightState = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, 15);
            serverLevel.setBlock(currentPos, lightState, 3);  // 设置新光源
            this.lastLightPosMap.put(entity, currentPos);  // 更新记录的光源位置
         }
      }
      return true;
   }

   @Override
   public void onEffectAdded(LivingEntity entity, int amplifier) {
      super.onEffectAdded(entity, amplifier);
      // 你可以在这里加入效果添加时的其他行为（例如播放音效等）
   }

   @Override
   public void removeAttributeModifiers(AttributeMap attributeMap) {
      super.removeAttributeModifiers(attributeMap);  // 调用父类的方法以清除属性修改器

      // 清理光源效果
      this.lastLightPosMap.forEach((entity, pos) -> {
         if (entity.level() instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel) entity.level();
            serverLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);  // 清除光源
         }
      });
      this.lastLightPosMap.clear();
   }
}
