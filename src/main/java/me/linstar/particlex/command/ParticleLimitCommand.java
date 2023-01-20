package me.linstar.particlex.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.linstar.particlex.until.ParticleConfig;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource;
import net.minecraft.text.LiteralText;

public class ParticleLimitCommand {

    public static void register(){
        CommandDispatcher<FabricClientCommandSource> dispatcher = ClientCommandManager.DISPATCHER;
        ClientCommandManager.DISPATCHER.register(
                dispatcher.register(ClientCommandManager.literal("set_particle_limit").then(ClientCommandManager.argument("limit", IntegerArgumentType.integer()).executes(context -> execute(context.getSource(), IntegerArgumentType.getInteger(context, "limit"))))).createBuilder()
        );
    }
    public static int execute(FabricClientCommandSource source, int max){
        int late = ParticleConfig.getInstance().value;
        ParticleConfig.getInstance().set(max);
        source.sendFeedback(new LiteralText("已将粒子效果上限设置为:"+max+"(原来是:"+late+")"));
        return 0;
    }
}
