package com.endlesnights.naturalslabsmod.blocks.foliage;

import java.util.Random;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class TallFlowerSlab extends DoublePlantSlab implements IGrowable
{
	public TallFlowerSlab(Block.Properties properties)
	{
		super(properties);
	}

	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		return false;
	}

	   /**
	    * Whether this IGrowable can grow
	    */
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
	{
		return facing == Direction.DOWN && !isValidPosition(state, world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}
	
	public void grow(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState state)
	{
		if(state.getBlock() == ModBlocks.sunflower_slab)
			spawnAsEntity(p_225535_1_, p_225535_3_, new ItemStack(Blocks.SUNFLOWER));
		else if(state.getBlock() == ModBlocks.lilac_slab)
			spawnAsEntity(p_225535_1_, p_225535_3_, new ItemStack(Blocks.LILAC));
		else if(state.getBlock() == ModBlocks.rose_bush_slab)
			spawnAsEntity(p_225535_1_, p_225535_3_, new ItemStack(Blocks.ROSE_BUSH));
		else if(state.getBlock() == ModBlocks.peony_slab)
			spawnAsEntity(p_225535_1_, p_225535_3_, new ItemStack(Blocks.PEONY));
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		if (this == ModBlocks.sunflower_slab)
			return new ItemStack(Items.SUNFLOWER);
		else if (this == ModBlocks.lilac_slab)
			return new ItemStack(Items.LILAC);
		else if (this == ModBlocks.rose_bush_slab)
			return new ItemStack(Items.ROSE_BUSH);
		else if (this == ModBlocks.peony_slab)
			return new ItemStack(Items.PEONY);
		
		return new ItemStack(null);
	}
	
//	@Override
//	public ResourceLocation getLootTable()
//	{
//		if (this.getBlock() == ModBlocks.sunflower_slab)
//			return Blocks.SUNFLOWER.getLootTable();
//		else if (this.getBlock() == ModBlocks.lilac_slab)
//			return Blocks.LILAC.getLootTable();
//		else if (this.getBlock() == ModBlocks.rose_bush_slab)
//			return Blocks.ROSE_BUSH.getLootTable();
//		else if (this.getBlock() == ModBlocks.peony_slab)
//			return Blocks.PEONY.getLootTable();
//		
//		return null;
//	}
}
