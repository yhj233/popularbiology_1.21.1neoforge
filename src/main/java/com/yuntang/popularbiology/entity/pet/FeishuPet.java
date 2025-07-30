package com.yuntang.popularbiology.entity.pet;

import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.interact.BaseInteract;
import com.yuntang.popularbiology.entity.sound.PetSoundManager;
import com.yuntang.popularbiology.init.InitSound;
import com.yuntang.popularbiology.init.InitTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.tags.ItemTags;

public class FeishuPet extends BasePet {
   public FeishuPet(EntityType<? extends BasePet> type, Level level) {
      super(type, level, "feishu");
   }

   public BaseInteract getInteractAction() {
      return new BaseInteract();
   }

   public PetSoundManager getSoundManager() {
      return (new PetSoundManager.Builder()).tameSound((SoundEvent)InitSound.FEISHU_TAME_SOUND.get()).hurtSound((SoundEvent)InitSound.FEISHU_GETHURT_SOUND.get()).deathSound((SoundEvent)InitSound.FEISHU_DEATH_SOUND.get()).build();
   }

   @Override
   public boolean isFood(ItemStack stack) {
      return stack.is(ItemTags.MEAT) || stack.is(ItemTags.HORSE_FOOD);
   }

}
