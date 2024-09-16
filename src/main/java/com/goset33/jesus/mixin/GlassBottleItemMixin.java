package com.goset33.jesus.mixin;

import com.goset33.jesus.Jesus;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GlassBottleItem.class)
public abstract class GlassBottleItemMixin extends Item {
    @Shadow
    protected abstract ItemStack fill(ItemStack stack, PlayerEntity player, ItemStack outputStack);

    public GlassBottleItemMixin(Item.Settings settings) {
        super(settings);
    }

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    private void AddReactionToWine(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> info) {
        ItemStack stack = user.getStackInHand(hand);
        BlockHitResult hitResult = GlassBottleItem.raycast(world, user, RaycastContext.FluidHandling.SOURCE_ONLY);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            BlockPos pos = hitResult.getBlockPos();
            if (world.getFluidState(pos).getFluid() == Jesus.STILL_WINE) {
                world.playSound(user, user.getX(), user.getY(), user.getZ(), SoundEvents.ITEM_BOTTLE_FILL, SoundCategory.NEUTRAL, 1.0f, 1.0f);
                world.emitGameEvent(user, GameEvent.FLUID_PICKUP, pos);
                info.setReturnValue(TypedActionResult.success(this.fill(stack, user, new ItemStack(Jesus.WINE_BOTTLE))));
            }
        }
    }
}
