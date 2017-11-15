package com.ckdk.wemo;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@Mod(modid = LightMod.MODID, version = LightMod.VERSION)
public class LightMod
{
    public static final String MODID = "lightmod";
    public static final String VERSION = "1.0";

    @Mod.EventHandler
    public void init(FMLInitializationEvent event)
    {

    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        // register server commands

        event.registerServerCommand(new LightICommand());
    }
}
