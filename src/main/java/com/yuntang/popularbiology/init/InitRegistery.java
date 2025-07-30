package com.yuntang.popularbiology.init;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;

public class InitRegistery {
    public static void createRegistry(NewRegistryEvent event) {
        // 如果需要创建自定义注册表，可以在这里添加
        // 例如：
        // event.register(createCustomRegistry());
    }

    public static void register(IEventBus bus) {
        bus.addListener(InitRegistery::createRegistry);
    }
    
    // 示例：创建自定义注册表
    /*
    private static DeferredRegister<?> createCustomRegistry() {
        return DeferredRegister.create(
            new ResourceLocation("popularbiology", "custom_registry"),
            "popularbiology"
        );
    }
    */
}