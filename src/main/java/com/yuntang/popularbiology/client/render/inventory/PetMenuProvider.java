package com.yuntang.popularbiology.client.render.inventory;

import com.yuntang.popularbiology.entity.BasePet;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class PetMenuProvider implements MenuProvider {
   private final BasePet pet;

   public PetMenuProvider(BasePet pet) {
      this.pet = pet;
   }

   public Component getDisplayName() {
      return Component.literal(this.pet.getPetName());
   }

   @Nullable
   public AbstractContainerMenu createMenu(int containerId, Inventory playerInv, Player player) {
      return new PetContainerMenu(containerId, playerInv, this.pet);
   }
}
