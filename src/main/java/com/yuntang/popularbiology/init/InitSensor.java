package com.yuntang.popularbiology.init;

import com.yuntang.popularbiology.entity.ai.senor.PetAttackbleEntitySensor;
import com.yuntang.popularbiology.entity.ai.senor.PetContainerSensor;
import com.yuntang.popularbiology.entity.ai.senor.PetHarvestCropSensor;
import com.yuntang.popularbiology.entity.ai.senor.PetItemEntitySensor;
import com.yuntang.popularbiology.entity.ai.senor.PetPlantCropSensor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class InitSensor {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES;
    
    public static final DeferredHolder<SensorType<?>, SensorType<PetHarvestCropSensor>> PET_CROP_SENSOR;
    public static final DeferredHolder<SensorType<?>, SensorType<PetPlantCropSensor>> PET_FARMLAND_SENSOR;
    public static final DeferredHolder<SensorType<?>, SensorType<PetItemEntitySensor>> PET_ITEM_ENTITY_SENSOR;
    public static final DeferredHolder<SensorType<?>, SensorType<PetContainerSensor>> PET_CONTAINER_SENSOR;
    public static final DeferredHolder<SensorType<?>, SensorType<PetAttackbleEntitySensor>> PET_ATTACKBLE_ENTITY_SENSOR;

    public static void register(IEventBus eventBus) {
        SENSOR_TYPES.register(eventBus);
    }

   static {
      SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, "popularbiology");
      PET_CROP_SENSOR = SENSOR_TYPES.register("pet_crop_sensor", () -> {
         return new SensorType(PetHarvestCropSensor::new);
      });
      PET_FARMLAND_SENSOR = SENSOR_TYPES.register("pet_farmland_sensor", () -> {
         return new SensorType(PetPlantCropSensor::new);
      });
      PET_ITEM_ENTITY_SENSOR = SENSOR_TYPES.register("pet_item_entity_sensor", () -> {
         return new SensorType(PetItemEntitySensor::new);
      });
      PET_CONTAINER_SENSOR = SENSOR_TYPES.register("pet_container_sensor", () -> {
         return new SensorType(PetContainerSensor::new);
      });
      PET_ATTACKBLE_ENTITY_SENSOR = SENSOR_TYPES.register("pet_attackble_entity_sensor", () -> {
         return new SensorType(PetAttackbleEntitySensor::new);
      });
   }
}
