package me.linstar.particlex.client;

import me.linstar.particlex.Particlex;
import me.linstar.particlex.command.ParticleLimitCommand;
import me.linstar.particlex.mixin.ParticleManagerAccessor;
import me.linstar.particlex.particle.BasicParticleEffect;
import me.linstar.particlex.until.CustomParticleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.Map;
import java.util.Queue;

@Environment(EnvType.CLIENT)
public class ParticlexClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomParticleManager.getINSTANCE().client_register(); //注册所有自定义粒子
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(Particlex.REGISTER_NAME, "particle"), new Handler()); //注册自定义粒子数据包监听
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(Particlex.REGISTER_NAME, "clean"), new CleanParticleHandler());
        ParticleLimitCommand.register();  //注册粒子上限客户端命令
    }

    private static class Handler implements ClientPlayNetworking.PlayChannelHandler{

        @Override
        public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            CustomParticlePacket packet = new CustomParticlePacket(buf);
            packet.create(client);
        }
    }

    private static class CleanParticleHandler implements ClientPlayNetworking.PlayChannelHandler{

        @Override
        public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            ((ParticleManagerAccessor)client.particleManager).getParticles().clear();
            ((ParticleManagerAccessor)client.particleManager).getNewParticles().clear();
        }
    }
}
