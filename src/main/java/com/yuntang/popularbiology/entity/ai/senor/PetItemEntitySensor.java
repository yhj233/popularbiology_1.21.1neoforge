package com.yuntang.popularbiology.entity.ai.senor;

import com.google.common.collect.ImmutableSet;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitMemory;
import com.yuntang.popularbiology.utils.Utils;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.AABB;

public class PetItemEntitySensor extends Sensor<BasePet> {
   private static final int VERTICAL_SEARCH_RANGE = 7;

   public PetItemEntitySensor() {
      super(30);
   }

   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of((MemoryModuleType)InitMemory.PICKABLE_ITEM.get());
   }

   protected void doTick(ServerLevel level, BasePet entity) {
      if (entity.isTame() && entity.getState() == PetState.WORK && entity.getAction() == 0) {
         AABB aabb = entity.getBoundingBox().inflate(7.0D, 7.0D, 7.0D);
         List<Entity> allEntities = level.getEntitiesOfClass(Entity.class, aabb, Entity::isAlive);
         Objects.requireNonNull(entity);
         allEntities.sort(Comparator.comparingDouble(entity::distanceToSqr));
         Stream var10000 = allEntities.stream().filter((e) -> {
            return entity.canPickup(e);
         });
         Objects.requireNonNull(entity);
List<Entity> optional = allEntities.stream()
    .filter(e -> entity.canPickup(e))
    .filter(e -> entity.hasLineOfSight(e)) // 改为 lambda 表达式
    .filter(e -> Utils.canReach(entity, e.blockPosition()))
    .collect(Collectors.toList());
         entity.getBrain().setMemory((MemoryModuleType)InitMemory.PICKABLE_ITEM.get(), optional);
      }

   }
}
