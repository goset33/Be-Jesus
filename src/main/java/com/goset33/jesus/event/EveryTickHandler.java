package com.goset33.jesus.event;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

public class EveryTickHandler implements ClientTickEvents.StartWorldTick {
    @Override
    public void onStartTick(ClientWorld world) {
        for (PlayerEntity player : world.getPlayers()) {
            BlockPos playerPos = player.getBlockPos();
            BlockState playerBlock = world.getBlockState(playerPos.up());
            BlockState waterBlock = world.getBlockState(playerPos);
            double waterLevel = (waterBlock.getFluidState().getLevel() + 1.0) / 10.0;

            player.velocityModified = true;
            if (waterBlock.getBlock() == Blocks.WATER && playerBlock.isAir() && !player.isSneaking()) {
                player.setPos(player.getX(), playerPos.getY() + waterLevel, player.getZ());
                player.setVelocity(player.getVelocity().multiply(1, 0, 1));
                player.setOnGround(true);
            }
        }
    }
}
