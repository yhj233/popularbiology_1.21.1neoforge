package com.yuntang.popularbiology.entity.sound;

import java.util.Random;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class PetSoundManager {
   private SoundEvent tameSound;
   private SoundEvent hurtSound;
   private SoundEvent deathSound;
   private SoundEvent attackSound;

   private PetSoundManager(PetSoundManager.Builder builder) {
      this.tameSound = SoundEvents.EMPTY;
      this.hurtSound = SoundEvents.EMPTY;
      this.deathSound = SoundEvents.EMPTY;
      this.attackSound = SoundEvents.EMPTY;
      this.tameSound = builder.tameSound;
      this.hurtSound = builder.hurtSound;
      this.deathSound = builder.deathSound;
      this.attackSound = builder.attackSound;
   }

   public SoundEvent getTameSound() {
      return this.tameSound;
   }

   public SoundEvent getHurtSound() {
      return this.hurtSound;
   }

   public SoundEvent getDeathSound() {
      return this.deathSound;
   }

   public SoundEvent getAttackSound() {
      return this.attackSound;
   }

   public SoundEvent getRandomSound() {
      SoundEvent[] sounds = new SoundEvent[]{this.tameSound, this.hurtSound, this.deathSound, this.attackSound};
      Random random = new Random();
      return sounds[random.nextInt(sounds.length)];
   }

   public static class Builder {
      private SoundEvent tameSound;
      private SoundEvent hurtSound;
      private SoundEvent deathSound;
      private SoundEvent attackSound;

      public Builder() {
         this.tameSound = SoundEvents.EMPTY;
         this.hurtSound = SoundEvents.EMPTY;
         this.deathSound = SoundEvents.EMPTY;
         this.attackSound = SoundEvents.EMPTY;
      }

      public PetSoundManager.Builder tameSound(SoundEvent sound) {
         this.tameSound = sound;
         return this;
      }

      public PetSoundManager.Builder hurtSound(SoundEvent sound) {
         this.hurtSound = sound;
         return this;
      }

      public PetSoundManager.Builder deathSound(SoundEvent sound) {
         this.deathSound = sound;
         return this;
      }

      public PetSoundManager.Builder attackSound(SoundEvent sound) {
         this.attackSound = sound;
         return this;
      }

      public PetSoundManager build() {
         return new PetSoundManager(this);
      }
   }
}
