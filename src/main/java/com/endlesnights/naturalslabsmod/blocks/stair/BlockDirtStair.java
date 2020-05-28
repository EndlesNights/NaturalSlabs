package com.endlesnights.naturalslabsmod.blocks.stair;

import java.util.Random;

import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.util.StairAction;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;

@SuppressWarnings("deprecation")
public class BlockDirtStair extends StairsBlock
{

	public BlockDirtStair(BlockState state, Properties properties)
	{
		super(state, properties.tickRandomly());
	}

	@Override
	public void tick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random)
	{
		super.tick(blockState, world, blockPos, random);
		SpreadableSnowyDirtBlock(blockState, world, blockPos, random);
	}
	
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
    	
		ItemStack itemStack = playerIn.getHeldItem(hand);
		
		if (itemStack.getItem() instanceof HoeItem )
        	return StairAction.onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
		else if (itemStack.getItem() instanceof ShovelItem)
			return StairAction.onItemUseSpade(state, worldIn, pos, playerIn, hand, p_225533_6_);

        return ActionResultType.FAIL;
    }
	
	private void SpreadableSnowyDirtBlock(BlockState state, ServerWorld world, BlockPos blockPos, Random random)
	{
		if (world.getLight(blockPos.up()) >= 9)
		{
			BlockState blockstate = this.getDefaultState();
				
			for(int i = 0; i < 4; ++i)
			{
				BlockPos pos = blockPos.add(random.nextInt(3) - 1, 3 - random.nextInt(5), random.nextInt(3) - 1);
				if ((world.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK && func_220256_c(blockstate, world, pos))
						|| ((world.getBlockState(pos).getBlock() == ModBlocks.block_grass_slab && func_220256_c(blockstate, world, pos))
								&& !(world.getBlockState(pos).get(SlabBlock.TYPE) == SlabType.BOTTOM && world.getBlockState(pos).get(WATERLOGGED) == true) ) )
				{
					blockstate = ModBlocks.block_grass_stairs.getDefaultState()
				    		.with(StairsBlock.FACING, state.get(StairsBlock.FACING))
				    		.with(StairsBlock.HALF, state.get(StairsBlock.HALF))
				    		.with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE))
				    		.with(StairsBlock.WATERLOGGED, state.get(StairsBlock.WATERLOGGED))
				    		; // TODO set up a check for Block.Dirt and different DirtSlab
					world.setBlockState(blockPos, blockstate);
				}
			}
		}
	}
	
	private static boolean func_220257_b(BlockState state, IWorldReader reader, BlockPos pos)
	{
		BlockPos blockpos = pos.up();
		BlockState blockstate = reader.getBlockState(blockpos);
				
		if (blockstate.getBlock() == Blocks.SNOW && blockstate.get(SnowBlock.LAYERS) == 1)
		{
			return true;
		}
		else
		{	
			int i = LightEngine.func_215613_a(reader, state, pos, blockstate, blockpos,
					state.get(HALF) == Half.BOTTOM ? Direction.UP : Direction.DOWN,
					blockstate.getOpacity(reader, blockpos));
			
//			if(state.getBlock() == ModBlocks.block_dirt_slab && state.get(SlabBlock.TYPE) == SlabType.TOP)
//			{
//				
//				i = LightEngine.func_215613_a(reader, Blocks.GRASS_BLOCK.getDefaultState(), pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(reader, blockpos));
//			}
//			
			return i < reader.getMaxLightLevel();
		}
	}

	
	private static boolean func_220256_c(BlockState p_220256_0_, IWorldReader p_220256_1_, BlockPos p_220256_2_)
	{
		BlockPos blockpos = p_220256_2_.up();
		
		
		return func_220257_b(p_220256_0_, p_220256_1_, p_220256_2_) && !p_220256_1_.getFluidState(blockpos).isTagged(FluidTags.WATER);
	}
}
