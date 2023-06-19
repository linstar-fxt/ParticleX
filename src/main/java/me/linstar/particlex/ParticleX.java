package me.linstar.particlex;

import me.linstar.particlex.command.ParticleCleanCommand;
import me.linstar.particlex.command.ParticleXCommand;
import me.linstar.particlex.until.CustomParticleManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.client.particle.ParticleTextureSheet;

public class ParticleX implements ModInitializer {

    public static final String REGISTER_NAME = "particlex";

    @Override
    public void onInitialize() {
        register_particle();
        CustomParticleManager.getINSTANCE().server_register();  //服务端注册粒子
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> ParticleXCommand.register(dispatcher, registryAccess))); //粒子命令注册
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> ParticleCleanCommand.register(dispatcher)));
    }

    //所有自定义粒子在此定义
    public void register_particle(){
        ParticleBuilder.build("basic").render_type(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT).change_color(true).register();
        ParticleBuilder.build("hello").render_type(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT).change_color(false).light(15).register();
        ParticleBuilder.build("block").render_type(ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT).change_color(true).block_render(true).register();
    }
}
