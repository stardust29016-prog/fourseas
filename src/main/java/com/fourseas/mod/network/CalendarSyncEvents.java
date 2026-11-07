package com.fourseas.mod.network;

import com.lunaclock.api.CalendarUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

@EventBusSubscriber(modid = "fourseas")
public class CalendarSyncEvents {

    /** 上次同步时的天数，-1 表示还没记过 */
    private static int lastSyncedDay = -1;
    private static final int CHECK_INTERVAL = 20;
    private static int tickCounter = 0;

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CalendarSync.sendToPlayer(player);
            // 记下当前天数，避免进服后又立刻因“换天”
            if (player.level() instanceof ServerLevel level) {
                lastSyncedDay = CalendarUtil.getcurrentDay(level);
            }
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CalendarSync.sendToPlayer(player);
        }
    }

    /**
     * 检测日历天数是否增加/被修改，变了就同步给所有玩家
     */
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        tickCounter++;
        if (tickCounter < CHECK_INTERVAL) {
            return;
        }
        tickCounter = 0;

        ServerLevel level = event.getServer().overworld();
        int currentDay = CalendarUtil.getcurrentDay(level);

        if (lastSyncedDay == -1) {
            lastSyncedDay = currentDay;
            return;
        }

        if (currentDay != lastSyncedDay) {
            lastSyncedDay = currentDay;
            CalendarSync.sendToLevel(level);
            // 调试用，稳定后可删
            System.out.println("[fourseas] 检测到换天，同步季节 day=" + currentDay
                    + " season=" + CalendarUtil.getSeasonName(level));
        }
    }
}