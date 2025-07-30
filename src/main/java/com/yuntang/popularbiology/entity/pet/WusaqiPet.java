package com.yuntang.popularbiology.entity.pet;

import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.interact.BaseInteract;
import com.yuntang.popularbiology.entity.sound.PetSoundManager;
import com.yuntang.popularbiology.init.InitSound;
import com.yuntang.popularbiology.item.WusaqiTaoFaBang;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;

public class WusaqiPet extends BasePet {
   public WusaqiPet(EntityType<? extends BasePet> type, Level level) {
      super(type, level, "wusaqi");
   }

   public BaseInteract getInteractAction() {
      return new BaseInteract();
   }

   public boolean doHurtTarget(Entity pEntity) {
      if (this.getMainHandItem().getItem() instanceof WusaqiTaoFaBang) {
         Level level = this.level();
         if (!level.isClientSide) {
            level.explode(this, pEntity.getX(), pEntity.getY(), pEntity.getZ(), 1.0F, false, ExplosionInteraction.NONE);
         }
      }

      return super.doHurtTarget(pEntity);
   }

   public PetSoundManager getSoundManager() {
      return (new PetSoundManager.Builder()).tameSound((SoundEvent)InitSound.WUSAQI_TAME_SOUND.get()).hurtSound((SoundEvent)InitSound.WUSAQI_GETHURT_SOUND.get()).deathSound((SoundEvent)InitSound.WUSAQI_DEATH_SOUND.get()).build();
   }
   @Override
   public boolean isFood(ItemStack stack) {
      return stack.is(ItemTags.MEAT) || stack.is(ItemTags.HORSE_FOOD);
   }
}
