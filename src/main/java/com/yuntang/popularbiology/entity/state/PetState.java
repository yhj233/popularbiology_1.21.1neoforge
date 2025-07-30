package com.yuntang.popularbiology.entity.state;

import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.utils.Utils;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public enum PetState {
   FOLLOW(0, (pet) -> {
      return Component.translatable("message.popularbiology.entity_follow", new Object[]{pet.getDisplayName().getString()});
   }),
   SIT(1, (pet) -> {
      return Component.translatable("message.popularbiology.entity_sit", new Object[]{pet.getDisplayName().getString()});
   }),
   WORK(2, (pet) -> {
      return Component.translatable("message.popularbiology.entity_work", new Object[]{pet.getDisplayName().getString()});
   });


   private final int id;
   private final Function<BasePet, Component> message;

   private PetState(int id, Function<BasePet, Component> message) {
      this.id = id;
      this.message = message;
   }

   public int getId() {
      return this.id;
   }

   public Component getMessage(BasePet pet) {
      return (Component)this.message.apply(pet);
   }

   public PetState getNextState() {
      PetState[] values = values();
      return values[(this.ordinal() + 1) % values.length];
   }

   public CompoundTag serializeNBT() {
      CompoundTag tag = new CompoundTag();
      tag.putInt("StateId", this.id);
      return tag;
   }

   public static PetState deserializeNBT(CompoundTag tag) {
      int stateId = tag.getInt("StateId");
      return fromId(stateId);
   }

   public static PetState fromId(int id) {
      PetState[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         PetState state = var1[var3];
         if (state.id == id) {
            return state;
         }
      }

      return FOLLOW;
   }

   public void onStateChange(BasePet pet) {
      switch(pet.getState()) {
//      case WORK:
//         Utils.setHomeMemory(pet, pet.blockPosition(), (ServerLevel)pet.level());
//         break;
      case FOLLOW:
         pet.getBrain().eraseMemory(MemoryModuleType.HOME);
         pet.getBrain().eraseMemory((MemoryModuleType)InitMemory.HARVEST_POS.get());
      }

   }

   // $FF: synthetic method
   private static PetState[] $values() {
      return new PetState[]{FOLLOW, SIT,}; //WORK};
   }
}
