package com.yuntang.popularbiology.init;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class InitCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS;
    public static DeferredHolder<CreativeModeTab, CreativeModeTab> MAIN_TAB;

    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }

    static {
        TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, "popularbiology");
        MAIN_TAB = TABS.register("main", () -> {
            return CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.popular_biology_group"))
                    .icon(() -> InitItem.WUSAQI_SPAWN_EGG.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(InitItem.WUSAQI_ARMOR_BOOTS.get());
                        output.accept(InitItem.WUSAQI_SPAWN_EGG.get());
                        output.accept(InitItem.JIYI_SPAWN_EGG.get());
                        output.accept(InitItem.XIAOBA_SPAWN_EGG.get());
                        output.accept(InitItem.FEISHU_SPAWN_EGG.get());
                        output.accept(InitItem.LIZIMANTOU_SPAWN_EGG.get());
                        output.accept(InitItem.SHISA_SPAWN_EGG.get());
                        output.accept(InitItem.LAISHI_SPAWN_EGG.get());
                        output.accept(InitItem.YUGUIGOU_SPAWN_EGG.get());
                        output.accept(InitItem.NAILONG_SPAWN_EGG.get());
                        output.accept(InitItem.WUSAQI_TAOFABANG.get());
                        output.accept(InitItem.JIYI_TAOFABANG.get());
                        output.accept(InitItem.XIAOBA_TAOFABANG.get());
                    })
                    .build();
        });
    }
}