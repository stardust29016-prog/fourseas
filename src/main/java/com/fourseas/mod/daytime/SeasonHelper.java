package com.fourseas.mod.daytime;
import com.lunaclock.api.CalendarUtil;
import net.minecraft.server.level.ServerLevel;

import java.util.logging.Level;

public class SeasonHelper {
    //获取季节
    public static int getSeason(ServerLevel level) {
        return (int) WorldDate.getSeason(level);
    }
    //判断是不是冬天
    public static boolean isWinter(ServerLevel level) {
        int season = (int) WorldDate.getSeason(level);
        return season == 3;
    }
    //判断是否是春天
    public static boolean isSpring(ServerLevel level) {
        int season = (int) WorldDate.getSeason(level);
        return season == 0;
    }
}
