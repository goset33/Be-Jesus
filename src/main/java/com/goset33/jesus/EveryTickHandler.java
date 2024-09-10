package com.goset33.jesus;

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

            player.velocityModified = true;
            if (waterBlock.getBlock() == Blocks.WATER && playerBlock.isAir() && !player.isSneaking()) {
                player.setVelocity(player.getVelocity().multiply(1, 0, 1));
                if (player.getY() < playerPos.getY() + 0.9) {
                    player.setPos(player.getX(), playerPos.getY() + 1, player.getZ());
                }
                player.setOnGround(true);
            }

            Jesus.LOGGER.info(player.getPos().toString());
            Jesus.LOGGER.info(playerPos.toString());
        }
    }
}
