package me.linstar.particlex.until;


import java.util.ArrayList;
import java.util.List;

public class MathHelper {



    protected MathHelper(String function){

    }

    public static MathHelper build(String function){
        return new MathHelper(function);
    }
}
