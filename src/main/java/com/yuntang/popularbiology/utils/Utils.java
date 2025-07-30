package com.yuntang.popularbiology.utils;

import com.google.common.collect.ImmutableList;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.util.Pair;
import com.yuntang.popularbiology.entity.BasePet;
import com.yuntang.popularbiology.entity.ai.task.tameable.KeepOwnerOrHomeAroundTask;
import com.yuntang.popularbiology.entity.ai.task.tameable.PickUpItemTask;
import com.yuntang.popularbiology.entity.ai.task.tameable.RandomWalkTask;
import com.yuntang.popularbiology.entity.ai.task.tameable.SitTask;
import com.yuntang.popularbiology.entity.ai.task.tameable.TalkTask;
import com.yuntang.popularbiology.init.InitTag;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.BehaviorControl;
import net.minecraft.world.entity.ai.behavior.DoNothing;
import net.minecraft.world.entity.ai.behavior.LookAtTargetSink;
import net.minecraft.world.entity.ai.behavior.MoveToTargetSink;
import net.minecraft.world.entity.ai.behavior.RunOne;
import net.minecraft.world.entity.ai.behavior.SetEntityLookTarget;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.*;
import net.minecraft.world.entity.TamableAnimal;
// NeoForge的FakePlayer相关导入
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {
    private static final Logger LOGGER = LogManager.getLogger(Utils.class);

    public static void putGlobalPos(CompoundTag tag, String key, GlobalPos pos) {
        CompoundTag posTag = new CompoundTag();
        posTag.putString("Dimension", pos.dimension().location().toString());
        BlockPos blockPos = pos.pos();
        posTag.putInt("X", blockPos.getX());
        posTag.putInt("Y", blockPos.getY());
        posTag.putInt("Z", blockPos.getZ());
        tag.put(key, posTag);
    }

    public static GlobalPos getGlobalPos(CompoundTag tag, String key, ServerLevel fallbackLevel) {
        if (!tag.contains(key)) {
            return null;
        } else {
            CompoundTag posTag = tag.getCompound(key);
            ResourceLocation dimId = ResourceLocation.parse(posTag.getString("Dimension")); // 1.21.1中使用parse替代构造函数
            ResourceKey<Level> dimensionKey = ResourceKey.create(Registries.DIMENSION, dimId);
            BlockPos pos = new BlockPos(posTag.getInt("X"), posTag.getInt("Y"), posTag.getInt("Z"));
            ServerLevel targetLevel = fallbackLevel.getServer().getLevel(dimensionKey);
            return targetLevel != null ? GlobalPos.of(dimensionKey, pos) : null;
        }
    }

    public static void setHomeMemory(BasePet pet, BlockPos pos, ServerLevel fallbackLevel) {
        pet.getBrain().setMemory(MemoryModuleType.HOME, GlobalPos.of(pet.level().dimension(), pos));
    }

    public static boolean isSafePosition(BasePet pet, BlockPos pos, ServerLevel world) {
        PathType pathNodeType = WalkNodeEvaluator.getPathTypeStatic(pet, pos.mutable());
        if (pathNodeType != PathType.WALKABLE && pathNodeType != PathType.WATER) {
            return false;
        } else {
            BlockPos blockPos = pos.subtract(pet.blockPosition());
            return world.noCollision(pet, pet.getBoundingBox().move(blockPos));
        }
    }

    public static BlockPos getNearestBlockPos(List<BlockPos> list, BasePet entity) {
        if (list != null && !list.isEmpty()) {
            double ex = entity.getX();
            double ey = entity.getY();
            double ez = entity.getZ();
            BlockPos nearest = null;
            double minDistSq = Double.MAX_VALUE;
            Iterator var11 = list.iterator();

            while(var11.hasNext()) {
                BlockPos pos = (BlockPos)var11.next();
                double dx = (double)pos.getX() + 0.5D - ex;
                double dy = (double)pos.getY() + 0.5D - ey;
                double dz = (double)pos.getZ() + 0.5D - ez;
                double distSq = dx * dx + dy * dy + dz * dz;
                if (distSq == 0.0D) {
                    return pos;
                }

                if (distSq < minDistSq) {
                    minDistSq = distSq;
                    nearest = pos;
                }
            }

            return nearest;
        } else {
            return null;
        }
    }

    public static boolean canHarvesr(ServerLevel world, BlockPos pos) {
        boolean var10000;
        BlockState state;
        boolean isCrop;
        label33: {
            state = world.getBlockState(pos);
            isCrop = state.is(InitTag.ENTITY_HARVEST_CROPS);
            Block var6 = state.getBlock();
            if (var6 instanceof CropBlock) {
                CropBlock crop = (CropBlock)var6;
                if (crop.isMaxAge(state)) {
                    var10000 = true;
                    break label33;
                }
            }

            var10000 = false;
        }

        boolean isMaxAge = var10000;
        boolean isMelonOrPumpkin = state.is(Blocks.MELON) || state.is(Blocks.PUMPKIN);
        return isCrop && (isMaxAge || isMelonOrPumpkin);
    }

    public static boolean canReach(BasePet entity, BlockPos pos) {
        PathNavigation navigation = entity.getNavigation();
        Path path = navigation.createPath(pos, 0);
        return path != null && path.canReach();
    }

    public static boolean isCanPlantFarmland(ServerLevel world, BlockPos pos) {
        return world.getBlockState(pos).is(Blocks.FARMLAND) && world.getBlockState(pos.above()).isAir();
    }

    public static ItemStack getSeed(BasePet pet) {
        for(int i = 0; i < pet.getInventory().getContainerSize(); ++i) {
            ItemStack item = pet.getInventory().getItem(i);
            if (item.is(InitTag.ENTITY_PLANT_CROPS)) {
                return item;
            }
        }

        return null;
    }

    public static List<ItemStack> isFitCondition(SimpleContainer inventory, TagKey<Item> tag) {
        List<ItemStack> list = new ArrayList();

        for(int i = 0; i < inventory.getContainerSize(); ++i) {
            ItemStack item = inventory.getItem(i);
            if (item.is(tag)) {
                list.add(item);
            }
        }

        if (list.isEmpty()) {
            return null;
        } else {
            return list;
        }
    }

    public static void removeItems(Container inventory, Item targetItem, int removeCount) {
        int remaining = removeCount;

        for(int i = 0; i < inventory.getContainerSize() && remaining > 0; ++i) {
            ItemStack stack = inventory.getItem(i);
            if (ItemStack.matches(stack, new ItemStack(targetItem))) {
                int removeAmount = Math.min(stack.getCount(), remaining);
                stack.shrink(removeAmount);
                remaining -= removeAmount;
                inventory.setItem(i, stack.isEmpty() ? ItemStack.EMPTY : stack);
            }
        }
    }

    // NeoForge中FakePlayer的创建方式发生了变化
    public static FakePlayer getFakePlayer(ServerLevel level) {
        GameProfile profile = new GameProfile(UUID.randomUUID(), "PopularBiology");
        // NeoForge使用不同的方式创建FakePlayer
        return new FakePlayer(level, profile);
    }

    public static List<ItemStack> getItems(List<ItemStack> list, int count) {
        List<ItemStack> result = new ArrayList();
        int remaining = count;
        Iterator var4 = list.iterator();

        while(var4.hasNext()) {
            ItemStack sourceStack = (ItemStack)var4.next();
            if (remaining <= 0) {
                break;
            }

            ItemStack stack = sourceStack.copy();
            int transferAmount = Math.min(remaining, stack.getCount());
            ItemStack splitStack = stack.split(transferAmount);
            Iterator var9 = result.iterator();

            while(var9.hasNext()) {
                ItemStack existing = (ItemStack)var9.next();
                if (ItemStack.isSameItem(existing, splitStack) ) {
                    int canMerge = existing.getMaxStackSize() - existing.getCount();
                    if (canMerge > 0) {
                        int mergeAmount = Math.min(splitStack.getCount(), canMerge);
                        existing.grow(mergeAmount);
                        splitStack.shrink(mergeAmount);
                        remaining -= mergeAmount;
                    }
                }
            }

            if (!splitStack.isEmpty()) {
                result.add(splitStack);
                remaining -= splitStack.getCount();
            }
        }

        return result;
    }

    public static boolean canInsertContainer(ServerLevel level, BlockPos pos, BasePet pet) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof Container)) {
            return false;
        } else {
            Container container = (Container)blockEntity;
            List itemsToDeliver = isFitCondition(pet.getInventory(), InitTag.ENTITY_DELIVER_ITEM);
            if (itemsToDeliver != null && !itemsToDeliver.isEmpty()) {
                Iterator var6 = itemsToDeliver.iterator();

                while(var6.hasNext()) {
                    ItemStack itemStack = (ItemStack)var6.next();
                    ItemStack remaining = itemStack.copy();

                    for(int slot = 0; slot < container.getContainerSize(); ++slot) {
                        ItemStack slotStack = container.getItem(slot);
                        int space;
                        if (slotStack.isEmpty()) {
                            space = Math.min(remaining.getCount(), remaining.getMaxStackSize());
                            if (space > 0) {
                                return true;
                            }
                        } else if (ItemStack.isSameItem(slotStack, remaining) ) {
                            space = slotStack.getMaxStackSize() - slotStack.getCount();
                            if (space > 0) {
                                return true;
                            }
                        }
                    }
                }

                return false;
            } else {
                return false;
            }
        }
    }

    public static ItemStack getArrow(BasePet pet) {
        for(int i = 0; i < pet.getInventory().getContainerSize(); ++i) {
            ItemStack item = pet.getInventory().getItem(i);
            if (item.is(Items.ARROW) || item.is(Items.TIPPED_ARROW)) {
                return item;
            }
        }

        return ItemStack.EMPTY;
    }

    public static class AIUtils {
        public static void addCoreTasks(Brain<BasePet> brain) {
            Pair<Integer, BehaviorControl<? super BasePet>> talk = Pair.of(0, new TalkTask());
            Pair<Integer, BehaviorControl<? super BasePet>> sit = Pair.of(0, new SitTask());
            Pair<Integer, BehaviorControl<? super BasePet>> keepOwnerOrHomeAround = Pair.of(2, new KeepOwnerOrHomeAroundTask(7.0F, 13.0F, 20.0F));
            Pair<Integer, BehaviorControl<? super BasePet>> walkToTarget = Pair.of(1, new MoveToTargetSink());
            Pair<Integer, BehaviorControl<? super BasePet>> look = Pair.of(0, new LookAtTargetSink(45, 90));
            Pair<Integer, BehaviorControl<? super BasePet>> pickUpItem = Pair.of(3, new PickUpItemTask(0.7F));
            brain.addActivity(Activity.CORE, ImmutableList.of(talk, look, sit, walkToTarget, keepOwnerOrHomeAround, pickUpItem));
        }

        public static void addIdleTasks(Brain<BasePet> brain) {
            Pair<Integer, BehaviorControl<? super BasePet>> randomTask = Pair.of(99, getLookAndRandomWalk());
            brain.addActivity(Activity.IDLE, ImmutableList.of(randomTask));
        }

        private static RunOne getLookAndRandomWalk() {
            Pair<BehaviorControl<? super BasePet>, Integer> lookToPlayer = Pair.of(SetEntityLookTarget.create(EntityType.PLAYER, 5.0F), 2);
            Pair<BehaviorControl<? super BasePet>, Integer> lookToAny = Pair.of(SetEntityLookTarget.create(MobCategory.CREATURE, 5.0F), 2);
            Pair<BehaviorControl<? super BasePet>, Integer> walkRandomly = Pair.of(new RandomWalkTask(), 2);
            Pair<BehaviorControl<? super BasePet>, Integer> noThing = Pair.of(new DoNothing(30, 60), 1);
            return new RunOne(ImmutableList.of(lookToPlayer, lookToAny, walkRandomly, noThing));
        }
    }

    public static class DetectAreaIsFitCondition {
        public static Optional<BlockPos> spiralBlockSearch(ServerLevel level, BasePet pet, int maxRadius, int verticalRange, Utils.DetectAreaIsFitCondition.BlockCheckPredicate predicate) {
            BlockPos center = pet.blockPosition();

            for(int radius = 0; radius <= maxRadius; ++radius) {
                for(int quadrant = 0; quadrant < 4; ++quadrant) {
                    for(int i = -radius; i <= radius; ++i) {
                        for(int y = -verticalRange; y <= verticalRange; ++y) {
                            BlockPos pos = calculateSpiralPos(center, radius, quadrant, i, y);
                            if (predicate.test(level, pos, pet)) {
                                return Optional.of(pos);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }

        private static BlockPos calculateSpiralPos(BlockPos center, int radius, int quadrant, int i, int y) {
            BlockPos var10000;
            switch(quadrant) {
                case 0:
                    var10000 = center.offset(radius, y, i);
                    break;
                case 1:
                    var10000 = center.offset(-radius, y, i);
                    break;
                case 2:
                    var10000 = center.offset(i, y, radius);
                    break;
                case 3:
                    var10000 = center.offset(i, y, -radius);
                    break;
                default:
                    var10000 = center;
            }

            return var10000;
        }

        @FunctionalInterface
        public interface BlockCheckPredicate {
            boolean test(ServerLevel var1, BlockPos var2, BasePet var3);
        }
    }
}
