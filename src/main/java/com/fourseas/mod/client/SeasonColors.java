package com.fourseas.mod.client;

import com.fourseas.mod.FourSeas;
import com.fourseas.mod.daytime.WorldDate;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;

@EventBusSubscriber(modid = FourSeas.MODID,value = Dist.CLIENT)
public class SeasonColors {
    public static final ColorResolver GRASS_COLOR = (biome, posX, posZ) ->
    {
        //草方块
        var level = net.minecraft.client.Minecraft.getInstance().level;
        int check = (int) WorldDate.getSeason(level);
        return switch (check) {
            case 0 -> 0x9DC801;
            case 1 -> 0x91BD59;
            case 2 -> 0xD2B235;
            case 3 -> 0xD0E8F0;
            default -> -1;
        };

    };
    public static final ColorResolver FOLIAGE_COLOR = (biome, posX, posZ) ->
    {
        //树叶
        var level = net.minecraft.client.Minecraft.getInstance().level;
        int check = (int) WorldDate.getSeason(level);
        return switch (check) {
            case 0 -> 0x7BCE56;
            case 1 -> 0x3A9D3A;
            case 2 -> 0xD9A441;
            case 3 -> 0xE8ECEF;
            default -> -1;
        };

    };

    public static int getSpecialLeavesColor(BlockState state, BlockAndTintGetter level, BlockPos pos) {
        Block block = state.getBlock();
        var clientLevel = net.minecraft.client.Minecraft.getInstance().level;
        int check = (int) WorldDate.getSeason(clientLevel);
        //松树
        if (block == Blocks.SPRUCE_LEAVES) {
            return switch (check) {
                case 2 -> ARGB.opaque(0x8F9E4A);
                case 3 -> ARGB.opaque(0x4F6F58);
                default -> ARGB.opaque(0x619961);
            };
        }
        //桦树
        if (block == Blocks.BIRCH_LEAVES) {
            return switch (check) {
                case 0 -> ARGB.opaque(0x80A755);
                case 1 -> ARGB.opaque(0x6B8E23);
                case 2 -> ARGB.opaque(0xD35400);
                case 3 -> ARGB.opaque(0xB8A878);
                default -> -1;
            };
        }
        //红树
        if (block == Blocks.MANGROVE_LEAVES) {
            return switch (check) {
                case 3 -> ARGB.opaque(0xE8ECEF);
                default -> ARGB.opaque(0x619961);
            };
        }
        //樱花树落叶
        if (block == Blocks.PINK_PETALS) {
            return switch (check) {
                case 3 -> ARGB.opaque(0xE8ECEF);
                default -> ARGB.opaque(0xFFB7C5);
            };
        }
        //野花葵落叶
        if (block == Blocks.WILDFLOWERS) {
            return switch (check) {
                case 3 -> ARGB.opaque(0xE8ECEF);
                default -> ARGB.opaque(0xFFFF99);
            };
        }
        return ARGB.opaque(0x48B518);
    }
}
