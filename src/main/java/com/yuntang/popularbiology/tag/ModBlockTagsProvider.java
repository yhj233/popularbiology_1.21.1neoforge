package com.yuntang.popularbiology.tag;

import com.yuntang.popularbiology.init.InitTag;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ModBlockTagsProvider extends BlockTagsProvider {

    public ModBlockTagsProvider(PackOutput output, 
                               CompletableFuture<Provider> lookupProvider, 
                               ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "popularbiology", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 添加可收获作物的标签
        tag(InitTag.ENTITY_HARVEST_CROPS)
            .add(Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS, 
                 Blocks.BEETROOTS, Blocks.PUMPKIN, Blocks.MELON);
        
        // 添加可交付容器的标签
        tag(InitTag.ENTITY_DELEVER_CONTAINER)
            .add(Blocks.CHEST, Blocks.TRAPPED_CHEST, 
                 Blocks.BARREL, Blocks.HOPPER);
    }
}