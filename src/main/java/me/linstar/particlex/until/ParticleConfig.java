package me.linstar.particlex.until;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.*;
import java.nio.file.Path;

//写的非常丑的Java IO
public class ParticleConfig {
    private static final String CONFIG_FILE_NAME = "particle_limit.json";
    private static final Path CONFIG_FILE_PATH = FabricLoader.getInstance().getConfigDir().resolve(CONFIG_FILE_NAME);
    private static final int DEFAULT_LIMIT = 16384;
    private static final ParticleConfig INSTANCE = new ParticleConfig();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public int value = 16384;

    public ParticleConfig()  {
        File file = CONFIG_FILE_PATH.toFile();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file));){
            String str = bufferedReader.readLine();
            if (str == null){
                set(DEFAULT_LIMIT);
                return;
            }
            get();
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void get(){
        File file = CONFIG_FILE_PATH.toFile();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file));){
            Data data = GSON.fromJson(bufferedReader.readLine(), Data.class);
            value = data.getLimit();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void set(int limit){
        this.value = limit;
        File file = CONFIG_FILE_PATH.toFile();
        Data data = new Data();
        data.setLimit(limit);
        try(FileWriter writer = new FileWriter(file)){
            writer.write(GSON.toJson(data).replace("\n", ""));
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static ParticleConfig getInstance(){
        return INSTANCE;
    }
    private static class Data{
        private int limit;

        public void setLimit(int limit){
            this.limit = limit;
        }

        public int getLimit(){
            return this.limit;
        }
    }
}
