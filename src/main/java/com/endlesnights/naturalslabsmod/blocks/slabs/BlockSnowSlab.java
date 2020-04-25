package com.endlesnights.naturalslabsmod.blocks.slabs;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LightType;
import net.minecraft.world.server.ServerWorld;

public class BlockSnowSlab extends Block
{
	public static final IntegerProperty LAYERS_1_12 = IntegerProperty.create("snowlayers", 1, 12);
	public static final IntegerProperty LAYERS = LAYERS_1_12;
	//public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS_1_8;
	
	protected static final VoxelShape[] SHAPES = new VoxelShape[]{VoxelShapes.empty(),

			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 2.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 4.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 6.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 8.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 10.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 12.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 14.0D, 16.0D),
			Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

	 
	public BlockSnowSlab(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(LAYERS, Integer.valueOf(1)));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(LAYERS);
	}
//
	@Override
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		switch(type) {
		case LAND:
			return state.get(LAYERS) < 5;
		case WATER:
			return false;
		case AIR:
			return false;
		default:
			return false;
		}
	}
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		System.out.println(state.get(LAYERS));
			return SHAPES[state.get(LAYERS)];
	}
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if(state.get(LAYERS) == 1)
			return VoxelShapes.empty();
		else if(state.get(LAYERS) == 12)
			return SHAPES[state.get(LAYERS)];
		else if(state.get(LAYERS) < 5)
		{
			return Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, -8.0D + 2 * (state.get(LAYERS) - 1) , 16.0D);
		}
		else
			return SHAPES[state.get(LAYERS)-1];
	}
	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()).getBlock() instanceof SlabBlock 
				&& worldIn.getBlockState(pos.down()).get(SlabBlock.TYPE) == SlabType.BOTTOM;
	}

		   /**
		    * Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
		    * For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
		    * returns its solidified counterpart.
		    * Note that this method should ideally consider only the specific face passed in.
		    */
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	@Override
	public void tick(BlockState state, ServerWorld worldIn, BlockPos blockPos, Random rand) {
		if (worldIn.getLightFor(LightType.BLOCK, blockPos) > 7) {
			spawnDrops(state, worldIn, blockPos);
			worldIn.removeBlock(blockPos, false);
		}

	}
	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		int i = state.get(LAYERS);
		if (useContext.getItem().getItem() == this.asItem() && i < 12) {
			if (useContext.replacingClickedOnBlock()) {
				return useContext.getFace() == Direction.UP;
			} else {
				return true;
			}
		} else {
			return i == 1;
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockstate = context.getWorld().getBlockState(context.getPos());
		if (blockstate.getBlock() == this) {
			int i = blockstate.get(LAYERS);
			return blockstate.with(LAYERS, Integer.valueOf(Math.min(12, i + 1)));
		} else {
			return super.getStateForPlacement(context);
		}
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(Items.SNOW);
	}
}
