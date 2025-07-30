package com.yuntang.popularbiology;

import com.yuntang.popularbiology.tag.ModBlockTagsProvider;
import com.yuntang.popularbiology.tag.ModEntityTagsProvider;
import com.yuntang.popularbiology.tag.ModItemTagsProvider;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(
   modid = "popularbiology",
   bus = EventBusSubscriber.Bus.MOD
)
public class DataGenerators {
   @SubscribeEvent
   public static void gatherData(GatherDataEvent event) {
      DataGenerator gen = event.getGenerator();
      PackOutput output = gen.getPackOutput();
      CompletableFuture<Provider> lookupProvider = event.getLookupProvider();
      ExistingFileHelper helper = event.getExistingFileHelper();
      DataGenerator.PackGenerator packGen = gen.getVanillaPack(event.includeServer());
      var blockTagsProvider = packGen.addProvider(output1 -> new ModBlockTagsProvider(output1, lookupProvider, helper));
      packGen.addProvider(output1 -> new ModItemTagsProvider(output1, lookupProvider, blockTagsProvider.contentsGetter(), helper));
      packGen.addProvider(output1 -> new ModEntityTagsProvider(output1, lookupProvider, helper));
   }
}
