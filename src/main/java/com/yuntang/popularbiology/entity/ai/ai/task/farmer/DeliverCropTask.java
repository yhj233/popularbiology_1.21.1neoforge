package com.yuntang.popularbiology.entity.ai.task.farmer;

import com.google.common.collect.ImmutableMap;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.init.InitTag;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.util.FakePlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeliverCropTask extends Behavior<BasePet> {
   private ItemEntity lastTransferItemEntity = null;
   private FakePlayer fakePlayer;
   private static final Logger LOGGER = LogManager.getLogger(DeliverCropTask.class);
   private static final Map<MemoryModuleType<?>, MemoryStatus> REQUIRED_MEMORIES;

   public DeliverCropTask() {
      super(REQUIRED_MEMORIES, 3000);
   }

   protected boolean checkExtraStartConditions(ServerLevel world, BasePet pet) {
      if (pet.getState() == PetState.WORK && pet.getJobId() == 1 && Utils.isFitCondition(pet.getInventory(), InitTag.ENTITY_DELIVER_ITEM) != null && !pet.getBrain().getMemory((MemoryModuleType)InitMemory.PLANT_POS.get()).isPresent() && !pet.getBrain().getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).isPresent() && pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get()).isPresent() && Utils.canInsertContainer(world, (BlockPos)pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get()).get(), pet) && pet.getAction() == 0) {
         Optional<BlockPos> containerPosOpt = pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get());
         if (containerPosOpt.isEmpty()) {
            return false;
         } else {
            BlockPos containerPos = (BlockPos)containerPosOpt.get();
            double distanceSq = pet.distanceToSqr((double)containerPos.getX() + 0.5D, (double)containerPos.getY() + 0.5D, (double)containerPos.getZ() + 0.5D);
            return distanceSq <= 9.0D;
         }
      } else {
         return false;
      }
   }

   protected void start(ServerLevel world, BasePet pet, long time) {
      pet.setAction(3);
      this.fakePlayer = Utils.getFakePlayer(world);
      Vec3 centre = Vec3.atCenterOf(pet.blockPosition());
      this.fakePlayer.setPos(centre.x() + 0.501D, centre.y() + 0.501D, centre.z() + 0.501D);
      pet.level().addFreshEntity(this.fakePlayer);
      this.doOpenChest(pet, this.fakePlayer);
   }

   protected boolean canStillUse(ServerLevel world, BasePet pet, long time) {
      return pet.getState() == PetState.WORK && pet.getJobId() == 1 && Utils.isFitCondition(pet.getInventory(), InitTag.ENTITY_DELIVER_ITEM) != null && !pet.getBrain().getMemory((MemoryModuleType)InitMemory.PLANT_POS.get()).isPresent() && !pet.getBrain().getMemory((MemoryModuleType)InitMemory.HARVEST_POS.get()).isPresent() && pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get()).isPresent() && Utils.canInsertContainer(world, (BlockPos)pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get()).get(), pet) && pet.getAction() == 3;
   }

   protected void tick(ServerLevel world, BasePet pet, long time) {
      pet.getNavigation().stop();
      BlockPos containerPos = (BlockPos)pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get()).get();
      List<ItemStack> items = Utils.isFitCondition(pet.getInventory(), InitTag.ENTITY_DELIVER_ITEM);
      List<ItemStack> transferItems = Utils.getItems(items, 1);
      BlockEntity blockEntity = world.getBlockEntity(containerPos);
      if (blockEntity instanceof Container) {
         Container container = (Container)blockEntity;

         for(Iterator var10 = transferItems.iterator(); var10.hasNext(); container.setChanged()) {
            ItemStack item = (ItemStack)var10.next();
            ItemStack remaining = item.copy();

            int slot;
            for(slot = 0; slot < container.getContainerSize() && !remaining.isEmpty(); ++slot) {
               ItemStack slotStack = container.getItem(slot);
               int space;
               if (slotStack.isEmpty()) {
                  space = Math.min(remaining.getCount(), remaining.getMaxStackSize());
                  container.setItem(slot, remaining.split(space));
               } else if (ItemStack.isSameItem(slotStack, remaining)) {
                  space = slotStack.getMaxStackSize() - slotStack.getCount();
                  int transfer = Math.min(remaining.getCount(), space);
                  slotStack.grow(transfer);
                  remaining.shrink(transfer);
                  container.setItem(slot, slotStack);
               }
            }

            if (remaining.getCount() < item.getCount()) {
               slot = item.getCount() - remaining.getCount();
               Utils.removeItems(pet.getInventory(), item.getItem(), slot);
               item.setCount(remaining.getCount());
            }
         }
      }

   }

   protected void stop(ServerLevel world, BasePet pet, long time) {
      super.stop(world, pet, time);
      if (this.lastTransferItemEntity != null) {
         this.lastTransferItemEntity.discard();
         this.lastTransferItemEntity = null;
      }

      Optional<BlockPos> containerPosOpt = pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get());
      if (containerPosOpt.isPresent()) {
         this.fakePlayer.closeContainer();
         world.removePlayerImmediately(this.fakePlayer, RemovalReason.DISCARDED);
      }

      pet.setAction(0);
   }

   private void doOpenChest(BasePet pet, FakePlayer fakePlayer) {
      Optional<BlockPos> chestPos = pet.getBrain().getMemory((MemoryModuleType)InitMemory.CONTAINER_POS.get());
      if (!chestPos.isEmpty()) {
         BlockHitResult hitResult = new BlockHitResult(new Vec3((double)((BlockPos)chestPos.get()).getX(), (double)((BlockPos)chestPos.get()).getY(), (double)((BlockPos)chestPos.get()).getZ()), Direction.UP, (BlockPos)chestPos.get(), false);
         fakePlayer.gameMode.useItemOn(fakePlayer, pet.level(), ItemStack.EMPTY, InteractionHand.MAIN_HAND, hitResult);
      }
   }

   static {
      REQUIRED_MEMORIES = ImmutableMap.of((MemoryModuleType)InitMemory.CONTAINER_POS.get(), MemoryStatus.VALUE_PRESENT);
   }
}
