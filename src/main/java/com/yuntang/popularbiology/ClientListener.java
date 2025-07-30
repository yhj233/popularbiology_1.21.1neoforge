package com.yuntang.popularbiology;

import com.mojang.blaze3d.platform.InputConstants.Type;
import com.yuntang.popularbiology.client.render.inventory.PetScreen;
import com.yuntang.popularbiology.client.render.pet.FeishuRender;
import com.yuntang.popularbiology.client.render.pet.JiyiRender;
import com.yuntang.popularbiology.client.render.pet.LaishiRender;
import com.yuntang.popularbiology.client.render.pet.LizimantouRender;
import com.yuntang.popularbiology.client.render.pet.NailongRender;
import com.yuntang.popularbiology.client.render.pet.ShisaRender;
import com.yuntang.popularbiology.client.render.pet.WusaqiRender;
import com.yuntang.popularbiology.client.render.pet.XiaobaRender;
import com.yuntang.popularbiology.client.render.pet.YuguigouRender;
import com.yuntang.popularbiology.init.InitContainer;
import com.yuntang.popularbiology.init.InitEntity;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import net.neoforged.bus.api.SubscribeEvent; // 修改事件总线导入
import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(
   modid = "popularbiology",
   bus = EventBusSubscriber.Bus.MOD,
   value = {Dist.CLIENT}
)
public final class ClientListener {
   public static final String CATEGORY = "key.popularbiology.category";
   public static final KeyMapping TALK_KEY;

   @SubscribeEvent
   public static void registerKeys(RegisterKeyMappingsEvent event) {
      event.register(TALK_KEY);
   }

   @SubscribeEvent
   public static void registerRenderers(RegisterRenderers event) {
      event.registerEntityRenderer((EntityType)InitEntity.WUSAQI.get(), WusaqiRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.XIAOBA.get(), XiaobaRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.JIYI.get(), JiyiRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.FEISHU.get(), FeishuRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.LAISHI.get(), LaishiRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.LIZIMANTOU.get(), LizimantouRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.SHISA.get(), ShisaRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.NAILONG.get(), NailongRender::new);
      event.registerEntityRenderer((EntityType)InitEntity.YUGUIGOU.get(), YuguigouRender::new);
   }

   @SubscribeEvent
   public static void registerMenuScreens(RegisterMenuScreensEvent event) {
      event.register(InitContainer.PET_CONTAINER.get(), PetScreen::new);
   }

   static {
      TALK_KEY = new KeyMapping("key.popularbiology.talk", KeyConflictContext.IN_GAME, KeyModifier.NONE, Type.KEYSYM, 88, "key.popularbiology.category");
   }
}
