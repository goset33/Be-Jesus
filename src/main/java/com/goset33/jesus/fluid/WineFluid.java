package com.goset33.jesus.fluid;

import com.goset33.jesus.Jesus;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;

public abstract class WineFluid extends JesusFluids {
    @Override
    public Fluid getStill() {
        return Jesus.STILL_WINE;
    }

    @Override
    public Fluid getFlowing() {
        return Jesus.FLOWING_WINE;
    }

    @Override
    public Item getBucketItem() {
        return Jesus.WINE_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return Jesus.WINE.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    public static class Flowing extends WineFluid {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends WineFluid {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
