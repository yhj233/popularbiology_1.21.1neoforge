package com.yuntang.popularbiology.init;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitMemory {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORIES;
    
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> HARVEST_POS;
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> PLANT_POS;
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<List<Entity>>> PICKABLE_ITEM;
    public static final DeferredHolder<MemoryModuleType<?>, MemoryModuleType<BlockPos>> CONTAINER_POS;

    public static void register(IEventBus eventBus) {
        MEMORIES.register(eventBus);
    }

    static {
        // 使用 BuiltInRegistries 替代 ForgeRegistries
        MEMORIES = DeferredRegister.create(BuiltInRegistries.MEMORY_MODULE_TYPE, "popularbiology");
        
        // 使用泛型安全的方式注册 MemoryModuleType
        HARVEST_POS = MEMORIES.register("harvest_pos", () -> 
            new MemoryModuleType<>(Optional.of(BlockPos.CODEC))
        );
        
        PLANT_POS = MEMORIES.register("plant_pos", () -> 
            new MemoryModuleType<>(Optional.of(BlockPos.CODEC))
        );
        
        // 对于复杂类型，使用显式类型转换
        PICKABLE_ITEM = MEMORIES.register("pickable_item", () -> 
            new MemoryModuleType<List<Entity>>(Optional.empty())
        );
        
        CONTAINER_POS = MEMORIES.register("container_pos", () -> 
            new MemoryModuleType<>(Optional.of(BlockPos.CODEC))
        );
    }
}