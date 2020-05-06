package com.endlesnights.naturalslabsmod.data;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = NaturalSlabsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(NaturalSlabsMod.MODID)
public class ModDataGenerator
{
    @SubscribeEvent
    public static void gatherData(GatherDataEvent evt)
    {
    	System.out.println("TEST ENTER HERE?");
        if (evt.includeServer()) {
        	evt.getGenerator().addProvider(new BlockTagProvider(evt.getGenerator()));
        }
    }
}
