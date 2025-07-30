package com.yuntang.popularbiology.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class InitTag {
   public static final TagKey<Item> ENTITY_FARMER_TOOLS;
   public static final TagKey<Block> ENTITY_HARVEST_CROPS;
   public static final TagKey<Item> ENTITY_PLANT_CROPS;
   public static final TagKey<Item> ENTITY_PICKABLE_ITEMS;
   public static final TagKey<Block> ENTITY_DELEVER_CONTAINER;
   public static final TagKey<Item> ENTITY_DELIVER_ITEM;
   public static final TagKey<Item> ENTITY_FENCER_TOOLS;
   public static final TagKey<EntityType<?>> ENTITY_HOSTILE_ENTITY;
   public static final TagKey<Item> ENTITY_ARCHER_TOOLS;

   static {
      ENTITY_FARMER_TOOLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_farmer_tools"));
      ENTITY_HARVEST_CROPS = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_harvest_crops"));
      ENTITY_PLANT_CROPS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_plant_crops"));
      ENTITY_PICKABLE_ITEMS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_pickable_items"));
      ENTITY_DELEVER_CONTAINER = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_delever_container"));
      ENTITY_DELIVER_ITEM = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_deliver_item"));
      ENTITY_FENCER_TOOLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_fencer_tools"));
      ENTITY_HOSTILE_ENTITY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_hostile_entity"));
      ENTITY_ARCHER_TOOLS = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("popularbiology", "entity_archer_tools"));
   }
}
