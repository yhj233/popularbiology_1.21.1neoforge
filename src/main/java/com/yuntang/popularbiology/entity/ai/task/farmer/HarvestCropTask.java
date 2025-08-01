package com.yuntang.popularbiology.entity.ai.task.farmer;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;

public class HarvestCropTask extends Behavior<BasePet> {
   private int actionTime = 100;
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public HarvestCropTask() {
      super(REQUIRED_MEMORIES, 15);
   }

   protected boolean checkExtraStartConditions(ServerLevel world, BasePet pet) {
      if (pet.getState() == PetState.WORK && pet.getJobId() == 1 && pet.getBrain().getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).isPresent() && pet.getAction() == 0) {
         Brain<BasePet> brain = pet.getBrain();
         BlockPos cropPos = (BlockPos)brain.getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).get();
         return Utils.canHarvesr(world, cropPos) && pet.getState() == PetState.WORK && cropPos.distSqr(pet.blockPosition()) <= 1.0D;
      } else {
         return false;
      }
   }

   protected void start(ServerLevel world, BasePet pet, long time) {
      pet.setAction(1);
      Brain<BasePet> brain = pet.getBrain();
      Optional<BlockPos> cropPosOpt = brain.getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get());
      if (!cropPosOpt.isEmpty()) {
         BlockPos cropPos = (BlockPos)cropPosOpt.get();
         brain.setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(cropPos));
      }
   }

   protected void tick(ServerLevel pLevel, BasePet pOwner, long pGameTime) {
      --this.actionTime;
   }

   protected boolean canStillUse(ServerLevel pLevel, BasePet pEntity, long pGameTime) {
      return this.actionTime > 0;
   }

   protected void stop(ServerLevel world, BasePet pet, long pGameTime) {
      Brain<BasePet> brain = pet.getBrain();
      Optional<BlockPos> cropPosOpt = brain.getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get());
      if (!cropPosOpt.isEmpty()) {
         BlockPos cropPos = (BlockPos)cropPosOpt.get();
         if (!world.isClientSide()) {
            ItemStack tool = pet.getMainHandItem();
            world.destroyBlock(cropPos, true, pet);
            tool.hurtAndBreak(1, pet, EquipmentSlot.MAINHAND);
            };
         }

         pet.setAction(0);
         pet.getBrain().eraseMemory((MemoryModuleType)InitMemory.HARVEST_POS.get());
         this.actionTime = 100;
      }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of((MemoryModuleType)InitMemory.HARVEST_POS.get(), MemoryStatus.VALUE_PRESENT);
   }
}
