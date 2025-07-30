package com.yuntang.popularbiology.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitSound {
    private static final DeferredRegister<SoundEvent> SOUNDS;
    
    // 驯服声音
    public static final DeferredHolder<SoundEvent, SoundEvent> JIYI_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> XIAOBA_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> LIZIMANTOU_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> FEISHU_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> YUGUIGOU_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> SHISA_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> LAISHI_TAME_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> NAILONG_TAME_SOUND;
    
    // 受伤声音
    public static final DeferredHolder<SoundEvent, SoundEvent> JIYI_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> XIAOBA_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> LIZIMANTOU_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> FEISHU_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> YUGUIGOU_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> SHISA_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> LAISHI_GETHURT_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> NAILONG_GETHURT_SOUND;
    
    // 死亡声音
    public static final DeferredHolder<SoundEvent, SoundEvent> JIYI_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> XIAOBA_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> LIZIMANTOU_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> FEISHU_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> YUGUIGOU_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> SHISA_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> LAISHI_DEATH_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> NAILONG_DEATH_SOUND;
    
    // 武器和脚步声
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_ATTACK_SOUND;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_BOOT_1;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_BOOT_2;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_BOOT_3;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_BOOT_4;
    public static final DeferredHolder<SoundEvent, SoundEvent> WUSAQI_HENG;

    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvent(String name) {
        return SOUNDS.register(name, () -> 
            SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath("popularbiology", name)) 
        );
    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }

    static {
        // 使用原版注册表
        SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, "popularbiology");
        
        // 驯服声音
        JIYI_TAME_SOUND = registerSoundEvent("jiyi_tame_sound");
        WUSAQI_TAME_SOUND = registerSoundEvent("wusaqi_tame_sound");
        XIAOBA_TAME_SOUND = registerSoundEvent("xiaoba_tame_sound");
        LIZIMANTOU_TAME_SOUND = registerSoundEvent("lizimantou_tame_sound");
        FEISHU_TAME_SOUND = registerSoundEvent("feishu_tame_sound");
        YUGUIGOU_TAME_SOUND = registerSoundEvent("yuguigou_tame_sound");
        SHISA_TAME_SOUND = registerSoundEvent("shisa_tame_sound");
        LAISHI_TAME_SOUND = registerSoundEvent("laishi_tame_sound");
        NAILONG_TAME_SOUND = registerSoundEvent("nailong_tame_sound");
        
        // 受伤声音
        JIYI_GETHURT_SOUND = registerSoundEvent("jiyi_gethurt_sound");
        WUSAQI_GETHURT_SOUND = registerSoundEvent("wusaqi_gethurt_sound");
        XIAOBA_GETHURT_SOUND = registerSoundEvent("xiaoba_gethurt_sound");
        LIZIMANTOU_GETHURT_SOUND = registerSoundEvent("lizimantou_gethurt_sound");
        FEISHU_GETHURT_SOUND = registerSoundEvent("feishu_gethurt_sound");
        YUGUIGOU_GETHURT_SOUND = registerSoundEvent("yuguigou_gethurt_sound");
        SHISA_GETHURT_SOUND = registerSoundEvent("shisa_gethurt_sound");
        LAISHI_GETHURT_SOUND = registerSoundEvent("laishi_gethurt_sound");
        NAILONG_GETHURT_SOUND = registerSoundEvent("nailong_gethurt_sound");
        
        // 死亡声音
        JIYI_DEATH_SOUND = registerSoundEvent("jiyi_death_sound");
        WUSAQI_DEATH_SOUND = registerSoundEvent("wusaqi_death_sound");
        XIAOBA_DEATH_SOUND = registerSoundEvent("xiaoba_death_sound");
        LIZIMANTOU_DEATH_SOUND = registerSoundEvent("lizimantou_death_sound");
        FEISHU_DEATH_SOUND = registerSoundEvent("feishu_death_sound");
        YUGUIGOU_DEATH_SOUND = registerSoundEvent("yuguigou_death_sound");
        SHISA_DEATH_SOUND = registerSoundEvent("shisa_death_sound");
        LAISHI_DEATH_SOUND = registerSoundEvent("laishi_death_sound");
        NAILONG_DEATH_SOUND = registerSoundEvent("nailong_death_sound");
        
        // 武器和脚步声
        WUSAQI_ATTACK_SOUND = registerSoundEvent("wusaqi_attack_sound");
        WUSAQI_BOOT_1 = registerSoundEvent("wusaqi_boot_1");
        WUSAQI_BOOT_2 = registerSoundEvent("wusaqi_boot_2");
        WUSAQI_BOOT_3 = registerSoundEvent("wusaqi_boot_3");
        WUSAQI_BOOT_4 = registerSoundEvent("wusaqi_boot_4");
        WUSAQI_HENG = registerSoundEvent("wusaqi_heng");
    }
}