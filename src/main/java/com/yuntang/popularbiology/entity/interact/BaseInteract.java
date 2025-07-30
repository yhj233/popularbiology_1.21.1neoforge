package com.yuntang.popularbiology.entity.interact;

import com.yuntang.popularbiology.client.render.inventory.PetMenuProvider;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.init.InitEffect;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import net.minecraft.core.Holder;

import net.neoforged.neoforge.client.extensions.IMenuProviderExtension;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.tags.ItemTags;


public class BaseInteract {
   private static final Logger LOGGER = LogManager.getLogger(BaseInteract.class);

	public boolean isFood(ItemStack stack) {
		return stack.is(ItemTags.MEAT) || stack.is(ItemTags.HORSE_FOOD);
	}
   public void execute(Level world, BasePet pet, Player player, InteractionHand hand) {
      boolean isTame = pet.isTame();
      boolean isOwner = pet.isOwnedBy(player);
      boolean isSneaking = player.isShiftKeyDown();

  //    ItemStack itemStack = player.getItemInHand(hand);
  //       Item item = itemStack.getItem();
      boolean isFoodT = this.isFood(player.getItemInHand(hand)) ;
      	
      if (!isTame && isFoodT) {
         this.handleTame(world, pet, player, hand);
      } else if (isTame && isOwner && isFoodT) {
         this.handleFeed(world, pet, player, hand);
      } else if (isTame && isOwner && isSneaking) {
         this.handleStateChange(world, pet, player, hand);
      } else if (isTame && isOwner && !isSneaking && !isFoodT) {
         this.handleOpenGUI(world, pet, player, hand);
      }

   }

   protected void handleTame(Level world, BasePet entity, Player player, InteractionHand hand) {
      if (!world.isClientSide) {
         if (!player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
         }

         boolean isStartTame = Math.random() < 0.3D;
         if (isStartTame) {
            entity.tame(player);
            entity.playSound(entity.getSoundManager().getTameSound(), 1.0F, 1.0F);
            if (world instanceof ServerLevel) {
               ServerLevel serverLevel = (ServerLevel)world;
               serverLevel.sendParticles(ParticleTypes.HEART, entity.getX(), entity.getY() + (double)entity.getBbHeight() / 2.0D, entity.getZ(), 6, 1.0D, 1.0D, 1.0D, 0.1D);
            }

            player.displayClientMessage(entity.getState().getMessage(entity).copy().withStyle(ChatFormatting.GOLD), true);
         }
      }

   }

   protected void handleFeed(Level world, BasePet entity, Player player, InteractionHand hand) {
      if (!world.isClientSide && entity.getMaxHealth() != entity.getHealth()) {
         if (!player.isCreative()) {
            player.getItemInHand(hand).shrink(1);
         }

         if (player.getItemInHand(hand).getItem() == Items.GLOW_BERRIES) {
            entity.addEffect(new MobEffectInstance((Holder<MobEffect>) InitEffect.WULAYAH_EFFECT.get(), 1200, 1));   
         }

         entity.setHealth(Math.min(entity.getHealth() + 4.0F, entity.getMaxHealth()));
         entity.playSound(SoundEvents.PLAYER_LEVELUP, 1.0F, 1.0F);
         ((ServerLevel)world).sendParticles(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY() + (double)entity.getBbHeight() / 2.0D, entity.getZ(), 6, 1.0D, 1.0D, 1.0D, 0.1D);
      }

   }

   protected void handleStateChange(Level world, BasePet entity, Player player, InteractionHand hand) {
      if (!world.isClientSide) {
         entity.setState(entity.getState().getNextState());
         entity.getState().onStateChange(entity);
         player.displayClientMessage(entity.getState().getMessage(entity).copy().withStyle(ChatFormatting.GOLD), true);
      }

   }

   protected void handleOpenGUI(Level world, BasePet entity, Player player, InteractionHand hand) {
      if (!world.isClientSide) {
         ((ServerPlayer)player).openMenu(new PetMenuProvider(entity), (buf) -> {
            buf.writeInt(entity.getId());
         });
      }

   }
}
