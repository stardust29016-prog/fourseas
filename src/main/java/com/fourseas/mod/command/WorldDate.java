package com.fourseas.mod.command;
import net.minecraft.world.level.Level;

public class WorldDate {

    // 每个季节的天数
    public static final int DAYS_PER_SEASON = 28;

    /**
     * 获取当前世界是第几天
     */
    public static int getCurrentDay(Level level) {
        // 原版一天是 24000 tick
        return (int) (level.getDefaultClockTime() / 24000L);
    }

    /**
     * 根据当前世界获取当前季节
     */
    public static Season getSeason(Level level) {
        int currentDay = getCurrentDay(level);

        // 计算是第几个季节 (0=春, 1=夏, 2=秋, 3=冬)
        int seasonIndex = (currentDay / DAYS_PER_SEASON) % 4;

        // 返回对应的枚举
        return Season.values()[seasonIndex];
    }
}
