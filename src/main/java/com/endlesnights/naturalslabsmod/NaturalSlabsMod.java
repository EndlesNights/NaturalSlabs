package com.endlesnights.naturalslabsmod;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import com.endlesnights.naturalslabsmod.config.Config;
import com.endlesnights.naturalslabsmod.events.FallEvent;
import com.endlesnights.naturalslabsmod.init.ModBiomes;
import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.world.biome.SlabPlainsBiome;

import net.minecraft.block.Block;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod(NaturalSlabsMod.MODID)
@EventBusSubscriber(bus=Bus.MOD)
public class NaturalSlabsMod 
{
	public static final String MODID = "naturalslabsmod";
	public static final String NAME = "Natural Slabs Mod";
	
	private static List<Supplier<INaturalSlabsCompat>> compatList = new ArrayList<>();
	
	public static NaturalSlabsMod instance;
	public final ItemGroup creativeTab;
	
	public NaturalSlabsMod()
	{
		instance = this;		
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, Config.SERVER, "naturalslabsmod-server.toml");
		Config.loadConfig(Config.SERVER, FMLPaths.CONFIGDIR.get().resolve("naturalslabsmod-server.toml").toString());
		
		creativeTab = new CreativeTab();
		compatList.add(ModBlocks::new);
		//compatList.add(ModBlocksCompat::new);
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(this::loadComplete);
		MinecraftForge.EVENT_BUS.register(new WorldTickHandler());
		
	}
	
	private void loadComplete(FMLLoadCompleteEvent event)
	{
		MinecraftForge.EVENT_BUS.register((Object)new FallEvent());
	}
	
	@SubscribeEvent
	public static void onRegisterBiome(RegistryEvent.Register<Biome> event)
	{
//		registerBiome(event, new SlabPlainsBiome(), "plains_slab", 40, BiomeType.WARM, Type.PLAINS, Type.CONIFEROUS, Type.FOREST, Type.HILLS, Type.OVERWORLD);
	}
	
//	private static void registerBiome(RegistryEvent.Register<Biome> event, Biome biome, String registryName, int spawnWeight, BiomeType spawnType, Type... types)
	{
//		event.getRegistry().register(biome.setRegistryName(new ResourceLocation(MODID, registryName)));
//		BiomeDictionary.addTypes(biome, types);
//		BiomeManager.addBiome(spawnType, new BiomeEntry(biome, spawnWeight));
	}
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		for(Supplier<INaturalSlabsCompat> compat : compatList)
		{
			compat.get().registerBlocks(event);
		}
	}

	@SubscribeEvent
	public static void onInterModEnqueue(InterModEnqueueEvent event)
	{
		for(Supplier<INaturalSlabsCompat> compat : compatList)
		{
			compat.get().registerPlaceEntries();
		}
	}
	
	//ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, GrassConfigHandler.COMMON_SPEC);
	public ItemGroup getTab()
	{
		return creativeTab;
	}
    
	private class CreativeTab extends ItemGroup
	{
		public CreativeTab()
    	{
			super(NaturalSlabsMod.MODID);
    	}

		public ItemStack createIcon()
		{
			return new ItemStack(ModBlocks.block_grass_slab);
		}
	};
}
