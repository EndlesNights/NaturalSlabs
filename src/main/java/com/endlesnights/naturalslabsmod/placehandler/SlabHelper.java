package com.endlesnights.naturalslabsmod.placehandler;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class SlabHelper
{
	public static void slabCheck(IWorld worldIn, BlockPos currentPos)
	{
		BlockState stateUp = worldIn.getBlockState(currentPos.up());
		
		if(slabBottomFoliage(stateUp) != stateUp)
			worldIn.setBlockState(currentPos.up(), slabBottomFoliage(stateUp), 0);
		else if(slabBottomDoubleFoliage(stateUp) != stateUp)
		{
			worldIn.setBlockState(currentPos.up(), ModBlocks.fern_slab.getDefaultState(), 0);
						
			worldIn.setBlockState(currentPos.up(), slabBottomDoubleFoliage(stateUp).with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER), 0);
			worldIn.setBlockState(currentPos.up().up(), slabBottomDoubleFoliage(stateUp).with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER), 0);
		}
	}
	public static BlockState slabBottomFoliage(BlockState state)
	{
		if(state.getBlock() == Blocks.GRASS)
			return ModBlocks.grass_slab.getDefaultState();
		if(state.getBlock() == Blocks.FERN)
			return ModBlocks.fern_slab.getDefaultState();
		
		if(state.getBlock() == Blocks.DANDELION)
			return ModBlocks.dandelion_slab.getDefaultState();
		if(state.getBlock() == Blocks.POPPY)
			return ModBlocks.poppy_slab.getDefaultState();
		if(state.getBlock() == Blocks.BLUE_ORCHID)
			return ModBlocks.blue_orchid_slab.getDefaultState();
		if(state.getBlock() == Blocks.ALLIUM)
			return ModBlocks.allium_slab.getDefaultState();
		if(state.getBlock() == Blocks.AZURE_BLUET)
			return ModBlocks.azure_bluet_slab.getDefaultState();
		if(state.getBlock() == Blocks.RED_TULIP)
			return ModBlocks.red_tulip_slab.getDefaultState();
		if(state.getBlock() == Blocks.ORANGE_TULIP)
			return ModBlocks.orange_tulip_slab.getDefaultState();
		if(state.getBlock() == Blocks.WHITE_TULIP)
			return ModBlocks.white_tulip_slab.getDefaultState();
		if(state.getBlock() == Blocks.PINK_TULIP)
			return ModBlocks.pink_tulip_slab.getDefaultState();
		if(state.getBlock() == Blocks.OXEYE_DAISY)
			return ModBlocks.oxeye_daisy_slab.getDefaultState();
		if(state.getBlock() == Blocks.CORNFLOWER)
			return ModBlocks.cornflower_slab.getDefaultState();
		if(state.getBlock() == Blocks.LILY_OF_THE_VALLEY)
			return ModBlocks.lily_of_the_valley_slab.getDefaultState();
		if(state.getBlock() == Blocks.WITHER_ROSE)
			return ModBlocks.wither_rose_slab.getDefaultState();
		
		if(state.getBlock() == Blocks.RED_MUSHROOM)
			return ModBlocks.red_mushroom_slab.getDefaultState();
		if(state.getBlock() == Blocks.BROWN_MUSHROOM)
			return ModBlocks.brown_mushroom_slab.getDefaultState();
		
		if(state.getBlock() == Blocks.WHEAT)
			return ModBlocks.wheat_slab.getDefaultState();
		if(state.getBlock() == Blocks.POTATOES)
			return ModBlocks.potatoes_slab.getDefaultState();
		if(state.getBlock() == Blocks.CARROTS)
			return ModBlocks.carrots_slab.getDefaultState();
		if(state.getBlock() == Blocks.BEETROOTS)
			return ModBlocks.beetroots_slab.getDefaultState();
	
		if(state.getBlock() == Blocks.OAK_SAPLING)
			return ModBlocks.oak_sapling_slab.getDefaultState();	
		if(state.getBlock() == Blocks.SPRUCE_SAPLING)
			return ModBlocks.spruce_sapling_slab.getDefaultState();	
		if(state.getBlock() == Blocks.BIRCH_SAPLING)
			return ModBlocks.birch_sapling_slab.getDefaultState();	
		if(state.getBlock() == Blocks.JUNGLE_SAPLING)
			return ModBlocks.jungle_sapling_slab.getDefaultState();	
		if(state.getBlock() == Blocks.ACACIA_SAPLING)
			return ModBlocks.acacia_sapling_slab.getDefaultState();	
		if(state.getBlock() == Blocks.DARK_OAK_SAPLING)
			return ModBlocks.dark_oak_sapling_slab.getDefaultState();	
		
		return state;
	}
	public static BlockState slabBottomDoubleFoliage(BlockState state)
	{
		if(state.getBlock() == Blocks.TALL_GRASS)
			return ModBlocks.tall_grass_slab.getDefaultState();
		if(state.getBlock() == Blocks.LARGE_FERN)
			return ModBlocks.large_fern_slab.getDefaultState();
		
		if(state.getBlock() == Blocks.SUNFLOWER)
			return ModBlocks.sunflower_slab.getDefaultState();	
		if(state.getBlock() == Blocks.LILAC)
			return ModBlocks.lilac_slab.getDefaultState();	
		if(state.getBlock() == Blocks.ROSE_BUSH)
			return ModBlocks.rose_bush_slab.getDefaultState();	
		if(state.getBlock() == Blocks.PEONY)
			return ModBlocks.peony_slab.getDefaultState();	
		
		return state;
	}
	
}
