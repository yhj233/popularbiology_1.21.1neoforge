package com.yuntang.popularbiology.init;

import com.yuntang.popularbiology.client.render.inventory.PetContainerMenu;
import com.yuntang.popularbiology.entity.BasePet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.core.registries.BuiltInRegistries;

public class InitContainer {
    public static final DeferredRegister<MenuType<?>> CONTAINERS;
    public static final DeferredHolder<MenuType<?>, MenuType<PetContainerMenu>> PET_CONTAINER;

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }

    static {
        CONTAINERS = DeferredRegister.create(BuiltInRegistries.MENU, "popularbiology");
        PET_CONTAINER = CONTAINERS.register("pet_container", () -> {
            return IMenuTypeExtension.create((windowId, inv, data) -> {
                int entityId = data.readInt();
                Level level = inv.player.getCommandSenderWorld();
                Entity entity = level.getEntity(entityId);
                return new PetContainerMenu(windowId, inv, (BasePet) entity);
            });
        });
    }
}