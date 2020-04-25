package com.endlesnights.naturalslabsmod.blocks.slabs;

import java.util.Random;
import java.util.stream.IntStream;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.StairsShape;
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

public class BlockSnowStairs extends Block
{
	public static final IntegerProperty LAYERS = BlockSnowSlab.LAYERS_1_12;
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
	   	
	protected static VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
	protected static VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, -6.0D, 16.0D);
	protected static VoxelShape NWD_CORNER = Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 8.0D, -6.0D, 8.0D);
	protected static VoxelShape SWD_CORNER = Block.makeCuboidShape(0.0D, -8.0D, 8.0D, 8.0D, -6.0D, 16.0D);
	protected static VoxelShape NWU_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 2.0D, 8.0D);
	protected static VoxelShape SWU_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 8.0D, 2.0D, 16.0D);
	protected static VoxelShape NED_CORNER = Block.makeCuboidShape(8.0D, -8.0D, 0.0D, 16.0D, -6.0D, 8.0D);
	protected static VoxelShape SED_CORNER = Block.makeCuboidShape(8.0D, -8.0D, 8.0D, 16.0D, -6.0D, 16.0D);
	protected static VoxelShape NEU_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 2.0D, 8.0D);
	protected static VoxelShape SEU_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 8.0D, 16.0D, 2.0D, 16.0D);
	
	protected static VoxelShape[] SLAB_TOP_SHAPES = makeShapes(TOP_SHAPE, NWD_CORNER, NED_CORNER, SWD_CORNER, SED_CORNER);
	protected static VoxelShape[] SLAB_BOTTOM_SHAPES = makeShapes(BOTTOM_SHAPE, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
	
	private static final int[] field_196522_K = new int[]{12, 5, 3, 10, 14, 13, 7, 11, 13, 7, 11, 14, 8, 4, 1, 2, 4, 1, 2, 8};
	
	public BlockSnowStairs(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState()
				.with(FACING, Direction.NORTH)
				.with(SHAPE, StairsShape.STRAIGHT)
				.with(LAYERS, Integer.valueOf(1)));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, SHAPE, LAYERS);
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		return worldIn.getBlockState(pos.down()).getBlock() instanceof StairsBlock
				&& worldIn.getBlockState(pos.down()).get(StairsBlock.HALF) == Half.BOTTOM;
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{

		if(worldIn.getBlockState(currentPos.down()).getBlock() instanceof StairsBlock && worldIn.getBlockState(currentPos.down()).get(StairsBlock.HALF) == Half.BOTTOM)
			return stateIn  
				.with(StairsBlock.FACING, worldIn.getBlockState(currentPos.down()).get(StairsBlock.FACING))
				.with(StairsBlock.SHAPE, worldIn.getBlockState(currentPos.down()).get(StairsBlock.SHAPE));
		
		return !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
	private static VoxelShape[] makeShapes(VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner)
	{

		return IntStream.range(0, 16).mapToObj((p_199780_5_) -> {
			return combineShapes(p_199780_5_, slabShape, nwCorner, neCorner, swCorner, seCorner);
		}).toArray((p_199778_0_) -> {
			return new VoxelShape[p_199778_0_];
		});
	}  
	
	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}
	
	private static VoxelShape combineShapes(int bitfield, VoxelShape slabShape, VoxelShape nwCorner, VoxelShape neCorner, VoxelShape swCorner, VoxelShape seCorner)
	{
		VoxelShape voxelshape = slabShape;
		if ((bitfield & 1) != 0) {
			voxelshape = VoxelShapes.or(slabShape, nwCorner);
		}

		if ((bitfield & 2) != 0) {
			voxelshape = VoxelShapes.or(voxelshape, neCorner);
		}

		if ((bitfield & 4) != 0) {
			voxelshape = VoxelShapes.or(voxelshape, swCorner);
		}

		if ((bitfield & 8) != 0) {
			voxelshape = VoxelShapes.or(voxelshape, seCorner);
		}

		return voxelshape;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, -6.0D + 2*(state.get(LAYERS)-1), 16.0D);
		
		double offset =  Math.round((14.0D/11.0D)*(state.get(LAYERS)-1));
		NWU_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 2.0D + offset, 8.0D);
		NEU_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 2.0D + offset, 8.0D);
		SWU_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 8.0D, 2.0D + offset, 16.0D);
		SEU_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 8.0D, 16.0D, 2.0D + offset, 16.0D);
		
		SLAB_BOTTOM_SHAPES = makeShapes(BOTTOM_SHAPE, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
		
		return SLAB_BOTTOM_SHAPES[field_196522_K[this.func_196511_x(state)]];
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if(state.get(LAYERS) == 12)
			return getShape(state, worldIn, pos, context);
		
		BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, -7.0D + 2*(state.get(LAYERS)-1), 16.0D);
		
		double offset =  Math.round((14.0D/11.0D)*(state.get(LAYERS)-1));
		NWU_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 1.0D + offset, 8.0D);
		NEU_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 0.0D, 16.0D, 1.0D + offset, 8.0D);
		SWU_CORNER = Block.makeCuboidShape(0.0D, 0.0D, 8.0D, 8.0D, 1.0D + offset, 16.0D);
		SEU_CORNER = Block.makeCuboidShape(8.0D, 0.0D, 8.0D, 16.0D, 1.0D + offset, 16.0D);
		
		SLAB_BOTTOM_SHAPES = makeShapes(BOTTOM_SHAPE, NWU_CORNER, NEU_CORNER, SWU_CORNER, SEU_CORNER);
		
		return SLAB_BOTTOM_SHAPES[field_196522_K[this.func_196511_x(state)]];
	}
	
	
	private int func_196511_x(BlockState state)
	{
		return state.get(SHAPE).ordinal() * 4 + state.get(FACING).getHorizontalIndex();
	}
	
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
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(Items.SNOW);
	}
}
