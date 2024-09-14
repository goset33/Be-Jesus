package com.goset33.jesus.event;

import com.goset33.jesus.Jesus;
import com.goset33.jesus.JesusClient;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.math.BlockPos;

public class EveryTickHandler implements ClientTickEvents.StartWorldTick, ClientTickEvents.EndTick {
    @Override
    public void onStartTick(ClientWorld world) {
        for (PlayerEntity player : world.getPlayers()) {
            BlockPos playerPos = player.getBlockPos();
            BlockState playerBlock = world.getBlockState(playerPos.up());
            FluidState waterBlock = world.getFluidState(playerPos);
            double waterLevel = (waterBlock.getLevel() + 1.0) / 10.0;

            player.velocityModified = true;
            if ((waterBlock.getFluid() == Fluids.WATER) && playerBlock.isAir() && !player.isSneaking()) {
                player.setPos(player.getX(), playerPos.getY() + waterLevel, player.getZ());
                player.setVelocity(player.getVelocity().multiply(1, 0, 1));
                player.setOnGround(true);
            }
        }
    }

    @Override
    public void onEndTick(MinecraftClient client) {
        if (JesusClient.transformKey.wasPressed()) {
            PlayerEntity player = client.player;
            ItemStack itemStack = player.getMainHandStack();
            ItemStack newWineStack;

            if (itemStack.getItem() == Items.POTION && PotionUtil.getPotion(itemStack) == Potions.WATER) {
                newWineStack = new ItemStack(Jesus.WINE_BOTTLE);
            } else if (itemStack.getItem() == Items.WATER_BUCKET) {
                newWineStack = new ItemStack(Jesus.WINE_BUCKET); // Items.LAVA_BUCKET Jesus.WINE_BUCKET
                player.getInventory().removeOne(itemStack);
            } else {
                player.sendMessage(Text.translatable("message.transform_impossible").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(0xFF5555))), false);
                return;
            }

            player.getInventory().insertStack(newWineStack);
            Jesus.LOGGER.info(itemStack.getItem().toString());
            Jesus.LOGGER.info(newWineStack.getItem().toString());
//            player.getInventory().insertStack(newWineStack);
        }
    }
}