package com.fourseas.mod.event;

import com.fourseas.mod.daytime.SeasonHelper;
import com.fourseas.mod.daytime.WorldDate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = "fourseas")
public class WeatherHandler {
    private static long lastProcessedDay = -1;

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return;
        if (!level.dimension().equals(Level.OVERWORLD)) return;

        // 只在一天的开始处理一次（0刻 = 早上 6:00）
        long currentDay = WorldDate.getDay(level);
        // 2. 如果还是同一天，直接返回（不执行后面的天气逻辑）
        if (currentDay == lastProcessedDay) return;
        lastProcessedDay = currentDay;
            int season = SeasonHelper.getSeason(level); // 0春1夏2秋3冬
            boolean rainToday = rollRainForSeason(level, season);
            if (rainToday) {
                // 下接近一整天：clear=0，雨持续约 24000 tick
                level.getServer().setWeatherParameters(0, 24000, true, false);
            } else {
                // 晴一天
                level.getServer().setWeatherParameters(24000, 0, false, false);
            }
        }
    //各季节今天下雨的概率
    private static boolean rollRainForSeason(ServerLevel level, int season) {
        float chance = switch (season) {
            case 0 -> 0.35f; // 春：多雨
            case 1 -> 0.20f; // 夏：较少
            case 2 -> 0.30f; // 秋：中等偏多
            case 3 -> 0.25f; // 冬：中等吧,我不会算概率awa
            default -> 0.25f;
        };
        return level.getRandom().nextFloat() < chance;
    }
}
