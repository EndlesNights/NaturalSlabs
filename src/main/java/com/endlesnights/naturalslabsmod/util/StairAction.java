package com.endlesnights.naturalslabsmod.util;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

public class StairAction
{
    @SuppressWarnings("deprecation")
	public static ActionResultType onItemUseHoe(BlockState state,  World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
		
		ItemUseContext context = new ItemUseContext(playerIn, hand, p_225533_6_);
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		
		if (context.getFace() != Direction.DOWN && world.getBlockState(blockpos.up()).isAir())
		{
			BlockState blockstate = ModBlocks.block_farmland_slab.getDefaultState()
					.with(SlabBlock.TYPE, (state.get(StairsBlock.HALF) == Half.BOTTOM ? SlabType.BOTTOM : SlabType.TOP))
					.with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED))
					;
		    
		    if (blockstate != null)
		    {
				PlayerEntity playerentity = context.getPlayer();
				world.playSound(playerentity, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);			    
				world.setBlockState(blockpos, blockstate);
			    if (playerentity != null)
			    {
				context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
					    p_220043_1_.sendBreakAnimation(context.getHand());
					});
			    }
			    return ActionResultType.SUCCESS;
		    }
		}
    	return ActionResultType.FAIL;
    }
    
    @SuppressWarnings("deprecation")
	public static ActionResultType onItemUseSpade(BlockState state,  World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
		
		ItemUseContext context = new ItemUseContext(playerIn, hand, p_225533_6_);
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		
		if (context.getFace() != Direction.DOWN && world.getBlockState(blockpos.up()).isAir())
		{
		    BlockState blockstate = ModBlocks.block_path_stairs.getDefaultState()
		    		.with(StairsBlock.FACING, state.get(StairsBlock.FACING))
		    		.with(StairsBlock.HALF, state.get(StairsBlock.HALF))
		    		.with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE))
		    		.with(StairsBlock.WATERLOGGED, state.get(StairsBlock.WATERLOGGED))
		    		;
		    
		    if (blockstate != null)
		    {
				PlayerEntity playerentity = context.getPlayer();
				world.playSound(playerentity, blockpos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);			    
				world.setBlockState(blockpos, blockstate);
			    if (playerentity != null)
			    {
				context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
					    p_220043_1_.sendBreakAnimation(context.getHand());
					});
			    }
			    return ActionResultType.SUCCESS;
		    }
		}
    	return ActionResultType.FAIL;
    }
}
