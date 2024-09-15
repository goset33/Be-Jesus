package com.goset33.jesus.event;

import com.goset33.jesus.Jesus;
import com.goset33.jesus.mixin.ZombieVillagerInvoker;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OnAttackEntity implements AttackEntityCallback {
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        if (!player.isSpectator()) {
            if (!(entity instanceof ZombieVillagerEntity)) {
                LivingEntity livingEntity = (LivingEntity) entity;
                livingEntity.addStatusEffect(new StatusEffectInstance(
                        StatusEffects.LEVITATION, 100, 0, true, true)); // Не летает, но эффект вроде есть
                Jesus.LOGGER.info(livingEntity.getStatusEffects().toString());
            } else {
                ZombieVillagerEntity zombieVillager = (ZombieVillagerEntity) entity;
                if (!zombieVillager.isConverting()) {
                    ((ZombieVillagerInvoker) zombieVillager).invokeSetConverting(player.getUuid(), 5); // Не превращается
                }
                Jesus.LOGGER.info(String.valueOf(zombieVillager.isConverting()));
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}
