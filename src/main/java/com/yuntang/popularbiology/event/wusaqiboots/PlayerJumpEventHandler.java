package com.yuntang.popularbiology.event.wusaqiboots;

import com.mojang.logging.LogUtils;
import com.yuntang.popularbiology.init.InitSound;
import com.yuntang.popularbiology.item.WusaqiArmorItem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.slf4j.Logger;

@EventBusSubscriber(modid = "popularbiology")
public class PlayerJumpEventHandler {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final int MAX_JUMPS = 4;
   private static final int JUMP_COOLDOWN_TICKS = 100;
   private static final Map<Player, Integer> playerJumpCounts = new HashMap<>();

   @SubscribeEvent
   public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
      LivingEntity entity = event.getEntity();
      if (!(entity instanceof Player player)) return;
      
      Level world = player.level();
      if (world.isClientSide) return;
      
      Item feetItem = player.getItemBySlot(EquipmentSlot.FEET).getItem();
      if (!(feetItem instanceof WusaqiArmorItem wusaqiArmorItem)) return;
      
      if (player.getCooldowns().isOnCooldown(wusaqiArmorItem)) return;
      
      int jumpCount = playerJumpCounts.getOrDefault(player, 0);
      SoundEvent soundEvent = switch (jumpCount) {
         case 0 -> InitSound.WUSAQI_BOOT_1.get();
         case 1 -> InitSound.WUSAQI_BOOT_2.get();
         case 2 -> InitSound.WUSAQI_BOOT_3.get();
         case 3 -> InitSound.WUSAQI_BOOT_4.get();
         default -> null;
      };
      
      if (soundEvent != null) {
         world.playSound(null, player.getX(), player.getY(), player.getZ(), soundEvent, SoundSource.PLAYERS, 1.0F, 1.0F);
      }

      playerJumpCounts.put(player, jumpCount + 1);
      if (jumpCount + 1 >= MAX_JUMPS) {
         player.getCooldowns().addCooldown(wusaqiArmorItem, JUMP_COOLDOWN_TICKS);
         playerJumpCounts.put(player, 0);
      }
   }
}