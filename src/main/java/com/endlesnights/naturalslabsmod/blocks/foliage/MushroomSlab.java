package com.endlesnights.naturalslabsmod.blocks.foliage;

import java.util.Random;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.MushroomBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.BigMushroomFeatureConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.server.ServerWorld;

public class MushroomSlab extends MushroomBlock implements IGrowable
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, -8.0D, 5.0D, 11.0D, 2.0D, 11.0D);
	
	public MushroomSlab(Block.Properties properties)
	{
		super(properties);
	}
	
   public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
	      if (p_225534_4_.nextInt(25) == 0) {
	         int i = 5;
	         int j = 4;

	         for(BlockPos blockpos : BlockPos.getAllInBoxMutable(p_225534_3_.add(-4, -1, -4), p_225534_3_.add(4, 1, 4))) {
	            if (p_225534_2_.getBlockState(blockpos).getBlock() == this) {
	               --i;
	               if (i <= 0) {
	                  return;
	               }
	            }
	         }

	         BlockPos blockpos1 = p_225534_3_.add(p_225534_4_.nextInt(3) - 1, p_225534_4_.nextInt(2) - p_225534_4_.nextInt(2), p_225534_4_.nextInt(3) - 1);

	         for(int k = 0; k < 4; ++k) {
	            if (p_225534_2_.isAirBlock(blockpos1) && p_225534_1_.isValidPosition(p_225534_2_, blockpos1)) {
	               p_225534_3_ = blockpos1;
	            }

	            blockpos1 = p_225534_3_.add(p_225534_4_.nextInt(3) - 1, p_225534_4_.nextInt(2) - p_225534_4_.nextInt(2), p_225534_4_.nextInt(3) - 1);
	         }

	         if (p_225534_2_.isAirBlock(blockpos1) && p_225534_1_.isValidPosition(p_225534_2_, blockpos1)) {
	            p_225534_2_.setBlockState(blockpos1, p_225534_1_, 2);
	         }
	      }

	   }

	   protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
	      return state.isOpaqueCube(worldIn, pos);
	   }

	   public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
	      BlockPos blockpos = pos.down();
	      BlockState blockstate = worldIn.getBlockState(blockpos);
	      Block block = blockstate.getBlock();
	      
	      if(!(blockstate.getBlock() instanceof SlabBlock)
	    		  || blockstate.get(SlabBlock.TYPE) != SlabType.BOTTOM 
	    		  || blockstate.get(SlabBlock.WATERLOGGED))
	    	  return false;
	      
	      if (block != Blocks.MYCELIUM && block != Blocks.PODZOL) {
	         return worldIn.getLightSubtracted(pos, 0) < 13 && blockstate.canSustainPlant(worldIn, blockpos, net.minecraft.util.Direction.UP, this);
	      } else {
	         return true;
	      }
	   }

	   public boolean func_226940_a_(ServerWorld p_226940_1_, BlockPos p_226940_2_, BlockState p_226940_3_, Random p_226940_4_) {
	      p_226940_1_.removeBlock(p_226940_2_, false);
	      ConfiguredFeature<BigMushroomFeatureConfig, ?> configuredfeature;
	      if (this == ModBlocks.brown_mushroom_slab) {
	         configuredfeature = Feature.HUGE_BROWN_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_BROWN_MUSHROOM);
	      } else {
	         if (this != ModBlocks.red_mushroom_slab) {
	            p_226940_1_.setBlockState(p_226940_2_, p_226940_3_, 3);
	            return false;
	         }

	         configuredfeature = Feature.HUGE_RED_MUSHROOM.withConfiguration(DefaultBiomeFeatures.BIG_RED_MUSHROOM);
	      }

	      if (configuredfeature.place(p_226940_1_, p_226940_1_.getChunkProvider().getChunkGenerator(), p_226940_4_, p_226940_2_)) {
	         return true;
	      } else {
	         p_226940_1_.setBlockState(p_226940_2_, p_226940_3_, 3);
	         return false;
	      }
	   }

	   /**
	    * Whether this IGrowable can grow
	    */
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
		return (double)rand.nextFloat() < 0.4D;
	}

	public void func_225535_a_(ServerWorld p_225535_1_, Random p_225535_2_, BlockPos p_225535_3_, BlockState p_225535_4_) {
		this.func_226940_a_(p_225535_1_, p_225535_3_, p_225535_4_, p_225535_2_);
	}

	public boolean needsPostProcessing(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return true;
	}
		   
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		if (this == ModBlocks.brown_mushroom_slab)
			return new ItemStack(ModBlocks.brown_mushroom_slab.asItem());
		
		if (this == ModBlocks.red_mushroom_slab)
			return new ItemStack(ModBlocks.red_mushroom_slab.asItem());
		
		return null;
	}
}
