package com.endlesnights.naturalslabsmod.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BlockSunFlower extends TallFlowerBlock
{
	public static final DirectionProperty HORIZONTAL_FACING = HorizontalBlock.HORIZONTAL_FACING;
	
	public BlockSunFlower(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(HALF, DoubleBlockHalf.LOWER).with(HORIZONTAL_FACING, Direction.SOUTH));
	}
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(HALF, HORIZONTAL_FACING);
	}
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		DoubleBlockHalf doubleblockhalf = stateIn.get(HALF);
		if(doubleblockhalf == DoubleBlockHalf.UPPER 
				&& worldIn.getBlockState(currentPos.down()).getBlock() instanceof BlockSunFlower
				&& worldIn.getBlockState(currentPos.down()).get(HALF) == DoubleBlockHalf.LOWER)
			return stateIn.with(HORIZONTAL_FACING, worldIn.getBlockState(currentPos.down()).get(HORIZONTAL_FACING));
		
		if (facing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.getBlock() == this && facingState.get(HALF) != doubleblockhalf) {
			return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
		} else {
			return Blocks.AIR.getDefaultState();
		}
	}
	
	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		Direction[] adirection = context.getNearestLookingDirections();

		for(Direction direction : adirection) {
			if (direction.getAxis().isHorizontal()) {
				Direction direction1 = direction.getOpposite();
				BlockPos blockpos = context.getPos();
				return blockpos.getY() < context.getWorld().getDimension().getHeight() - 1 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) ? this.getDefaultState().with(HORIZONTAL_FACING, direction1) : null;
			}
		}
		return null;
	}
}
