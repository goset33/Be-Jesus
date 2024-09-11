package com.goset33.jesus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JesusClient implements ClientModInitializer {
    public static final String MOD_ID = "Jesus";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        FluidRenderHandlerRegistry.INSTANCE.register(Jesus.STILL_WINE, Jesus.FLOWING_WINE, new SimpleFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow"),
                0xB11226));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), Jesus.STILL_WINE, Jesus.FLOWING_WINE);
    }
}
