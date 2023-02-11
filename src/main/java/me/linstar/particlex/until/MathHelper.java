package me.linstar.particlex.until;

import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.List;

public class MathHelper {



    protected MathHelper(String function){

    }

    public static MathHelper build(String function){
        return new MathHelper(function);
    }
}
