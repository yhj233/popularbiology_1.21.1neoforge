package com.yuntang.popularbiology.tag;

import com.yuntang.popularbiology.init.InitItem;
import com.yuntang.popularbiology.init.InitTag;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider.TagLookup;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemTagsProvider extends ItemTagsProvider {

    public ModItemTagsProvider(PackOutput output, 
                              CompletableFuture<Provider> lookupProvider,
                              CompletableFuture<TagLookup<net.minecraft.world.level.block.Block>> blockTags,
                              ExistingFileHelper helper) {
        super(output, lookupProvider, blockTags, "popularbiology", helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 添加农民工具的标签
        tag(InitTag.ENTITY_FARMER_TOOLS)
            .add(Items.WOODEN_HOE, Items.STONE_HOE, 
                 Items.IRON_HOE, Items.GOLDEN_HOE,
                 Items.DIAMOND_HOE, Items.NETHERITE_HOE);
        
        // 添加可拾取物品的标签
        tag(InitTag.ENTITY_PICKABLE_ITEMS)
            .add(Items.WHEAT, Items.WHEAT_SEEDS, 
                 Items.POTATO, Items.CARROT, 
                 Items.BEETROOT, Items.MELON_SLICE, 
                 Items.PUMPKIN);
        
        // 添加可种植作物的标签
        tag(InitTag.ENTITY_PLANT_CROPS)
            .add(Items.MELON_SEEDS, Items.PUMPKIN_SEEDS, 
                 Items.WHEAT_SEEDS, Items.BEETROOT_SEEDS, 
                 Items.POTATO, Items.CARROT);
        
        // 添加可交付物品的标签
        tag(InitTag.ENTITY_DELIVER_ITEM)
            .add(Items.WHEAT, Items.WHEAT_SEEDS, 
                 Items.BEETROOT, Items.BEETROOT_SEEDS, 
                 Items.MELON, Items.MELON_SLICE, 
                 Items.MELON_SEEDS, Items.POTATO, 
                 Items.CARROT, Items.PUMPKIN, 
                 Items.PUMPKIN_SEEDS);
        
        // 添加剑士工具的标签
        tag(InitTag.ENTITY_FENCER_TOOLS)
            .add(Items.WOODEN_SWORD, Items.STONE_SWORD, 
                 Items.IRON_SWORD, Items.GOLDEN_SWORD, 
                 Items.DIAMOND_SWORD, Items.NETHERITE_SWORD)
            .add(InitItem.WUSAQI_TAOFABANG.get())
            .add(InitItem.JIYI_TAOFABANG.get())
            .add(InitItem.XIAOBA_TAOFABANG.get());
        
        // 添加弓箭手工具的标签
        tag(InitTag.ENTITY_ARCHER_TOOLS)
            .add(Items.BOW);
    }
}