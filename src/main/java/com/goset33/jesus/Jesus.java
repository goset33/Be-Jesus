package com.goset33.jesus;

import com.goset33.jesus.event.EveryTickHandler;
import com.goset33.jesus.event.OnAttackEntity;
import com.goset33.jesus.fluid.WineFluid;
import com.goset33.jesus.item.WineBottleItem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginConnectionEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jesus implements ModInitializer {
    public static final String MOD_ID = "jesus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static Block WINE;
    public static FlowableFluid STILL_WINE;
    public static FlowableFluid FLOWING_WINE;
    public static Item WINE_BUCKET;
    public static Item WINE_BOTTLE;

    @Override
    public void onInitialize() {
        ClientLoginConnectionEvents.INIT.register((handler, client) -> {
            IntegratedServer server = client.getServer();
            if (server == null) {
                ServerPlayerEntity player = server.getPlayerManager().getPlayer(client.player.getUuid());
                player.networkHandler.disconnect(Text.translatable("message.disconnect"));
                // Я знаю что message.disconnect никогда не выводится, но это самый простой способ остановить подключение к серверу - создать ошибку
            }
        });

        STILL_WINE = Registry.register(Registries.FLUID, new Identifier(MOD_ID, "wine"), new WineFluid.Still());
        FLOWING_WINE = Registry.register(Registries.FLUID, new Identifier(MOD_ID, "flowing_wine"), new WineFluid.Flowing());
        WINE = Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "wine_block"),
                new FluidBlock(STILL_WINE, FabricBlockSettings.copy(Blocks.WATER)));

        WINE_BUCKET = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "wine_bucket"),
                new BucketItem(STILL_WINE, new FabricItemSettings().rarity(Rarity.RARE).recipeRemainder(Items.BUCKET).maxCount(1)));
        WINE_BOTTLE = Registry.register(Registries.ITEM, new Identifier(MOD_ID, "wine_bottle"),
                new WineBottleItem(new FabricItemSettings().rarity(Rarity.RARE).maxCount(16)));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.add(WINE_BUCKET));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS).register((itemGroup) -> itemGroup.add(WINE_BOTTLE));

        ClientTickEvents.START_WORLD_TICK.register(new EveryTickHandler());
        ClientTickEvents.START_CLIENT_TICK.register(new EveryTickHandler());
        AttackEntityCallback.EVENT.register(new OnAttackEntity());
    }
}
