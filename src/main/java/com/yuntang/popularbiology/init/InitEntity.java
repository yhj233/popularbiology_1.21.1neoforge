package com.yuntang.popularbiology.init;

import com.yuntang.popularbiology.entity.pet.FeishuPet;
import com.yuntang.popularbiology.entity.pet.JiyiPet;
import com.yuntang.popularbiology.entity.pet.LaishiPet;
import com.yuntang.popularbiology.entity.pet.LizimantouPet;
import com.yuntang.popularbiology.entity.pet.NailongPet;
import com.yuntang.popularbiology.entity.pet.ShisaPet;
import com.yuntang.popularbiology.entity.pet.WusaqiPet;
import com.yuntang.popularbiology.entity.pet.XiaobaPet;
import com.yuntang.popularbiology.entity.pet.YuguigouPet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitEntity {
    public static final DeferredRegister<EntityType<?>> ENTITIES;
    
    public static final DeferredHolder<EntityType<?>, EntityType<WusaqiPet>> WUSAQI;
    public static final DeferredHolder<EntityType<?>, EntityType<XiaobaPet>> XIAOBA;
    public static final DeferredHolder<EntityType<?>, EntityType<JiyiPet>> JIYI;
    public static final DeferredHolder<EntityType<?>, EntityType<FeishuPet>> FEISHU;
    public static final DeferredHolder<EntityType<?>, EntityType<LaishiPet>> LAISHI;
    public static final DeferredHolder<EntityType<?>, EntityType<LizimantouPet>> LIZIMANTOU;
    public static final DeferredHolder<EntityType<?>, EntityType<ShisaPet>> SHISA;
    public static final DeferredHolder<EntityType<?>, EntityType<NailongPet>> NAILONG;
    public static final DeferredHolder<EntityType<?>, EntityType<YuguigouPet>> YUGUIGOU;

    public static void register(IEventBus modEventBus) {
        ENTITIES.register(modEventBus);
    }

    static {
        ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "popularbiology");
        
        WUSAQI = ENTITIES.register("wusaqi", () -> 
            EntityType.Builder.of(WusaqiPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:wusaqi")
        );
        
        XIAOBA = ENTITIES.register("xiaoba", () -> 
            EntityType.Builder.of(XiaobaPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:xiaoba")
        );
        
        JIYI = ENTITIES.register("jiyi", () -> 
            EntityType.Builder.of(JiyiPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:jiyi")
        );
        
        FEISHU = ENTITIES.register("feishu", () -> 
            EntityType.Builder.of(FeishuPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:feishu")
        );
        
        LAISHI = ENTITIES.register("laishi", () -> 
            EntityType.Builder.of(LaishiPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:laishi")
        );
        
        LIZIMANTOU = ENTITIES.register("lizimantou", () -> 
            EntityType.Builder.of(LizimantouPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:lizimantou")
        );
        
        SHISA = ENTITIES.register("shisa", () -> 
            EntityType.Builder.of(ShisaPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:shisa")
        );
        
        NAILONG = ENTITIES.register("nailong", () -> 
            EntityType.Builder.of(NailongPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:nailong")
        );
        
        YUGUIGOU = ENTITIES.register("yuguigou", () -> 
            EntityType.Builder.of(YuguigouPet::new, MobCategory.CREATURE)
                .sized(0.6F, 0.85F)
                .build("popularbiology:yuguigou")
        );
    }
}