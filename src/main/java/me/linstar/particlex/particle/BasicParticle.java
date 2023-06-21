package me.linstar.particlex.particle;

import me.linstar.particlex.client.CustomParticlePacket;
import me.linstar.particlex.until.CustomParticleManager;
import me.linstar.particlex.until.ParticleModify;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BasicParticle extends SpriteBillboardParticle {

    SpriteProvider provider;

    int animate = 0;
    int delay;
    int color_delay;

    float target_red;
    float target_green;
    float target_blue;


    boolean hide;

    int milli_age;

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
        this.velocityMultiplier = packet.speed;

        this.alpha = packet.alpha;
        this.maxAge = packet.age;
        this.modify = manager.get_modify(Registry.PARTICLE_TYPE.getId(Registry.PARTICLE_TYPE.get(packet.particle_id)).getPath());
        //this.scale(packet.scale);
        this.scale = packet.scale;

        this.delay = packet.delay;

        this.color_delay = packet.color;

        this.provider = spriteProvider;

        this.hide = delay != 0;


        setSpriteForAge(spriteProvider);
    }

    @Override
    public void tick() {
        if (hide || this.dead){
            return;
        }

        this.prevPosX = this.x;                //坐标更新
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        animate();                         //自定义动画更新

        if (modify.BLOCK_RENDER){
            return;
        }
        this.setSpriteForAge(provider);  //纹理动画更新
    }

    public void milli_tick(){
        if (dead){
            return;
        }
        milli_age ++;
        if (milli_age == delay){
            hide = false;
        }
        if (milli_age == delay + maxAge){
            dead = true;
        }
    }

    public void animate(){

        if (this.age > this.color_delay) {
            //this.setAlpha(1.0F - ((float)this.age - (float)(this.maxAge / 2)) / (float)this.maxAge);
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
    public void buildGeometry(VertexConsumer vertexConsumer, Camera camera, float tickDelta) {

        if (this.hide || this.dead){
            return;
        }

        if (!modify.BLOCK_RENDER){
            super.buildGeometry(vertexConsumer, camera, tickDelta);
            return;
        }

        Vec3d vec3d = camera.getPos();
        float f = (float)(MathHelper.lerp(tickDelta, this.prevPosX, this.x) - vec3d.getX());
        float g = (float)(MathHelper.lerp(tickDelta, this.prevPosY, this.y) - vec3d.getY());
        float h = (float)(MathHelper.lerp(tickDelta, this.prevPosZ, this.z) - vec3d.getZ());

        Vec3f[] front = new Vec3f[]{
                new Vec3f(-1.0F, -1.0F, -1.0F), //前
                new Vec3f(-1.0F, 1.0F, -1.0F),
                new Vec3f(1.0F, 1.0F, -1.0F),
                new Vec3f(1.0F, -1.0F, -1.0F),

                new Vec3f(-1.0F, 1.0F, -1.0F),
                new Vec3f(-1.0F, -1.0F, -1.0F),
                new Vec3f(1.0F, -1.0F, -1.0F),
                new Vec3f(1.0F, 1.0F, -1.0F),

        };
        Vec3f[] back = new Vec3f[]{
                new Vec3f(-1.0F, 1.0F, 1.0F), //后
                new Vec3f(-1.0F, -1.0F, 1.0F),
                new Vec3f(1.0F, -1.0F, 1.0F),
                new Vec3f(1.0F, 1.0F, 1.0F),

                new Vec3f(-1.0F, -1.0F, 1.0F), //前
                new Vec3f(-1.0F, 1.0F, 1.0F),
                new Vec3f(1.0F, 1.0F, 1.0F),
                new Vec3f(1.0F, -1.0F, 1.0F),
        };

        Vec3f[] right = new Vec3f[]{
                new Vec3f(-1.0F, -1.0F, 1.0F), //左
                new Vec3f(-1.0F, 1.0F, 1.0F),
                new Vec3f(-1.0F, 1.0F, -1.0F),
                new Vec3f(-1.0F, -1.0F, -1.0F),

                new Vec3f(-1.0F, -1.0F, -1.0F), //右
                new Vec3f(-1.0F, 1.0F, -1.0F),
                new Vec3f(-1.0F, 1.0F, 1.0F),
                new Vec3f(-1.0F, -1.0F, 1.0F),
        };

        Vec3f[] left = new Vec3f[]{
                new Vec3f(1.0F, -1.0F, -1.0F), //右
                new Vec3f(1.0F, 1.0F, -1.0F),
                new Vec3f(1.0F, 1.0F, 1.0F),
                new Vec3f(1.0F, -1.0F, 1.0F),

                new Vec3f(1.0F, -1.0F, 1.0F), //左
                new Vec3f(1.0F, 1.0F, 1.0F),
                new Vec3f(1.0F, 1.0F, -1.0F),
                new Vec3f(1.0F, -1.0F, -1.0F),
        };
        Vec3f[] button = new Vec3f[]{
                new Vec3f(-1.0F, -1.0F, 1.0F), //下
                new Vec3f(-1.0F, -1.0F, -1.0F),
                new Vec3f(1.0F, -1.0F, -1.0F),
                new Vec3f(1.0F, -1.0F, 1.0F),

                new Vec3f(-1.0F, -1.0F, -1.0F), //上
                new Vec3f(-1.0F, -1.0F, 1.0F),
                new Vec3f(1.0F, -1.0F, 1.0F),
                new Vec3f(1.0F, -1.0F, -1.0F),
        };
        Vec3f[] top = new Vec3f[]{
                new Vec3f(-1.0F, 1.0F, -1.0F), //上
                new Vec3f(-1.0F, 1.0F, 1.0F),
                new Vec3f(1.0F, 1.0F, 1.0F),
                new Vec3f(1.0F, 1.0F, -1.0F),

                new Vec3f(-1.0F, 1.0F, 1.0F), //下
                new Vec3f(-1.0F, 1.0F, -1.0F),
                new Vec3f(1.0F, 1.0F, -1.0F),
                new Vec3f(1.0F, 1.0F, 1.0F),
        };

        List<Vec3f[]> vec3fs = new ArrayList<>();
        vec3fs.add(front);
        vec3fs.add(back);
        vec3fs.add(left);
        vec3fs.add(right);
        vec3fs.add(top);
        vec3fs.add(button);

        float j = this.getSize(tickDelta);
        int p = this.getBrightness(tickDelta);

        int i = 0;
        for (Vec3f[] face: vec3fs){
            for (Vec3f vec3f: face){
                vec3f.scale(j);
                vec3f.add(f, g, h);
            }
            this.setSprite(provider.getSprite(i, 5));
            float l = this.getMinU();
            float m = this.getMaxU();
            float n = this.getMinV();
            float o = this.getMaxV();
            i++;
            vertexConsumer.vertex((double)face[0].getX(), (double)face[0].getY(), (double)face[0].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
            vertexConsumer.vertex((double)face[1].getX(), (double)face[1].getY(), (double)face[1].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
            vertexConsumer.vertex((double)face[2].getX(), (double)face[2].getY(), (double)face[2].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
            vertexConsumer.vertex((double)face[3].getX(), (double)face[3].getY(), (double)face[3].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
            vertexConsumer.vertex((double)face[4].getX(), (double)face[4].getY(), (double)face[4].getZ()).texture(m, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
            vertexConsumer.vertex((double)face[5].getX(), (double)face[5].getY(), (double)face[5].getZ()).texture(m, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
            vertexConsumer.vertex((double)face[6].getX(), (double)face[6].getY(), (double)face[6].getZ()).texture(l, n).color(this.red, this.green, this.blue, this.alpha).light(p).next();
            vertexConsumer.vertex((double)face[7].getX(), (double)face[7].getY(), (double)face[7].getZ()).texture(l, o).color(this.red, this.green, this.blue, this.alpha).light(p).next();
        }
    }

    @Override
    protected int getBrightness(float tint) {
        return modify.LIGHT == -1 ? super.getBrightness(tint) : 15728880 - 16 * (16 - modify.LIGHT);
    }

    @Override
    public ParticleTextureSheet getType() {
        switch (modify.RENDER_TYPE) {
            case 0 -> {
                return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
            }
            case 1 -> {
                return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
            }
            case 2 -> {
                return ParticleTextureSheet.PARTICLE_SHEET_LIT;
            }
            default -> {
                return ParticleTextureSheet.NO_RENDER;
            }
        }
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
