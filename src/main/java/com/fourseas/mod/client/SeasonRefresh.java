package com.fourseas.mod.client;
import com.fourseas.mod.FourSeas;
import it.unimi.dsi.fastutil.ints.IntArraySet;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = FourSeas.MODID,value = Dist.CLIENT)
public class SeasonRefresh {

    /**
     * 换季大刷新（只在季节改变的那一瞬间调用一次！）
     * 作用：把视距内所有区块的地表颜色彻底刷新一遍，清空缓存。
     * 耗时：主线程 1~3 毫秒。渲染线程会在接下来 1~2 秒内平滑加载完毕。
     */
    public static void triggerBigRefresh() {
        ClientLevel level = Minecraft.getInstance().level;
        var player = Minecraft.getInstance().player;
        if (level == null || player == null) return;

        // 1. 极其重要：清空染色缓存，否则颜色处理器算出新颜色也用不上
        level.clearTintCaches();

        var lr = Minecraft.getInstance().levelRenderer;
        var centerPos = SectionPos.of(player.getOnPos());

        // 2. 先保底：强制刷新玩家周围 3x3x3 的区块（保证眼前瞬间变色！）
        lr.setSectionDirtyWithNeighbors(centerPos.x(), centerPos.y(), centerPos.z());

        int d = Minecraft.getInstance().options.renderDistance().get();

        // 3. 遍历视距内所有已加载区块，全量标记地表
        for (int cx = centerPos.x() - d; cx <= centerPos.x() + d; cx++) {
            for (int cz = centerPos.z() - d; cz <= centerPos.z() + d; cz++) {
                if (!level.hasChunk(cx, cz)) continue;

                var chunk = level.getChunk(cx, cz);

                // 4. 收集该区块 16x16 每一列的地表高度（去重），解决高山、树顶漏刷的问题
                IntArraySet set = new IntArraySet();
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        int height = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);
                        set.add(SectionPos.posToSectionCoord(height));
                    }
                }

                // 5. 把收集到的每一个 Y 层都标记为脏
                for (IntIterator it = set.iterator(); it.hasNext(); ) {
                    int sy = it.nextInt();
                    lr.setSectionDirty(cx, sy, cz);
                }
            }
        }
    }

    /**
     * 日常小刷新（用在 Tick 里，每隔 3~4 秒调一次）
     * 作用：缓慢把远处的颜色“渗透”过去，或者防止玩家跑图时露馅。
     * 开销：极低。
     */
    public static void triggerSmallRefresh() {
        ClientLevel level = Minecraft.getInstance().level;
        var player = Minecraft.getInstance().player;
        if (level == null || player == null) return;

        var lr = Minecraft.getInstance().levelRenderer;
        var centerPos = SectionPos.of(player.getOnPos());
        var random = level.getRandom();
        int d = Minecraft.getInstance().options.renderDistance().get();

        // 1. 刷新玩家脚下及周围的区块（高频维护）
        lr.setSectionDirtyWithNeighbors(centerPos.x(), centerPos.y(), centerPos.z());

        // 2. 在视距内随机挑 4~8 个区块刷一刷，让远处的颜色不知不觉变过去
        int randomCount = random.nextInt(5) + 4;
        for (int i = 0; i < randomCount; i++) {
            int dx = random.nextInt(d * 2) - d;
            int dz = random.nextInt(d * 2) - d;

            int cx = centerPos.x() + dx;
            int cz = centerPos.z() + dz;

            if (level.hasChunk(cx, cz)) {
                // 随机位置就不去算精准高度了，直接用玩家所在的 Y 层附近刷一下就行
                lr.setSectionDirtyWithNeighbors(cx, centerPos.y(), cz);
            }
        }
    }
}