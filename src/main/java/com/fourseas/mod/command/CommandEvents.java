package com.fourseas.mod.command;
import com.fourseas.mod.FourSeas;
import com.lunaclock.api.CalendarUtil;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ServerLevelData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

// 注意：注册指令通常不需要区分客户端和服务端，所以不用加 value = Dist.CLIENT
@EventBusSubscriber(modid = FourSeas.MODID)
public class CommandEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        // 注册指令 /setday <天数>
        dispatcher.register(Commands.literal("setday")
                // 权限等级 2 = 需要管理员/作弊模式
                // 使用 Commands.hasPermission 并传入等级常量，2级对应 LEVEL_GAMEMASTERS
                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                // 接收一个名为 "days" 的整数参数，且不能小于 0
                .then(Commands.argument("days", IntegerArgumentType.integer(0))
                        .executes(context -> {
                            // 1. 获取玩家输入的参数
                            int days = IntegerArgumentType.getInteger(context, "days");

                            // 2. 获取主世界
                            ServerLevel level = context.getSource().getServer().overworld();
                            CalendarUtil.setDay(level,days);
                            context.getSource().sendSuccess(() ->
                                    Component.literal("§a[四季] 已将时间修改为第 " + days + " 天！"), true);

                            return 1; // 返回 1 代表执行成功
                        })
                )
        );
        // 注册 /getday
        dispatcher.register(Commands.literal("getday")
                .executes(context -> {
                    ServerLevel level = context.getSource().getServer().overworld();
                    int currentDay = CalendarUtil.getDayInSeason(level);
                    context.getSource().sendSuccess(() ->
                                    Component.literal("§e[四季] 当前是第 " + currentDay + " 天。"),
                            false
                    );
                    return 1;
                })
        );
    }
}