package com.fourseas.mod.network;

import com.fourseas.mod.daytime.WorldDate;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * 服务端 → 客户端：只同步季节（0~3）
 */
public record CalendarSyncPayload(int season) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<CalendarSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    Identifier.fromNamespaceAndPath("fourseas", "calendar_sync")
            );

    public static final StreamCodec<ByteBuf, CalendarSyncPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    CalendarSyncPayload::season,
                    CalendarSyncPayload::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    /** 客户端收到包时调用 */
    public static void handle(CalendarSyncPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            WorldDate.setClientSeason(payload.season());
            // 调试用，确认能收到可以先留着，稳定后删掉
            System.out.println("[fourseas] 客户端收到季节: " + payload.season());
        });
    }
}