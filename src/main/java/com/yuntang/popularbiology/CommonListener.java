package com.yuntang.popularbiology;

import com.yuntang.popularbiology.entity.pet.FeishuPet;
import com.yuntang.popularbiology.entity.pet.JiyiPet;
import com.yuntang.popularbiology.entity.pet.LaishiPet;
import com.yuntang.popularbiology.entity.pet.LizimantouPet;
import com.yuntang.popularbiology.entity.pet.NailongPet;
import com.yuntang.popularbiology.entity.pet.ShisaPet;
import com.yuntang.popularbiology.entity.pet.WusaqiPet;
import com.yuntang.popularbiology.entity.pet.XiaobaPet;
import com.yuntang.popularbiology.entity.pet.YuguigouPet;
import com.yuntang.popularbiology.init.InitEntity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent; // 修改事件总线导入
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(
   modid = "popularbiology",
   bus = EventBusSubscriber.Bus.MOD
)
public final class CommonListener {
   @SubscribeEvent
   public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
      event.put((EntityType)InitEntity.WUSAQI.get(), WusaqiPet.createAttributes().build());
      event.put((EntityType)InitEntity.XIAOBA.get(), XiaobaPet.createAttributes().build());
      event.put((EntityType)InitEntity.JIYI.get(), JiyiPet.createAttributes().build());
      event.put((EntityType)InitEntity.FEISHU.get(), FeishuPet.createAttributes().build());
      event.put((EntityType)InitEntity.LAISHI.get(), LaishiPet.createAttributes().build());
      event.put((EntityType)InitEntity.LIZIMANTOU.get(), LizimantouPet.createAttributes().build());
      event.put((EntityType)InitEntity.SHISA.get(), ShisaPet.createAttributes().build());
      event.put((EntityType)InitEntity.NAILONG.get(), NailongPet.createAttributes().build());
      event.put((EntityType)InitEntity.YUGUIGOU.get(), YuguigouPet.createAttributes().build());
   }
}
