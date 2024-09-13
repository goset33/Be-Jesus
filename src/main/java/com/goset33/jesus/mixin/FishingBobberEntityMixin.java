package com.goset33.jesus.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin {
    @Accessor("waitCountdown")
    public abstract void setWaitCountdown(int waitCountdown);

    @Inject(method = "tickFishingLogic(Lnet/minecraft/util/math/BlockPos;)V", at = @At("HEAD"))
    private void InstantFish(BlockPos pos, CallbackInfo info) {
        setWaitCountdown(1);
    }

    @Inject(method = "use(Lnet/minecraft/item/ItemStack;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private void MultiplyDrop(ItemStack usedItem, CallbackInfoReturnable<Integer> info, @Local ItemEntity itemEntity, @Local LocalRef<ItemEntity> localRef, @Local LootContextParameterSet lootContextParameterSet, @Local LootTable lootTable) {
        ItemStack containStack = itemEntity.getStack();
        ArrayList<String> junks = new ArrayList<>(Arrays.asList(
                "lily_pad",
                "bowl",
                "fishing_rod",
                "leather",
                "leather_boots",
                "rotten_flesh",
                "stick",
                "string",
                "potion",
                "bone",
                "ink_sac",
                "tripwire_hook"
        ));

        for (String item : junks) {
            while (containStack.getItem().toString().equals(item)) {
                lootTable = ((FishingBobberEntity) (Object) this).getWorld().getServer().getLootManager().getLootTable(LootTables.FISHING_GAMEPLAY);
                ObjectArrayList<ItemStack> list = lootTable.generateLoot(lootContextParameterSet);
                for (ItemStack itemStack : list) {
                    containStack = itemStack;
                }
            }
        }

        containStack.increment(Random.create().nextBetween(1, 2));
        itemEntity.setStack(containStack);
        localRef.set(itemEntity);
    }
}
