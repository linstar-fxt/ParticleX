package me.linstar.particlex.client;

import me.linstar.particlex.ParticleX;
import me.linstar.particlex.command.ParticleLimitCommand;
import me.linstar.particlex.mixin.ParticleManagerAccessor;
import me.linstar.particlex.until.CustomParticleManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

@Environment(EnvType.CLIENT)
public class ParticleXClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CustomParticleManager.getINSTANCE().client_register(); //注册所有自定义粒子
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(ParticleX.REGISTER_NAME, "particle"), new Handler()); //注册自定义粒子数据包监听
        ClientPlayNetworking.registerGlobalReceiver(new Identifier(ParticleX.REGISTER_NAME, "clean"), new CleanParticleHandler());
        ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> ParticleLimitCommand.register(dispatcher)));//注册粒子上限客户端命令
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
