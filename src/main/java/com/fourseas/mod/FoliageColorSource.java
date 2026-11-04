package com.fourseas.mod;

import com.fourseas.mod.client.SeasonColors;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

public class FoliageColorSource implements BlockTintSource {
    @Override
    public int color(@NonNull BlockState state) {
        Block block = state.getBlock();
        if (block == Blocks.SPRUCE_LEAVES) {
            return FoliageColor.FOLIAGE_EVERGREEN;
        } else if (block == Blocks.BIRCH_LEAVES) {
            return FoliageColor.FOLIAGE_BIRCH;
        } else if (block == Blocks.MANGROVE_LEAVES) {
            return FoliageColor.FOLIAGE_MANGROVE;
        }else if (block == Blocks.PINK_PETALS) {
            return FoliageColor.FOLIAGE_MANGROVE;
        }
        return FoliageColor.FOLIAGE_DEFAULT;
    }
    @Override
    public int colorInWorld(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        // 世界里真正渲染时用的颜色
        return SeasonColors.getSpecialLeavesColor(state, level, pos);
    }

}
