package com.fourseas.mod.client.mixin;
import com.fourseas.mod.daytime.WorldDate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Biome.class)
public class BiomeMixin {

    @Inject(
            method = "getPrecipitationAt",
            at = @At("RETURN"),
            cancellable = true
    )
    private void fourseas$forceSnowInWinterServer(
            BlockPos pos,
            int seaLevel,
            CallbackInfoReturnable<Biome.Precipitation> cir
    ) {
        if (cir.getReturnValue() != Biome.Precipitation.RAIN) {
            return;
        }

        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        if (server == null) {
            return;
        }

        ServerLevel level = server.overworld();
        if ((int) WorldDate.getSeason(level) == 3) {
            cir.setReturnValue(Biome.Precipitation.SNOW);
        }
    }
}