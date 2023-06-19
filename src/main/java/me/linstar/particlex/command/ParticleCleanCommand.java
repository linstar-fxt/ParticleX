package me.linstar.particlex.command;

import com.mojang.brigadier.CommandDispatcher;
import me.linstar.particlex.ParticleX;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.Collection;

public class ParticleCleanCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(CommandManager.literal("particle_clean").then(CommandManager.argument("players", EntityArgumentType.players()).executes(context -> execute(context.getSource(), EntityArgumentType.getPlayers(context, "players")))));
    }
    public static int execute(ServerCommandSource source, Collection<ServerPlayerEntity> players){
        for(ServerPlayerEntity player :players){
            ServerPlayNetworking.send(player, new Identifier(ParticleX.REGISTER_NAME, "clean"), PacketByteBufs.create());
        }
        return 0;
    }
}
