package com.fourseas.mod.client.mixin;

import com.fourseas.mod.daytime.SeasonHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SnowLayerBlock.class)
public class SnowLayerBlockMixin {

    @Inject(method = "randomTick", at = @At("HEAD"), cancellable = true)
    private void fourseas$seasonalMelt(
            BlockState state,
            ServerLevel level,
            BlockPos pos,
            RandomSource random,
            CallbackInfo ci
    ) {
        // 冬天：完全走原版（只有强光才化）
        if (SeasonHelper.isWinter(level)) {
            return;
        }

        // 非冬天：有概率化一层（春天更积极）
        int chance = SeasonHelper.isSpring(level) ? 3 : 6; // 春约 1/3，夏秋约 1/6
        if (random.nextInt(chance) != 0) {
            return; // 这次不化，仍可走原版强光逻辑
        }

        IntegerProperty layersProp = SnowLayerBlock.LAYERS;
        int layers = state.getValue(layersProp);

        if (layers > 1) {
            level.setBlock(pos, state.setValue(layersProp, layers - 1), 3);
        } else {
            level.removeBlock(pos, false);
        }

        // 这层已经处理过，取消原版这次 randomTick
        ci.cancel();
    }
}