package me.linstar.particlex.until;

import me.linstar.particlex.Particlex;
import me.linstar.particlex.particle.BasicParticle;
import me.linstar.particlex.particle.BasicParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

//自定义粒子管理器
public class CustomParticleManager {

    private final Map<String, ParticleModify> particle_map = new HashMap<>();
    private final Map<String, BasicParticleEffect> instance_map = new HashMap<>();
    private static final CustomParticleManager INSTANCE = new CustomParticleManager();

    public CustomParticleManager(){}

    public static CustomParticleManager getINSTANCE(){
        return INSTANCE;
    }

    public void add(String name, ParticleModify modify){
        particle_map.put(name, modify);
        instance_map.put(name, new BasicParticleEffect(false){});
    }

    public ParticleModify get_modify(String name){
        if (particle_map.containsKey(name)){
            return particle_map.get(name);
        }
        return null;
    }

    public void server_register(){
        for (String name: particle_map.keySet()){
            Registry.register(Registry.PARTICLE_TYPE, new Identifier(Particlex.REGISTER_NAME, name), instance_map.get(name));
        }
    }

    @Environment(EnvType.CLIENT)
    public void client_register(){
        for(String name: particle_map.keySet()){
            ParticleFactoryRegistry.getInstance().register(instance_map.get(name), BasicParticle.Factory::new);
        }
    }
}
