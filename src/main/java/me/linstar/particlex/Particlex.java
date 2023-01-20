package me.linstar.particlex;

import me.linstar.particlex.command.ParticleXCommand;
import me.linstar.particlex.until.CustomParticleManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.particle.ParticleTextureSheet;

public class Particlex implements ModInitializer {

    public static final String REGISTER_NAME = "particlex";

    @Override
    public void onInitialize() {
        register_particle();
        CustomParticleManager.getINSTANCE().server_register();  //服务端注册粒子
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> ParticleXCommand.register(dispatcher))); //粒子命令注册
    }

    //所有自定义粒子在此定义
    public void register_particle(){
        ParticleBuilder.build("basic").render_type(ParticleTextureSheet.PARTICLE_SHEET_LIT).change_color(true).register();
        ParticleBuilder.build("hello").render_type(ParticleTextureSheet.PARTICLE_SHEET_OPAQUE).change_color(false).register();
    }
}
