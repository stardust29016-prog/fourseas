package com.fourseas.mod.client.mixin;

import com.fourseas.mod.daytime.WorldDate;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeClientMixin {

    @Inject(
            method = "getPrecipitationAt",
            at = @At("RETURN"),
            cancellable = true
    )
    private void fourseas$forceSnowInWinterClient(
            BlockPos pos,
            int seaLevel,

            CallbackInfoReturnable<Biome.Precipitation> cir
    ) {
        if (cir.getReturnValue() != Biome.Precipitation.RAIN) {
            return;
        }

        Level level = Minecraft.getInstance().level;
        if (level == null) {
            return;
        }

        if ((int) WorldDate.getSeason(level) == 3) {
            cir.setReturnValue(Biome.Precipitation.SNOW);
        }
    }
}