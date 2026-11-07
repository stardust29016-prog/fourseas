package com.fourseas.mod.client;
import com.fourseas.mod.FourSeas;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import java.util.List;

//客户端渲染设置
@EventBusSubscriber(modid = FourSeas.MODID,value = Dist.CLIENT)
public class ClientSetup {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            BiomeColors.GRASS_COLOR_RESOLVER = SeasonColors.GRASS_COLOR;
            BiomeColors.FOLIAGE_COLOR_RESOLVER = SeasonColors.FOLIAGE_COLOR;
        });
    }
//注册不能被染色的树叶颜色
    @SubscribeEvent
    public static void onRegisterColorHandlersEvent_Block(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new FoliageColorSource()),
                Blocks.SPRUCE_LEAVES,
                Blocks.BIRCH_LEAVES,
                Blocks.MANGROVE_LEAVES,
                //樱花落叶
                Blocks.PINK_PETALS,
                //野花葵落叶
                Blocks.WILDFLOWERS);

    }
}