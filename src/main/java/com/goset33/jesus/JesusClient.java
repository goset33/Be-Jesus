package com.goset33.jesus;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class JesusClient implements ClientModInitializer {
    public static KeyBinding transformKey;

    @Override
    public void onInitializeClient() {
        transformKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.jesus.transformation",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                "category.jesus.category"));

        FluidRenderHandlerRegistry.INSTANCE.register(Jesus.STILL_WINE, Jesus.FLOWING_WINE, new SimpleFluidRenderHandler(
                Identifier.of("minecraft:block/water_still"),
                Identifier.of("minecraft:block/water_flow"),
                0xB11226));

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), Jesus.STILL_WINE, Jesus.FLOWING_WINE);
    }
}
