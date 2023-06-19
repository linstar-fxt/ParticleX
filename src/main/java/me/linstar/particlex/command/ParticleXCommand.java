package me.linstar.particlex.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.linstar.particlex.ParticleX;
import me.linstar.particlex.client.CustomParticlePacket;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.command.argument.ParticleEffectArgumentType;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;
import java.util.Objects;

public class ParticleXCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher, CommandRegistryAccess registryAccess){
        dispatcher.register(CommandManager.literal("particlex").requires(source -> source.hasPermissionLevel(2)).then(
                CommandManager.argument("name", ParticleEffectArgumentType.particleEffect(registryAccess)).then(
                        CommandManager.argument("color", Vec3ArgumentType.vec3(false)).then(
                                CommandManager.argument("pos", Vec3ArgumentType.vec3()).then(
                                        CommandManager.argument("delta", Vec3ArgumentType.vec3(false)).then(
                                                CommandManager.argument("speed", FloatArgumentType.floatArg(0F)).then(
                                                        CommandManager.argument("count", IntegerArgumentType.integer(0)).then(
                                                                CommandManager.literal("force").then(
                                                                        CommandManager.argument("time", IntegerArgumentType.integer(0)).then(
                                                                                CommandManager.argument("alpha", FloatArgumentType.floatArg()).then(
                                                                                        CommandManager.argument("scale", FloatArgumentType.floatArg()).then(
                                                                                                CommandManager.argument("viewers", EntityArgumentType.players()).then(
                                                                                                        CommandManager.argument("delay", IntegerArgumentType.integer(0)).then(
                                                                                                                CommandManager.argument("color_delay", IntegerArgumentType.integer(0))
                                                                                                                    .executes(context -> execute(context.getSource(), ParticleEffectArgumentType.getParticle(context, "name"), Vec3ArgumentType.getVec3(context, "color"), Vec3d.ZERO, Vec3ArgumentType.getVec3(context, "pos"), Vec3ArgumentType.getVec3(context, "delta"), FloatArgumentType.getFloat(context, "speed"), IntegerArgumentType.getInteger(context, "count"), true, EntityArgumentType.getPlayers(context, "viewers"), IntegerArgumentType.getInteger(context, "time"), FloatArgumentType.getFloat(context, "alpha"), FloatArgumentType.getFloat(context, "scale"), IntegerArgumentType.getInteger(context, "delay"), IntegerArgumentType.getInteger(context, "color_delay")))
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )
                                                                        )

                                                                )
                                                        ).then(
                                                                CommandManager.literal("normal").then(
                                                                        CommandManager.argument("time", IntegerArgumentType.integer(0)).then(
                                                                                CommandManager.argument("alpha", FloatArgumentType.floatArg()).then(
                                                                                        CommandManager.argument("scale", FloatArgumentType.floatArg()).then(
                                                                                                CommandManager.argument("viewers", EntityArgumentType.players()).then(
                                                                                                        CommandManager.argument("delay", IntegerArgumentType.integer(0)).then(
                                                                                                                CommandManager.argument("color_delay", IntegerArgumentType.integer(0))
                                                                                                                .executes(context -> execute(context.getSource(), ParticleEffectArgumentType.getParticle(context, "name"), Vec3ArgumentType.getVec3(context, "color"), Vec3d.ZERO, Vec3ArgumentType.getVec3(context, "pos"), Vec3ArgumentType.getVec3(context, "delta"), FloatArgumentType.getFloat(context, "speed"), IntegerArgumentType.getInteger(context, "count"), false, EntityArgumentType.getPlayers(context, "viewers"), IntegerArgumentType.getInteger(context, "time"), FloatArgumentType.getFloat(context, "alpha"), FloatArgumentType.getFloat(context, "scale"), IntegerArgumentType.getInteger(context, "delay"), IntegerArgumentType.getInteger(context, "color_delay")))
                                                                                                        )
                                                                                                )
                                                                                        )

                                                                                )
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        ).then(
                                CommandManager.argument("target", Vec3ArgumentType.vec3(false)).then(
                                        CommandManager.argument("pos", Vec3ArgumentType.vec3()).then(
                                                CommandManager.argument("delta", Vec3ArgumentType.vec3(false)).then(
                                                        CommandManager.argument("speed", FloatArgumentType.floatArg(0F)).then(
                                                                CommandManager.argument("count", IntegerArgumentType.integer(0)).then(
                                                                        CommandManager.literal("force").then(
                                                                                CommandManager.argument("time", IntegerArgumentType.integer(0)).then(
                                                                                        CommandManager.argument("alpha", FloatArgumentType.floatArg()).then(
                                                                                                CommandManager.argument("scale", FloatArgumentType.floatArg()).then(
                                                                                                        CommandManager.argument("viewers", EntityArgumentType.players()).then(
                                                                                                                CommandManager.argument("delay", IntegerArgumentType.integer(0)).then(
                                                                                                                        CommandManager.argument("color_delay", IntegerArgumentType.integer(0))
                                                                                                                        .executes(context -> execute(context.getSource(), ParticleEffectArgumentType.getParticle(context, "name"), Vec3ArgumentType.getVec3(context, "color"), Vec3ArgumentType.getVec3(context, "target"), Vec3ArgumentType.getVec3(context, "pos"), Vec3ArgumentType.getVec3(context, "delta"), FloatArgumentType.getFloat(context, "speed"), IntegerArgumentType.getInteger(context, "count"), true, EntityArgumentType.getPlayers(context, "viewers"), IntegerArgumentType.getInteger(context, "time"), FloatArgumentType.getFloat(context, "alpha"), FloatArgumentType.getFloat(context, "scale"), IntegerArgumentType.getInteger(context, "delay"), IntegerArgumentType.getInteger(context, "color_delay")))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )

                                                                        )
                                                                ).then(
                                                                        CommandManager.literal("normal").then(
                                                                                CommandManager.argument("time", IntegerArgumentType.integer(0)).then(
                                                                                        CommandManager.argument("alpha", FloatArgumentType.floatArg()).then(
                                                                                                CommandManager.argument("scale", FloatArgumentType.floatArg()).then(
                                                                                                        CommandManager.argument("viewers", EntityArgumentType.players()).then(
                                                                                                                CommandManager.argument("delay", IntegerArgumentType.integer(0)).then(
                                                                                                                        CommandManager.argument("color_delay", IntegerArgumentType.integer(0))
                                                                                                                                .executes(context -> execute(context.getSource(), ParticleEffectArgumentType.getParticle(context, "name"), Vec3ArgumentType.getVec3(context, "color"), Vec3ArgumentType.getVec3(context, "target"), Vec3ArgumentType.getVec3(context, "pos"), Vec3ArgumentType.getVec3(context, "delta"), FloatArgumentType.getFloat(context, "speed"), IntegerArgumentType.getInteger(context, "count"), false, EntityArgumentType.getPlayers(context, "viewers"), IntegerArgumentType.getInteger(context, "time"), FloatArgumentType.getFloat(context, "alpha"), FloatArgumentType.getFloat(context, "scale"), IntegerArgumentType.getInteger(context, "delay"), IntegerArgumentType.getInteger(context, "color_delay")))
                                                                                                                )
                                                                                                        )
                                                                                                )
                                                                                        )
                                                                                )

                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                )
        ));

    }

    public static int execute(ServerCommandSource source, ParticleEffect particleEffect , Vec3d color, Vec3d target, Vec3d pos, Vec3d delta, float speed, int count, boolean force, Collection<ServerPlayerEntity> viewers, int age, float alpha, float scale, int delay, int color_delay){
        if (!Objects.equals(Objects.requireNonNull(Registries.PARTICLE_TYPE.getId(particleEffect.getType())).getNamespace(), ParticleX.REGISTER_NAME)){
            source.sendMessage(Text.of ("请使用ParticleX自定义粒子而不是原版粒子"));
            return 0;
        }
        CustomParticlePacket packet = new CustomParticlePacket(particleEffect, (float) color.getX(), (float) color.getY(), (float) color.getZ(), (float) target.getX(), (float) target.getY(), (float) target.getZ(), pos.x, pos.y, pos.z, delta.x, delta.y, delta.z, speed, count, force, age, alpha, scale, delay, color_delay);
        for (ServerPlayerEntity player: viewers){
            packet.send(player, new Identifier(ParticleX.REGISTER_NAME, "particle"));
        }
        return 0;
    }
}
