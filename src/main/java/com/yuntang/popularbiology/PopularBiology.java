package com.yuntang.popularbiology;

import com.mojang.logging.LogUtils;
import com.yuntang.popularbiology.config.ClientConfig;
import com.yuntang.popularbiology.config.CommonConfig;
import com.yuntang.popularbiology.init.InitActivity;
import com.yuntang.popularbiology.init.InitContainer;
import com.yuntang.popularbiology.init.InitCreativeTab;
import com.yuntang.popularbiology.init.InitEntity;
import com.yuntang.popularbiology.init.InitItem;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.init.InitNetwork;
import com.yuntang.popularbiology.init.InitRegistery;
import com.yuntang.popularbiology.init.InitSensor;
import com.yuntang.popularbiology.init.InitSound;
import com.yuntang.popularbiology.init.InitEffect;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig.Type;
//import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.fml.ModContainer;
import net.neoforged.bus.api.IEventBus;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.fml.ModLoadingContext;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

//@EventBusSubscriber
@Mod(PopularBiology.MODID)
public class PopularBiology {
   public static final String MODID = "popularbiology";
   private static final Logger LOGGER = LogUtils.getLogger();

   public PopularBiology(ModContainer modContainer,IEventBus eventBus) {
      LOGGER.info("PopularBiology Mod Loading");
      modContainer.registerConfig(Type.CLIENT, ClientConfig.SPEC, "popularbiology/popularbiology-client.toml");
      modContainer.registerConfig(Type.COMMON, CommonConfig.SPEC, "popularbiology/popularbiology-common.toml");
 //     GeckoLib.register(modbus);
      InitRegistery.register(eventBus);
      InitSound.register(eventBus);
      InitEntity.register(eventBus);
      InitContainer.register(eventBus);
      InitMemory.register(eventBus);
      InitSensor.register(eventBus);
      InitActivity.register(eventBus);
      InitItem.register(eventBus);
      InitCreativeTab.register(eventBus);
      InitEffect.register(eventBus);
      InitNetwork.register(eventBus);  
   }
}