package com.yuntang.popularbiology.world;

import com.yuntang.popularbiology.init.InitEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
//import net.minecraft.world.entity.SpawnPlacements;
//import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
//import net.minecraft.world.level.levelgen.Heightmap.Types;
//import net.neoforged.bus.api.SubscribeEvent; // 修改事件总线导入
//import net.neoforged.bus.api.IEventBus;
//import net.neoforged.fml.common.EventBusSubscriber;
//import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent; // 修改 CommonSetupEvent 导入
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent; // 修改 SpawnPlacementRegisterEvent 导入

//@EventBusSubscriber(modid = "popularbiology")
public class PetSpawn {
 //   @SubscribeEvent
    public static void addEntitySpawnPlacement(RegisterSpawnPlacementsEvent event) {
        // 在MC1.21.1 NeoForge中，需要使用SpawnPlacementRegisterEvent来注册生成位置
        event.register(
            InitEntity.WUSAQI.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.XIAOBA.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.JIYI.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.FEISHU.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.LAISHI.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.LIZIMANTOU.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.SHISA.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.NAILONG.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
        
        event.register(
            InitEntity.YUGUIGOU.get(), 
            SpawnPlacementTypes.ON_GROUND, 
            Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, 
            PetSpawn::isSpawn, 
            RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

    public static boolean isSpawn(EntityType<?> entityType, ServerLevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos pos, RandomSource randomSource) {
        boolean validGround = levelAccessor.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON);
        boolean hasSkylight = levelAccessor.canSeeSky(pos);
        boolean enoughLight = levelAccessor.getRawBrightness(pos, 0) > 8;
        return validGround && hasSkylight && enoughLight;
    }
}