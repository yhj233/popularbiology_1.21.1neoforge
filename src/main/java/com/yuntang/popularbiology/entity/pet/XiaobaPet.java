package com.yuntang.popularbiology.entity.pet;

import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.interact.BaseInteract;
import com.yuntang.popularbiology.entity.sound.PetSoundManager;
import com.yuntang.popularbiology.init.InitSound;
import com.yuntang.popularbiology.item.XiaobaTaoFaBnag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;


public class XiaobaPet extends BasePet {
   public XiaobaPet(EntityType<? extends BasePet> type, Level level) {
      super(type, level, "xiaoba");
   }

   public BaseInteract getInteractAction() {
      return new BaseInteract();
   }

   public boolean doHurtTarget(Entity pEntity) {
      if (this.getMainHandItem().getItem() instanceof XiaobaTaoFaBnag && pEntity instanceof LivingEntity) {
         LivingEntity livingTarget = (LivingEntity)pEntity;
         float knockbackStrength = 5.0F;
         double deltaX = this.getX() - pEntity.getX();
         double deltaZ = this.getZ() - pEntity.getZ();
         livingTarget.knockback((double)knockbackStrength, deltaX, deltaZ);
      }

      return super.doHurtTarget(pEntity);
   }

   public PetSoundManager getSoundManager() {
      return (new PetSoundManager.Builder()).tameSound((SoundEvent)InitSound.XIAOBA_TAME_SOUND.get()).hurtSound((SoundEvent)InitSound.XIAOBA_GETHURT_SOUND.get()).deathSound((SoundEvent)InitSound.XIAOBA_DEATH_SOUND.get()).build();
   }
   @Override
   public boolean isFood(ItemStack stack) {
      return stack.is(ItemTags.MEAT) || stack.is(ItemTags.HORSE_FOOD);
   }
}
