package com.endlesnights.naturalslabsmod.world.gen.surfacebuilders;

import java.util.Random;
import java.util.function.Function;

import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.mojang.datafixers.Dynamic;

import net.minecraft.block.AirBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.GrassBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class SlabDefaultSurfaceBuilder extends SurfaceBuilder<SurfaceBuilderConfig>
{
	public SlabDefaultSurfaceBuilder(Function<Dynamic<?>, ? extends SurfaceBuilderConfig> p_i51315_1_)
	{
		super(p_i51315_1_);
	}

	public void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight, double noise, BlockState defaultBlock, BlockState defaultFluid, int seaLevel, long seed, SurfaceBuilderConfig config) {
		this.buildSurface(random, chunkIn, biomeIn, x, z, startHeight, noise, defaultBlock, defaultFluid, config.getTop(), config.getUnder(), config.getUnderWaterMaterial(), seaLevel);
	}

	protected void buildSurface(Random random, IChunk chunkIn, Biome biomeIn, int x, int z, int startHeight,
			double noise, BlockState defaultBlock, BlockState defaultFluid, BlockState top, BlockState middle, BlockState bottom, int sealevel)
	{
		BlockState blockstate = top;
		BlockState blockstate1 = middle;
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();
		int i = -1;
		double j = (noise / 3.0D + 3.0D + random.nextDouble() * 0.25D);
		int k = x & 15;
		int l = z & 15;

		for(int i1 = startHeight; i1 >= 0; --i1)
		{
			blockpos$mutable.setPos(k, i1 , l);
			BlockState blockstate2 = chunkIn.getBlockState(blockpos$mutable);
			
			if (blockstate2.isAir())
			{
				i = -1;
			}
			else if (blockstate2.getBlock() == defaultBlock.getBlock())
			{
				if (i == -1)
				{
					if (j <= 0)
					{
						blockstate = Blocks.AIR.getDefaultState();
						blockstate1 = defaultBlock;
					}
					else if (i1 >= sealevel - 4 && i1 <= sealevel + 1)
					{
						blockstate = top;
						blockstate1 = middle;
					}
	
					if (i1 < sealevel && (blockstate == null || blockstate.isAir()))
					{
//						if (biomeIn.func_225486_c(blockpos$mutable.setPos(x, i1, z)) < 0.15F)
//						{
//							blockstate = Blocks.ICE.getDefaultState();
//						}
//						else
//						{
//							blockstate = defaultFluid;
//						}
	
						blockpos$mutable.setPos(k, i1, l);
					}
	
					i = (int)j;
					if (i1 >= sealevel - 1)
					{
						
						if(j%1 > 0.7)
						{
							chunkIn.setBlockState(new BlockPos(k, i1, l), ModBlocks.block_grass_slab.getDefaultState(), false);
							//chunkIn.setBlockState(blockpos$mutable, Blocks.DIRT.getDefaultState(), false);
						}
						else
						{
							chunkIn.setBlockState(blockpos$mutable, blockstate, false);
						}
						
//						if(chunkIn.getBlockState(new BlockPos(k+1 & 15, i1+1, l)).getBlock() instanceof GrassBlock
//								|| chunkIn.getBlockState(new BlockPos(k-1 & 15, i1+1, l)).getBlock() instanceof GrassBlock
//								|| chunkIn.getBlockState(new BlockPos(k, i1+1, l+1 & 15)).getBlock() instanceof GrassBlock
//								|| chunkIn.getBlockState(new BlockPos(k, i1+1, l-1 & 15)).getBlock() instanceof GrassBlock
//								
//								|| chunkIn.getBlockState(new BlockPos(k+1 & 15, i1+1, l)) == Blocks.DIRT.getDefaultState() 
//								|| chunkIn.getBlockState(new BlockPos(k-1 & 15, i1+1, l)) == Blocks.DIRT.getDefaultState()
//								|| chunkIn.getBlockState(new BlockPos(k, i1+1, l+1 & 15)) == Blocks.DIRT.getDefaultState()
//								|| chunkIn.getBlockState(new BlockPos(k, i1+1, l-1 & 15)) == Blocks.DIRT.getDefaultState())
//						{
//							chunkIn.setBlockState(new BlockPos(k, i1+1, l), ModBlocks.block_grass_slab.getDefaultState(), false);
//							chunkIn.setBlockState(blockpos$mutable, Blocks.DIRT.getDefaultState(), false);
//						}
//						else
//						{
//							chunkIn.setBlockState(blockpos$mutable, blockstate, false);
//						}
						
//						chunkIn.setBlockState(blockpos$mutable, blockstate, false);
						
					}
					else if (i1 < sealevel - 7 - j)
					{
						blockstate = Blocks.AIR.getDefaultState();
						blockstate1 = defaultBlock;
						chunkIn.setBlockState(blockpos$mutable, bottom, false);
					}
					else
					{
						chunkIn.setBlockState(blockpos$mutable, blockstate1, false);
					}
				}
				else if (i > 0)
				{
					--i;
					chunkIn.setBlockState(blockpos$mutable, blockstate1, false);
					if (i == 0 && blockstate1.getBlock() == Blocks.SAND && j > 1)
					{
						i = random.nextInt(4) + Math.max(0, i1 - 63);
						blockstate1 = blockstate1.getBlock() == Blocks.RED_SAND ? Blocks.RED_SANDSTONE.getDefaultState() : Blocks.SANDSTONE.getDefaultState();
					}
				}
			}
		}

	}
}
