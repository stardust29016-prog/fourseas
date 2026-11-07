package com.fourseas.mod.command;
import com.fourseas.mod.FourSeas;
import com.fourseas.mod.network.CalendarSync;
import com.lunaclock.api.CalendarUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = FourSeas.MODID)
public class CommandEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        // 注册指令 /setday <天数>
        dispatcher.register(Commands.literal("setday")
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("days", IntegerArgumentType.integer(0))
                        .executes(context -> {
                            // 获取玩家输入的参数
                            int days = IntegerArgumentType.getInteger(context, "days");

                            ServerLevel level = context.getSource().getServer().overworld();
                            CalendarUtil.setDay(level,days);
                            CalendarSync.sendToLevel(level);
                            context.getSource().sendSuccess(() ->
                                    Component.literal("§a[四季] 已将时间修改为第 " + days + " 天！"), true);

                            return 1;
                        })
                )
        );
        // 注册 /getday
        dispatcher.register(Commands.literal("getday")
                .executes(context -> {
                    ServerLevel level = context.getSource().getServer().overworld();
                    int currentDay = CalendarUtil.getDayInSeason(level);
                    context.getSource().sendSuccess(() ->
                                    Component.literal("§e[四季] 当前是本季第 " + currentDay + " 天。"),
                            false
                    );
                    return 1;
                })
        );
    }
}