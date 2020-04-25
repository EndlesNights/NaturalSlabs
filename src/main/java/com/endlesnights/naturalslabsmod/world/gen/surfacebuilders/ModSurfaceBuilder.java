package com.endlesnights.naturalslabsmod.world.gen.surfacebuilders;

import java.util.Random;
import java.util.function.Function;

import com.mojang.datafixers.Dynamic;

import net.minecraft.block.BlockState;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public abstract class ModSurfaceBuilder <C extends ISurfaceBuilderConfig> extends net.minecraftforge.registries.ForgeRegistryEntry<ModSurfaceBuilder<?>>
{
	private final Function<Dynamic<?>, ? extends C> field_215408_a;
	public static final SurfaceBuilder<SurfaceBuilderConfig> DEFAULT_SLAB = register("default_slab", new SlabDefaultSurfaceBuilder(SurfaceBuilderConfig::deserialize));
	
	private static <C extends ISurfaceBuilderConfig, F extends SurfaceBuilder<C>> F register(String key, F builderIn)
	{
		return (F)(Registry.<SurfaceBuilder<?>>register(Registry.SURFACE_BUILDER, key, builderIn));
	}

	public ModSurfaceBuilder(Function<Dynamic<?>, ? extends C> p_i51305_1_)
	{
		      this.field_215408_a = p_i51305_1_;
	}

	public abstract void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, C config);

	public void setSeed(long seed) {
		   }
}
