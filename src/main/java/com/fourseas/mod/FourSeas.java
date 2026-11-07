package com.fourseas.mod;

import com.fourseas.mod.network.ModNetwork;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;


@Mod(FourSeas.MODID)
public class FourSeas {
    public static final String MODID = "fourseas";
    public static final Logger LOGGER = LogUtils.getLogger();
    public FourSeas(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(ModNetwork::register);
    }
}
