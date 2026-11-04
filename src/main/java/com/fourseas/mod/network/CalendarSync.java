package com.fourseas.mod.network;

import com.lunaclock.api.CalendarUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public class CalendarSync {

    public static void sendToPlayer(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }
        int season = CalendarUtil.getSeasonName(level);
        PacketDistributor.sendToPlayer(player, new CalendarSyncPayload(season));
    }

    public static void sendToLevel(ServerLevel level) {
        int season = CalendarUtil.getSeasonName(level);
        CalendarSyncPayload payload = new CalendarSyncPayload(season);
        for (ServerPlayer player : level.players()) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }
}