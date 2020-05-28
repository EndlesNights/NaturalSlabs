package com.endlesnights.naturalslabsmod.blocks.stair;

import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.util.StairAction;

import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

@SuppressWarnings("deprecation")
public class BlockCoarseDirtStair extends StairsBlock 
{

	
	public BlockCoarseDirtStair(BlockState state, Properties properties)
	{
		super(state, properties);
	}

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
    	
		ItemStack itemStack = playerIn.getHeldItem(hand);
		
		if (itemStack.getItem() instanceof HoeItem )
        	return onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
		if (itemStack.getItem() instanceof ShovelItem)
			return StairAction.onItemUseSpade(state, worldIn, pos, playerIn, hand, p_225533_6_);

			   
        return ActionResultType.FAIL;
        
    }
    
    public static ActionResultType onItemUseHoe(BlockState state,  World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
		
		ItemUseContext context = new ItemUseContext(playerIn, hand, p_225533_6_);
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		
		if (context.getFace() != Direction.DOWN && world.getBlockState(blockpos.up()).isAir())
		{
		    BlockState blockstate = ModBlocks.block_dirt_stairs.getDefaultState()
		    		.with(StairsBlock.FACING, state.get(StairsBlock.FACING))
		    		.with(StairsBlock.HALF, state.get(StairsBlock.HALF))
		    		.with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE))
		    		.with(StairsBlock.WATERLOGGED, state.get(StairsBlock.WATERLOGGED))
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
}
