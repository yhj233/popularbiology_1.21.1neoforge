package com.yuntang.popularbiology.init;

import com.yuntang.popularbiology.item.JiyiTaoFaBang;
import com.yuntang.popularbiology.item.WusaqiArmorItem;
import com.yuntang.popularbiology.item.WusaqiTaoFaBang;
import com.yuntang.popularbiology.item.XiaobaTaoFaBnag;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitItem {
    public static final DeferredRegister<Item> ITEMS;
    
    // 刷怪蛋
    public static final DeferredHolder<Item, Item> WUSAQI_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> XIAOBA_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> JIYI_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> FEISHU_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> LAISHI_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> LIZIMANTOU_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> NAILONG_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> YUGUIGOU_SPAWN_EGG;
    public static final DeferredHolder<Item, Item> SHISA_SPAWN_EGG;
    
    // 武器
    public static final DeferredHolder<Item, Item> WUSAQI_TAOFABANG;
    public static final DeferredHolder<Item, Item> JIYI_TAOFABANG;
    public static final DeferredHolder<Item, Item> XIAOBA_TAOFABANG;
    
    // 护甲
    public static final DeferredHolder<Item, WusaqiArmorItem> WUSAQI_ARMOR_BOOTS;

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    static {
        ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, "popularbiology");
        
        // 刷怪蛋使用原版 SpawnEggItem
        WUSAQI_SPAWN_EGG = ITEMS.register("wusaqi_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.WUSAQI.get(), 15894791, 15911687, new Item.Properties()));
        
        XIAOBA_SPAWN_EGG = ITEMS.register("xiaoba_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.XIAOBA.get(), 65535, 9031664, new Item.Properties()));
        
        JIYI_SPAWN_EGG = ITEMS.register("jiyi_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.JIYI.get(), 15191244, 15591644, new Item.Properties()));
        
        FEISHU_SPAWN_EGG = ITEMS.register("feishu_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.FEISHU.get(), 703157, 139, new Item.Properties()));
        
        LAISHI_SPAWN_EGG = ITEMS.register("laishi_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.LAISHI.get(), 15400912, 15395562, new Item.Properties()));
        
        LIZIMANTOU_SPAWN_EGG = ITEMS.register("lizimantou_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.LIZIMANTOU.get(), 14336590, 14322510, new Item.Properties()));
        
        NAILONG_SPAWN_EGG = ITEMS.register("nailong_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.NAILONG.get(), 16760576, 16771584, new Item.Properties()));
        
        YUGUIGOU_SPAWN_EGG = ITEMS.register("yuguigou_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.YUGUIGOU.get(), 14941693, 7457230, new Item.Properties()));
        
        SHISA_SPAWN_EGG = ITEMS.register("shisa_spawn_egg", 
            () -> new SpawnEggItem(InitEntity.SHISA.get(), 16753920, 14679808, new Item.Properties()));
        
        // 武器
        WUSAQI_TAOFABANG = ITEMS.register("wusaqi_tao_fa_bang", 
            () -> new WusaqiTaoFaBang(Tiers.IRON, 3, -2.4F, new Item.Properties()));
        
        JIYI_TAOFABANG = ITEMS.register("jiyi_tao_fa_bang", 
            () -> new JiyiTaoFaBang(Tiers.WOOD, 3, -2.4F, new Item.Properties()));
        
        XIAOBA_TAOFABANG = ITEMS.register("xiaoba_tao_fa_bang", 
            () -> new XiaobaTaoFaBnag(Tiers.STONE, 3, -2.4F, new Item.Properties()));
        
        // 护甲
        WUSAQI_ARMOR_BOOTS = ITEMS.register("wusaqi_armor_boots", 
            () -> new WusaqiArmorItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, new Item.Properties()));
    }
}