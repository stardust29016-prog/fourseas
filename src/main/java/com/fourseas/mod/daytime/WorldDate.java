package com.fourseas.mod.daytime;

import com.lunaclock.api.CalendarUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class WorldDate {

    /** 客户端缓存的季节：0春 1夏 2秋 3冬 */
    private static int clientSeason = 0;

    public static void setClientSeason(int season) {
        clientSeason = season;
    }

    public static int getClientSeason() {
        return clientSeason;
    }

    /**
     * 服务端：读日历
     * 客户端：读同步过来的缓存
     */
    public static long getSeason(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return CalendarUtil.getSeasonName(serverLevel);
        }
        return clientSeason;
    }

    // 下面两个若别处还在用，先留着；客户端在没同步前可能不准
    public static long getDay(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return CalendarUtil.getDayInSeason(serverLevel);
        }
        return 1;
    }

    public static long getYear(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return CalendarUtil.getYear(serverLevel);
        }
        return 1;
    }
}