package com.yuntang.popularbiology.tag;

import com.yuntang.popularbiology.init.InitTag;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModEntityTagsProvider extends EntityTypeTagsProvider {
   public ModEntityTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper existingFileHelper) {
      super(output, lookupProvider, "popularbiology", existingFileHelper);
   }

   protected void addTags(Provider provider) {
      this.tag(InitTag.ENTITY_HOSTILE_ENTITY).add(EntityType.BLAZE).add(EntityType.CAVE_SPIDER).add(EntityType.DROWNED).add(EntityType.EVOKER).add(EntityType.GUARDIAN).add(EntityType.HUSK).add(EntityType.ILLUSIONER).add(EntityType.MAGMA_CUBE).add(EntityType.PHANTOM).add(EntityType.PIGLIN).add(EntityType.PIGLIN_BRUTE).add(EntityType.PILLAGER).add(EntityType.SILVERFISH).add(EntityType.SKELETON).add(EntityType.SLIME).add(EntityType.SPIDER).add(EntityType.STRAY).add(EntityType.VEX).add(EntityType.VINDICATOR).add(EntityType.WITCH).add(EntityType.WITHER_SKELETON).add(EntityType.ZOGLIN).add(EntityType.ZOMBIE);
   }
}
