package com.yuntang.popularbiology.config;

import net.neoforged.neoforge.common.ModConfigSpec;
//import net.neoforged.bus.api.SubscribeEvent; // 修改事件总线导入
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.fml.common.EventBusSubscriber;

//@EventBusSubscriber(modid = "popularbiology")

public class ClientConfig {
   private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
   public static final ModConfigSpec.BooleanValue SHOW_WELCOME_MESSAGE;
   public static final ModConfigSpec.ConfigValue<String> CHAT_APP_ID;
   public static final ModConfigSpec.ConfigValue<String> CHAT_API_KEY;
   public static final ModConfigSpec.ConfigValue<String> CHAT_API_URL;
   public static final ModConfigSpec.ConfigValue<String> CHAT_MODEL;
   public static final ModConfigSpec.BooleanValue ENABLE_CONTEXT;
   public static final ModConfigSpec.ConfigValue<String> SYSTEM_INFO;
   public static final ModConfigSpec.BooleanValue ENABLE_WEB_SEARCH;
   public static final ModConfigSpec.ConfigValue<String> TTS_API_URL;
   public static final ModConfigSpec.ConfigValue<String> TTS_API_KEY;
   public static final ModConfigSpec SPEC;

   static {
      SHOW_WELCOME_MESSAGE = BUILDER.comment("是否展示欢迎信息").define("showWelcomeMessage", true);
      CHAT_APP_ID = BUILDER.comment("百度千帆的 appid").define("appId", "your_appid");
      CHAT_API_KEY = BUILDER.comment("百度千帆apikey").define("apiKey", "your_apikey");
      CHAT_API_URL = BUILDER.comment("请求的URL").define("chat-apiUrl", "null");
      CHAT_MODEL = BUILDER.comment("使用的模型").define("model", "deepseek-r1");
      ENABLE_CONTEXT = BUILDER.comment("是否开启上下文，如果开启可能会导致token使用较多").define("enableContext", true);
      SYSTEM_INFO = BUILDER.comment("与AI交互的基础信息:你是Minecraft中的一只宠物...模组已经帮你拼接了每一个宠物的名字，剩下的信息可以自己写")
                         .define("systemInfo", "你是Minecraft中的一只宠物,负责对话聊天，你的回复不要超过30个字");
      ENABLE_WEB_SEARCH = BUILDER.comment("是否开启web搜索").define("enableWebSearch", false);
      TTS_API_URL = BUILDER.comment("TTS API URL").define("ttsApiUrl", "null");
      TTS_API_KEY = BUILDER.comment("TTS API KEY").define("ttsApiKey", "null");
      SPEC = BUILDER.build();
   }
}
