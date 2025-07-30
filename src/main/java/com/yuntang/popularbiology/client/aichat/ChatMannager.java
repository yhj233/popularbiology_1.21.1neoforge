package com.yuntang.popularbiology.client.aichat;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import com.yuntang.popularbiology.config.ClientConfig;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.init.InitNetwork;  // 导入初始化网络的类 不懂 二选一先
import com.yuntang.popularbiology.network.SyncDataC2SPacket;
import net.neoforged.neoforge.network.PacketDistributor; // 导入数据包分发器
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.EnumMap;
import java.util.Map;
import org.slf4j.Logger;

public class ChatMannager {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
   private final BasePet pet;
   private final JsonObject jsonBody = new JsonObject();
   private final String systemInfo;

   public ChatMannager(BasePet pet) {
      this.pet = pet;
      String var10001 = this.pet.getName().getString();
      this.systemInfo = "你叫" + var10001 + "，" + (String)ClientConfig.SYSTEM_INFO.get();
      this.initJsonBody();
   }

   private void initJsonBody() {
      this.jsonBody.addProperty("model", (String)ClientConfig.CHAT_MODEL.get());
      JsonObject webSearch = new JsonObject();
      webSearch.addProperty("enable", (Boolean)ClientConfig.ENABLE_WEB_SEARCH.get());
      webSearch.addProperty("enable_citation", false);
      webSearch.addProperty("enable_trace", false);
      this.jsonBody.add("web_search", webSearch);
      this.jsonBody.add("messages", new JsonArray());
      this.addMessage("user", this.systemInfo);
   }

   public void addMessage(String role, String content) {
      JsonObject message = new JsonObject();
      message.addProperty("role", role);
      message.addProperty("content", content);
      this.jsonBody.getAsJsonArray("messages").add(message);
   }

   public void clearMessages() {
      this.jsonBody.remove("messages");
      this.jsonBody.add("messages", new JsonArray());
      this.addMessage("user", this.systemInfo);
   }

   public void sendRequest() {
      try {
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create((String)ClientConfig.CHAT_API_URL.get())).header("Content-Type", "application/json").header("Authorization", "Bearer " + (String)ClientConfig.CHAT_API_KEY.get()).header("appid", (String)ClientConfig.CHAT_APP_ID.get()).POST(BodyPublishers.ofString(this.jsonBody.toString())).build();
         HTTP_CLIENT.sendAsync(request, BodyHandlers.ofString()).whenComplete((response, throwable) -> {
            if (throwable != null) {
               this.SendContentToPet("脑子坏掉惹QAQ");
               LOGGER.info("请求失败，{}", throwable.getMessage());
            } else if (response.statusCode() != 200) {
               this.SendContentToPet("脑子坏掉惹QAQ");
            } else {
               JsonObject jsonResponse = JsonParser.parseString((String)response.body()).getAsJsonObject();
               if (jsonResponse.has("choices") && jsonResponse.getAsJsonArray("choices").size() != 0) {
                  JsonObject choice = jsonResponse.getAsJsonArray("choices").get(0).getAsJsonObject();
                  if (choice.has("message")) {
                     JsonObject message = choice.getAsJsonObject("message");
                     String role = message.get("role").getAsString();
                     String content = message.get("content").getAsString();
                     this.SendContentToPet(content);
                     if ((Boolean)ClientConfig.ENABLE_CONTEXT.get()) {
                        this.addMessage(role, content);
                     }

                  }
               }
            }
         });
      } catch (Exception var2) {
         var2.printStackTrace();
         this.SendContentToPet("脑子坏掉惹QAQ");
      }

   }

   private void SendContentToPet(String content) {
      int chatbubbleMessageTime = content.length() * 20;
      Map<SyncDataC2SPacket.DataType, Object> data = new EnumMap(SyncDataC2SPacket.DataType.class);
      data.put(SyncDataC2SPacket.DataType.CHATBUBBLE_MESSAGE, content);
      data.put(SyncDataC2SPacket.DataType.CHATBUBBLE_MESSAGE_TIME, chatbubbleMessageTime);
      SyncDataC2SPacket packet = new SyncDataC2SPacket(this.pet.getId(), data);
      // 发送数据包到服务器
      PacketDistributor.sendToServer(packet);
      //
   }
}
