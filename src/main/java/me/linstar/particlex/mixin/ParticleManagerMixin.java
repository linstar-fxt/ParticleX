package me.linstar.particlex.mixin;

import com.google.common.collect.EvictingQueue;
import me.linstar.particlex.particle.BasicParticle;
import me.linstar.particlex.until.ParticleConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

@Environment(EnvType.CLIENT)
@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Shadow
    @Final
    private Map<ParticleTextureSheet, Queue<Particle>> particles;


    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci){
        for(Queue<Particle> queue: this.particles.values()){
            try {
                EvictingQueue<Particle> evictingQueue = (EvictingQueue<Particle>) queue;
                Field nameField = evictingQueue.getClass().getDeclaredField("maxSize");
                nameField.setAccessible(true);
                nameField.set(evictingQueue, ParticleConfig.getInstance().value);
            }catch (Exception e){
                e.printStackTrace(); //debug
            }
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(CallbackInfo ci){

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (Queue<Particle> queue : particles.values()) {
                    for (Particle particle : queue) {
                        if (particle instanceof BasicParticle) {
                            ((BasicParticle) particle).milli_tick();
                        }
                    }
                }
            }
        };

        Timer timer = new Timer("ParticleX");
        timer.schedule(task, 0, 10);
    }
}
