package me.linstar.particlex.particle;

import me.linstar.particlex.client.CustomParticlePacket;
import me.linstar.particlex.until.CustomParticleManager;
import me.linstar.particlex.until.ParticleModify;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

public class BasicParticle extends SpriteBillboardParticle {

    SpriteProvider provider;

    int animate = 0;

    float target_red;
    float target_green;
    float target_blue;

    ParticleModify modify;

    protected BasicParticle(ClientWorld clientWorld, double x, double y, double z, double dx, double dy, double dz, SpriteProvider spriteProvider, BasicParticleEffect effect) {
        super(clientWorld, x, y , z);

        CustomParticlePacket packet = effect.get_info();
        CustomParticleManager manager = CustomParticleManager.getINSTANCE();

        this.x = x;
        this.y = y;
        this.z = z;

        this.velocityX = dx;
        this.velocityY = dy;
        this.velocityZ = dz;

        this.red = packet.red;
        this.green = packet.green;
        this.blue = packet.blue;

        this.target_red = packet.target_red;
        this.target_green = packet.target_green;
        this.target_blue = packet.target_blue;

        System.out.println("color" + target_red + " " + target_green + " " + target_blue);

        this.maxAge = packet.age;

        System.out.println(Registry.PARTICLE_TYPE.getId(Registry.PARTICLE_TYPE.get(packet.particle_id)).getPath());
        this.modify = manager.get_modify(Registry.PARTICLE_TYPE.getId(Registry.PARTICLE_TYPE.get(packet.particle_id)).getPath());
        this.provider = spriteProvider;
        setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;                //坐标更新
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.age++ >= this.maxAge) {       //生命判断
            this.markDead();
        } else {
            animate();                         //自定义动画更新
        }
        this.setSpriteForAge(provider);  //纹理动画更新
    }

    public void animate(){

        if (this.age > this.maxAge / 2) {
            this.setAlpha(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
            if (modify.CHANGE_COLOR) {
                this.red += (this.target_red - this.red) * 0.2F;
                this.green += (this.target_green - this.green) * 0.2F;
                this.blue += (this.target_blue - this.blue) * 0.2F;
            }
        }
        double dx = this.velocityX / this.velocityMultiplier / 20;
        double dy = this.velocityY / this.velocityMultiplier / 20;
        double dz = this.velocityZ / this.velocityMultiplier / 20;

        this.animate += 1;  //判断是否到达
        if (dx * animate >= this.velocityX && dy * animate >= this.velocityY && dz * animate >= this.velocityZ){
            return;
        }
        this.x += dx;
        this.y += dy;
        this.z += dz;

    }

    @Override
    public ParticleTextureSheet getType() {
        return modify.RENDER_TYPE;
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<BasicParticleEffect> {
        //Mojang搓的答辩，用来构造Particle实例, 只需要根据需求改动createParticle方法
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(BasicParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new BasicParticle(world, x, y, z, velocityX, velocityY, velocityZ, spriteProvider, parameters);
        }
    }
}
