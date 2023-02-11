package me.linstar.particlex.client;

import me.linstar.particlex.particle.BasicParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class CustomParticlePacket {

    public int particle_id;

    public float red;
    public float green;
    public float blue;

    public float target_red;
    public float target_green;
    public float target_blue;

    double x;
    double y;
    double z;
    double dx;
    double dy;
    double dz;

    public float speed;
    int count;
    boolean force;
    public int age;

    public float alpha;

    public float scale;
    public int delay;
    public int color;

    public CustomParticlePacket(PacketByteBuf buf){
        particle_id = buf.readInt();

        red = buf.readFloat();
        green = buf.readFloat();
        blue = buf.readFloat();

        target_red = buf.readFloat();
        target_green = buf.readFloat();
        target_blue = buf.readFloat();

        x = buf.readDouble();
        y = buf.readDouble();
        z = buf.readDouble();
        dx = buf.readDouble();
        dy = buf.readDouble();
        dz = buf.readDouble();

        speed = buf.readFloat();
        count = buf.readInt();
        force = buf.readBoolean();
        age = buf.readInt();
        alpha = buf.readFloat();
        scale = buf.readFloat();
        delay = buf.readInt();
        color = buf.readInt();
    }

    public CustomParticlePacket(ParticleEffect effect, float red, float green, float blue, float target_red, float target_green, float target_blue, double x, double y, double z, double dx, double dy, double dz, float speed, int count, boolean force, int age, float alpha, float scale, int delay, int color){
        particle_id = Registry.PARTICLE_TYPE.getRawId(effect.getType());

        this.red = red;
        this.green = green;
        this.blue = blue;

        this.target_red = target_red;
        this.target_green = target_green;
        this.target_blue = target_blue;

        this.x = x;
        this.y = y;
        this.z = z;

        this.dx = dx;
        this.dy = dy;
        this.dz = dz;

        this.speed = speed;
        this.count = count;
        this.force = force;

        this.age = age;
        this.alpha = alpha;
        this.scale = scale;

        this.delay = delay;
        this.color = color;
    }

    public void send(ServerPlayerEntity player, Identifier identifier){
        PacketByteBuf buf = PacketByteBufs.create();

        buf.writeInt(particle_id);

        buf.writeFloat(red);
        buf.writeFloat(green);
        buf.writeFloat(blue);

        buf.writeFloat(target_red);
        buf.writeFloat(target_green);
        buf.writeFloat(target_blue);

        buf.writeDouble(x);
        buf.writeDouble(y);
        buf.writeDouble(z);

        buf.writeDouble(dx);
        buf.writeDouble(dy);
        buf.writeDouble(dz);

        buf.writeFloat(speed);
        buf.writeInt(count);
        buf.writeBoolean(force);
        buf.writeInt(age);
        buf.writeFloat(alpha);
        buf.writeFloat(scale);

        buf.writeInt(delay);
        buf.writeInt(color);

        ServerPlayNetworking.send(player, identifier, buf);
    }

    @Environment(EnvType.CLIENT)
    public void create(MinecraftClient client){
        BasicParticleEffect effect = (BasicParticleEffect) Registry.PARTICLE_TYPE.get(particle_id);
        assert effect != null;
        assert client.world != null;
        effect.set_info(this);

        for (int i = 0; i < count; i++) {
            client.world.addParticle(effect, force, x, y, z, dx, dy, dz);
        }
    }
}
