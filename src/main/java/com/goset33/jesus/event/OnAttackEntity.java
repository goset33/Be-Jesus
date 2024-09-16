package com.goset33.jesus.event;

import com.goset33.jesus.Jesus;
import com.goset33.jesus.mixin.ZombieVillagerInvoker;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.ZombieVillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class OnAttackEntity implements AttackEntityCallback {
    // Я НЕ ЕБУ КАК ЭТОТ СКРИПТ РАБОТАЕТ, ТАМ КОНСОЛЬ ВСЯ В ОШИБКАХ ПОМОГИТЕ (Пул реквест приветствуется)

    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult) {
        try {
            return healTouch((LivingEntity) entity, (ServerPlayerEntity) player);
        } catch (Exception e) {
            Jesus.LOGGER.error("Exception: " + e);
            return ActionResult.PASS;
        }

    }

    public ActionResult healTouch(LivingEntity entity, ServerPlayerEntity player) {
        if (!player.isSpectator()) {
            if (entity instanceof ZombieVillagerEntity) {
                ZombieVillagerEntity zombieVillager = (ZombieVillagerEntity) entity;
                if (!zombieVillager.isConverting()) {
                    ((ZombieVillagerInvoker) zombieVillager).invokeSetConverting(null, 2);
                }
            } else if (entity instanceof Monster) {
                entity.kill();
                return ActionResult.SUCCESS;
            } else {
                entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 100));
            }
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }
}
