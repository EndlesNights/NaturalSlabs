package com.endlesnights.naturalslabsmod.blocks.foliage;

import java.util.Random;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.TallGrassBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.server.ServerWorld;

public class TallGrassSlab extends TallGrassBlock implements IGrowable, net.minecraftforge.common.IShearable
{
	public TallGrassSlab(Block.Properties properties, String registerName)
	{
		super(properties.doesNotBlockMovement());
		this.setRegistryName(NaturalSlabsMod.MODID, registerName);
		//super(Block.Properties.from(Blocks.GRASS));
		//this.setDefaultState(this.getDefaultState());
		//this.setRegistryName(NaturalSlabsMod.MODID, "grass_slab");
	}
	
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XYZ;
	}
	
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_)
	{
		DoublePlantBlock doubleplantblock = (DoublePlantBlock)(this == ModBlocks.fern_slab ? Blocks.LARGE_FERN : Blocks.TALL_GRASS);
		if (doubleplantblock.getDefaultState().isValidPosition(p_225535_1_, p_225535_3_) && p_225535_1_.isAirBlock(p_225535_3_.up())) {
			doubleplantblock.placeAt(p_225535_1_, p_225535_3_, 2);
		}

	}
//	
//    public BlockRenderLayer getRenderLayer() 
//    {
//        return BlockRenderLayer.TRANSLUCENT;
//    }
//	
	
	public static class ColorHandler implements IBlockColor 
	{
		@Override
		public int getColor(BlockState state, @Nullable ILightReader reader , @Nullable BlockPos blockPos, int tintIndex)
		{
			// TODO Auto-generated method stub
			if(reader != null && blockPos != null)
			{
				return BiomeColors.getFoliageColor(reader, blockPos);
				//return reader.getBiome(blockPos).getGrassColor(blockPos);
			}
			return GrassColors.get(0.5D, 1.0D);
		}
	}
}
