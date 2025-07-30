package com.yuntang.popularbiology.client.render.inventory;

import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.init.InitContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PetContainerMenu extends AbstractContainerMenu {
   private final BasePet pet;
   private final SimpleContainer inventory;
   int x_offset = 8;
   int y_offset = 84;
   private final ContainerData data;

   public PetContainerMenu(int id, Inventory playerInv, BasePet pet) {
      super((MenuType)InitContainer.PET_CONTAINER.get(), id);
      this.pet = pet;
      this.inventory = pet.getInventory();
      this.inventory.startOpen(playerInv.player);
      this.addSlot(new Slot(this.inventory, 0, 8, 18));

      for(int row = 0; row < 3; ++row) {
         for(int col = 0; col < 5; ++col) {
            int x = 80 + col * 18;
            int y = 18 + row * 18;
            this.addSlot(new Slot(this.inventory, col + row * 5 + 1, x, y));
         }
      }

      this.addPlayerSlots(playerInv);
      this.data = new SimpleContainerData(2);
      this.addDataSlots(this.data);
   }

   public BasePet getPet() {
      return this.pet;
   }

   private void addPlayerSlots(Inventory playerInventory) {
    for(int row = 0; row < 3; ++row) {
        for(int col = 0; col < 9; ++col) {
            int x = this.x_offset + col * 18;
            int y = this.y_offset + row * 18;
            this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
        }
    }

    for(int col = 0; col < 9; ++col) {
        int x = this.x_offset + col * 18;
        int y = this.y_offset + 58;
        this.addSlot(new Slot(playerInventory, col, x, y));
    }
}

   public ItemStack quickMoveStack(Player player, int index) {
      ItemStack result = ItemStack.EMPTY;
      Slot slot = (Slot)this.slots.get(index);
      if (slot != null && slot.hasItem()) {
         ItemStack sourceStack = slot.getItem();
         result = sourceStack.copy();
         int petSlots = 16;
         if (index < petSlots) {
            if (!this.moveItemStackTo(sourceStack, petSlots, this.slots.size(), true)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.moveItemStackTo(sourceStack, 0, petSlots, false)) {
            return ItemStack.EMPTY;
         }

         if (sourceStack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
         } else {
            slot.setChanged();
         }

         if (sourceStack.getCount() == result.getCount()) {
            return ItemStack.EMPTY;
         }

         slot.onTake(player, sourceStack);
      }

      return result;
   }

   public void removed(Player player) {
      super.removed(player);
      this.inventory.stopOpen(player);
   }

   public boolean stillValid(Player player) {
      return this.pet != null && this.pet.isAlive() && this.pet.distanceToSqr(player) < 64.0D;
   }
}
