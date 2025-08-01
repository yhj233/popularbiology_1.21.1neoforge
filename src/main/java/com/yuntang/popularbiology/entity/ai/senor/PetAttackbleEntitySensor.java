package com.yuntang.popularbiology.entity.ai.senor;

import com.google.common.collect.ImmutableSet;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.state.PetState;
import com.yuntang.popularbiology.init.InitTag;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.AABB;

public class PetAttackbleEntitySensor extends Sensor<BasePet> {
   public Set<MemoryModuleType<?>> requires() {
      return ImmutableSet.of(MemoryModuleType.ATTACK_TARGET);
   }

   public PetAttackbleEntitySensor() {
      super(60);
   }

   protected void doTick(ServerLevel level, BasePet entity) {
      if (entity.getState() == PetState.WORK && (entity.getJobId() == 2 || entity.getJobId() == 3) && entity.getAction() == 0) {
         entity.getBrain().getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent((hurtByEntity) -> {
            entity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, hurtByEntity);
         });
         entity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent((attackTarget) -> {
            if (attackTarget.distanceTo(entity) <= 15.0F && attackTarget.isAlive()) {
               entity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
            }
         });
         AABB searchBox = entity.getBoundingBox().inflate(15.0D);
         List<Entity> nearbyEntities = level.getEntities(entity, searchBox, (e) -> {
            return e.getType().is(InitTag.ENTITY_HOSTILE_ENTITY) && e.isAlive();
         });
         Optional<Entity> closestTarget = nearbyEntities.stream().min((e1, e2) -> {
            return Double.compare(e1.distanceToSqr(entity), e2.distanceToSqr(entity));
         });
         closestTarget.ifPresentOrElse((target) -> {
            entity.getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, (LivingEntity)target);
         }, () -> {
            entity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
         });
      }

   }
}
