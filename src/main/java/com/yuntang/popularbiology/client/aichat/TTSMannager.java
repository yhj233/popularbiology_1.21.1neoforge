package com.yuntang.popularbiology.client.aichat;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import com.yuntang.popularbiology.config.ClientConfig;
import com.yuntang.popularbiology.entity.BasePet;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import org.slf4j.Logger;

public class TTSMannager {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
   private final JsonObject requestBody = new JsonObject();
   private final BasePet pet;

   public TTSMannager(BasePet pet) {
      this.pet = pet;
      this.initRequestBody();
   }

   private void initRequestBody() {
      this.requestBody.addProperty("format", "opus");
      this.requestBody.addProperty("mp3_bitrate", 128);
      this.requestBody.addProperty("chunk_length", 200);
      this.requestBody.addProperty("opus_bitrate", 24);
      this.requestBody.addProperty("normalize", true);
      this.requestBody.addProperty("latency", "normal");
   }

   public void sendRequestAndPlaySound(String text) {
      this.requestBody.addProperty("text", this.pet.getChatbubbleMessage());
      this.requestBody.addProperty("reference_id", this.pet.getTTSModelId());

      try {
         HttpRequest request = HttpRequest.newBuilder().uri(URI.create((String)ClientConfig.TTS_API_URL.get())).header("Content-Type", "application/json").header("Authorization", "Bearer " + (String)ClientConfig.TTS_API_KEY.get()).POST(BodyPublishers.ofString(this.requestBody.toString())).build();
         HTTP_CLIENT.sendAsync(request, BodyHandlers.ofString()).whenComplete((response, throwable) -> {
            if (throwable != null) {
               LOGGER.error("TTS请求失败", throwable);
            }

            if (response != null) {
               LOGGER.info("TTS请求成功");
            }

         });
      } catch (Exception var3) {
         LOGGER.error("TTS请求失败", var3);
      }

   }
}
