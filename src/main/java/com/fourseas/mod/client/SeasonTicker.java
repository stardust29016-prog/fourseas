package com.fourseas.mod.client;

import com.fourseas.mod.FourSeas;
import com.fourseas.mod.daytime.WorldDate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = FourSeas.MODID, value = Dist.CLIENT)
public final class SeasonTicker {
    public static int season = -1;
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        //空指针保护
        if (Minecraft.getInstance().level == null) return;
        ClientLevel level = Minecraft.getInstance().level;
        //判断当前季节
         int checkSeason = (int)WorldDate.getSeason(level);
         //判断是否换季
        if(checkSeason != season){
            season = checkSeason;
            SeasonRefresh.triggerBigRefresh();
        }else {
            //日常小刷新
            if (level.getGameTime() % 80 == 0) {
                SeasonRefresh.triggerSmallRefresh();
            }
        }
    }
}