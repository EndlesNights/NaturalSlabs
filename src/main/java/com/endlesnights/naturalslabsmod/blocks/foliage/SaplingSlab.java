package com.endlesnights.naturalslabsmod.blocks.foliage;

import java.util.Random;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.trees.Tree;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

public class SaplingSlab extends SaplingBlock
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private Tree tree;
	   
	public SaplingSlab(Tree tree, Block.Properties properties)
	{
		   super(tree, properties);
	      this.tree = tree;
	      this.setDefaultState(this.stateContainer.getBaseState().with(STAGE, Integer.valueOf(0)));
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return SHAPE;
	}
	   
	@Override
	public void func_226942_a_(ServerWorld worldIn, BlockPos blockPos, BlockState blockState, Random rand)
	{
		if (blockState.get(STAGE) == 0) {
			worldIn.setBlockState(blockPos, blockState.cycle(STAGE), 4);
		} else {
			if (!net.minecraftforge.event.ForgeEventFactory.saplingGrowTree(worldIn, rand, blockPos))
				return;
			this.tree.place(worldIn, worldIn.getChunkProvider().getChunkGenerator(), blockPos, blockState, rand);
			worldIn.setBlockState(blockPos.down(), Blocks.DIRT.getDefaultState());
		}
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
	{
		return facing == Direction.DOWN && !isValidPosition(state, world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
	{
		return (world.getBlockState(pos.offset(Direction.DOWN)).getProperties().contains(SlabBlock.TYPE) 
				&& world.getBlockState(pos.offset(Direction.DOWN)).get(SlabBlock.TYPE) == SlabType.BOTTOM);
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		
		if(this == ModBlocks.oak_sapling_slab)
		{
			return new ItemStack(Items.OAK_SAPLING);
		}
		else if (this == ModBlocks.spruce_sapling_slab)
		{
			return new ItemStack(Items.SPRUCE_SAPLING);
		}
		else if (this == ModBlocks.birch_sapling_slab)
		{
			return new ItemStack(Items.BIRCH_SAPLING);
		}
		else if (this == ModBlocks.jungle_sapling_slab)
		{
			return new ItemStack(Items.JUNGLE_SAPLING);
		}
		else if (this == ModBlocks.acacia_sapling_slab)
		{
			return new ItemStack(Items.ACACIA_SAPLING);
		}
		else if (this == ModBlocks.dark_oak_sapling_slab)
		{
			return new ItemStack(Items.DARK_OAK_SAPLING);
		}
		
		return new ItemStack(null);
	}
}
