package me.linstar.particlex.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.serialization.Codec;
import me.linstar.particlex.client.CustomParticlePacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;


public class BasicParticleEffect extends ParticleType<BasicParticleEffect> implements ParticleEffect {
    //因为客户端能拿到的实例只有Effect,所以自定义参数在Effect类中定义
    CustomParticlePacket packet;
    //Factory获取，Mojang的实现就是依托答辩，弃用了but it works
    private static final ParticleEffect.Factory<BasicParticleEffect> PARAMETER_FACTORY = new ParticleEffect.Factory<>() {
        public BasicParticleEffect read(ParticleType<BasicParticleEffect> particleType, StringReader stringReader) {
            return (BasicParticleEffect) particleType;
        }

        public BasicParticleEffect read(ParticleType<BasicParticleEffect> particleType, PacketByteBuf packetByteBuf) {
            return (BasicParticleEffect) particleType;
        }
    };
    //压缩器获取，同样是依托答辩
    private final Codec<BasicParticleEffect> codec = Codec.unit(this::getType);
    //构建是否启用force
    protected BasicParticleEffect(boolean alwaysShow) {
        super(alwaysShow, PARAMETER_FACTORY);
    }

    public BasicParticleEffect getType() {
        return this;
    }

    public Codec<BasicParticleEffect> getCodec() {
        return this.codec;
    }

    public void write(PacketByteBuf buf) {
    }

    public String asString() {
        return Registries.PARTICLE_TYPE.getId(this).toString();
    }

    public void set_info(CustomParticlePacket packet){
        this.packet = packet;
    }

    public CustomParticlePacket get_info(){
        return packet;
    }
}