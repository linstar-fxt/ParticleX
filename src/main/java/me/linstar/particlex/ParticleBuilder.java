package me.linstar.particlex;

import me.linstar.particlex.until.CustomParticleManager;
import me.linstar.particlex.until.ParticleModify;
import net.minecraft.client.particle.ParticleTextureSheet;


//粒子构建器，Pipeline管线包装
public class ParticleBuilder {

    private final String register_name;
    private final ParticleModify modify = new ParticleModify();
    public ParticleBuilder(String register_name){
        this.register_name = register_name;
    }

    public static ParticleBuilder build(String register_name){
        return new ParticleBuilder(register_name);
    }

    public ParticleBuilder change_color(boolean change){
        modify.CHANGE_COLOR = change;
        return this;
    }

    public ParticleBuilder render_type(ParticleTextureSheet type){
        modify.RENDER_TYPE = type;
        return this;
    }

    public void register(){
        CustomParticleManager.getINSTANCE().add(register_name, modify);
    }
}
