package com.endlesnights.naturalslabsmod.blocks;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FourWayBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.LeadItem;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class FenceSlabBlock extends FourWayBlock
{
	public static final BooleanProperty NORTH = BooleanProperty.create("north");
	public static final BooleanProperty EAST = BooleanProperty.create("east");
	public static final BooleanProperty SOUTH = BooleanProperty.create("south");
	public static final BooleanProperty WEST = BooleanProperty.create("west");
	
	public static final BooleanProperty NORTH_UP = BooleanProperty.create("north_up");
	public static final BooleanProperty EAST_UP = BooleanProperty.create("east_up");
	public static final BooleanProperty SOUTH_UP = BooleanProperty.create("south_up");
	public static final BooleanProperty WEST_UP = BooleanProperty.create("west_up");
	
	public static final BooleanProperty NORTH_DOWN = BooleanProperty.create("north_down");
	public static final BooleanProperty EAST_DOWN = BooleanProperty.create("east_down");
	public static final BooleanProperty SOUTH_DOWN = BooleanProperty.create("south_down");
	public static final BooleanProperty WEST_DOWN = BooleanProperty.create("west_down");
	
	private final VoxelShape[] renderShapes;
	private final Object2IntMap<BlockState> field_223008_i = new Object2IntOpenHashMap<>();
	
	Block parrentBlock;
	
	public FenceSlabBlock(Block parrent, Properties properties)
	{
		super(2.0F, 2.0F, 16.0F, 16.0F, 23.99F, properties.lootFrom(parrent).notSolid());
		this.setDefaultState(this.stateContainer.getBaseState()
				.with(NORTH, Boolean.valueOf(false))
				.with(EAST, Boolean.valueOf(false))
				.with(SOUTH, Boolean.valueOf(false))
				.with(WEST, Boolean.valueOf(false))
				
				.with(NORTH_UP, Boolean.valueOf(false))
				.with(EAST_UP, Boolean.valueOf(false))
				.with(SOUTH_UP, Boolean.valueOf(false))
				.with(WEST_UP, Boolean.valueOf(false))
				
				.with(NORTH_DOWN, Boolean.valueOf(false))
				.with(EAST_DOWN, Boolean.valueOf(false))
				.with(SOUTH_DOWN, Boolean.valueOf(false))
				.with(WEST_DOWN, Boolean.valueOf(false))
				
				.with(WATERLOGGED, Boolean.valueOf(false))
				);
		
		this.parrentBlock = parrent;
		
		//this.collisionShapes = this.makeShapes(nodeWidth, extensionWidth, collisionY, 0.0F, collisionY);
		//this.shapes = this.makeShapes(nodeWidth, extensionWidth, p_i48420_3_, 0.0F, p_i48420_4_);
	      
		this.renderShapes = this.makeShapes(2.0F, 1.0F, 16.0F, 6.0F, 15.0F);
	}
	
	public VoxelShape getRenderShape(BlockState state, IBlockReader worldIn, BlockPos pos) {
		return this.renderShapes[this.getIndex(state)];
	}

	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	public boolean checkConnection(BlockState blockState, boolean solidWall, Direction facing) {
		Block block = blockState.getBlock();
		boolean flag = block instanceof FenceSlabBlock;// .isIn(BlockTags.FENCES) && blockState.getMaterial() == this.material;
		boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.isParallel(blockState, facing);
		return !cannotAttach(block) && solidWall || flag || flag1;
	}
	
	public boolean checkConnectionUpDown(BlockState blockState) {
		Block block = blockState.getBlock();
		boolean flag = block.isIn(BlockTags.FENCES) && blockState.getMaterial() == this.material;
		//boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.isParallel(blockState, facing);
		return flag;//!cannotAttach(block) && solidWall || flag || flag1;
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult p_225533_6_) {
		if (worldIn.isRemote) {
			ItemStack itemstack = player.getHeldItem(handIn);
			return itemstack.getItem() == Items.LEAD ? ActionResultType.SUCCESS : ActionResultType.PASS;
		} else {
			return LeadItem.func_226641_a_(player, worldIn, pos);
		}
	}

	public BlockState getStateForPlacement(BlockItemUseContext context) {
		IBlockReader iblockreader = context.getWorld();
		BlockPos blockpos = context.getPos();
		IFluidState ifluidstate = context.getWorld().getFluidState(context.getPos());
		
		BlockPos blockpos1 = blockpos.north();
		BlockPos blockpos2 = blockpos.east();
		BlockPos blockpos3 = blockpos.south();
		BlockPos blockpos4 = blockpos.west();
		
		BlockState blockstate = iblockreader.getBlockState(blockpos1);
		BlockState blockstate1 = iblockreader.getBlockState(blockpos2);
		BlockState blockstate2 = iblockreader.getBlockState(blockpos3);
		BlockState blockstate3 = iblockreader.getBlockState(blockpos4);
		
		BlockPos blockpos1Down = blockpos.north().down();
		BlockPos blockpos2Down = blockpos.east().down();
		BlockPos blockpos3Down = blockpos.south().down();
		BlockPos blockpos4Down = blockpos.west().down();
		
		BlockState blockstateDown = iblockreader.getBlockState(blockpos1Down);
		BlockState blockstate1Down = iblockreader.getBlockState(blockpos2Down);
		BlockState blockstate2Down = iblockreader.getBlockState(blockpos3Down);
		BlockState blockstate3Down = iblockreader.getBlockState(blockpos4Down);
		
		return super.getStateForPlacement(context)
				.with(NORTH, Boolean.valueOf(this.checkConnection(blockstate, blockstate.isSolidSide(iblockreader, blockpos1, Direction.SOUTH), Direction.SOUTH)))
				.with(EAST, Boolean.valueOf(this.checkConnection(blockstate1, blockstate1.isSolidSide(iblockreader, blockpos2, Direction.WEST), Direction.WEST)))
				.with(SOUTH, Boolean.valueOf(this.checkConnection(blockstate2, blockstate2.isSolidSide(iblockreader, blockpos3, Direction.NORTH), Direction.NORTH)))
				.with(WEST, Boolean.valueOf(this.checkConnection(blockstate3, blockstate3.isSolidSide(iblockreader, blockpos4, Direction.EAST), Direction.EAST)))
				
				.with(NORTH_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate)))
				.with(EAST_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate1)))
				.with(SOUTH_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate2)))
				.with(WEST_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate3)))
				
				.with(NORTH_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstateDown)))
				.with(EAST_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstate1Down)))
				.with(SOUTH_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstate2Down)))
				.with(WEST_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstate3Down)))
				
				.with(WATERLOGGED, Boolean.valueOf(ifluidstate.getFluid() == Fluids.WATER));
	}

	/**
	* Update the provided state given the provided neighbor facing and neighbor state, returning a new state.
	* For example, fences make their connections to the passed in state if possible, and wet concrete powder immediately
	* returns its solidified counterpart.
	* Note that this method should ideally consider only the specific face passed in.
	*/
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (stateIn.get(WATERLOGGED)) {
			worldIn.getPendingFluidTicks().scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickRate(worldIn));
		}
				
		if(hasEnoughSolidSide(worldIn, currentPos.down(), Direction.UP) && !(worldIn.getBlockState(currentPos.down()).getBlock() instanceof FenceSlabBlock))
		{
			worldIn.destroyBlock(currentPos, true);
			return worldIn.getBlockState(currentPos);
			//return !(stateIn.get(WATERLOGGED)) ? Blocks.AIR.getDefaultState() : Blocks.WATER.getDefaultState();
		}
		
		BlockPos blockpos1 = currentPos.north();
		BlockPos blockpos2 = currentPos.east();
		BlockPos blockpos3 = currentPos.south();
		BlockPos blockpos4 = currentPos.west();
		
		BlockState blockstate = worldIn.getBlockState(blockpos1);
		BlockState blockstate1 = worldIn.getBlockState(blockpos2);
		BlockState blockstate2 = worldIn.getBlockState(blockpos3);
		BlockState blockstate3 = worldIn.getBlockState(blockpos4);
		
		BlockPos blockpos1Down = currentPos.north().down();
		BlockPos blockpos2Down = currentPos.east().down();
		BlockPos blockpos3Down = currentPos.south().down();
		BlockPos blockpos4Down = currentPos.west().down();
		
		BlockState blockstateDown = worldIn.getBlockState(blockpos1Down);
		BlockState blockstate1Down = worldIn.getBlockState(blockpos2Down);
		BlockState blockstate2Down = worldIn.getBlockState(blockpos3Down);
		BlockState blockstate3Down = worldIn.getBlockState(blockpos4Down);
		
		
		return stateIn
				.with(NORTH, Boolean.valueOf(this.checkConnection(blockstate, blockstate.isSolidSide(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH)))
				.with(EAST, Boolean.valueOf(this.checkConnection(blockstate1, blockstate1.isSolidSide(worldIn, blockpos2, Direction.WEST), Direction.WEST)))
				.with(SOUTH, Boolean.valueOf(this.checkConnection(blockstate2, blockstate2.isSolidSide(worldIn, blockpos3, Direction.NORTH), Direction.NORTH)))
				.with(WEST, Boolean.valueOf(this.checkConnection(blockstate3, blockstate3.isSolidSide(worldIn, blockpos4, Direction.EAST), Direction.EAST)))
				
				.with(NORTH_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate)))
				.with(EAST_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate1)))
				.with(SOUTH_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate2)))
				.with(WEST_UP, Boolean.valueOf(this.checkConnectionUpDown(blockstate3)))
				
				.with(NORTH_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstateDown)))
				.with(EAST_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstate1Down)))
				.with(SOUTH_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstate2Down)))
				.with(WEST_DOWN, Boolean.valueOf(this.checkConnectionUpDown(blockstate3Down)))
				
				.with(WATERLOGGED, stateIn.get(WATERLOGGED));
//		return facing.getAxis().getPlane() == Direction.Plane.HORIZONTAL ? 
//				stateIn.with(FACING_TO_PROPERTY_MAP.get(facing), Boolean.valueOf(this.checkConnection(facingState, facingState.isSolidSide(worldIn, facingPos, facing.getOpposite()), facing.getOpposite()))) 
//				: super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(NORTH, EAST, WEST, SOUTH,
				NORTH_UP, EAST_UP, WEST_UP, SOUTH_UP,
				NORTH_DOWN, EAST_DOWN, WEST_DOWN, SOUTH_DOWN,
				WATERLOGGED);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.shapes[this.getIndex(state)];
	}
	
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return this.collisionShapes[this.getIndex(state)];
	}
	
	private static int getMask(Direction facing) {
		return 1 << facing.getHorizontalIndex();
	}
	
	protected int getIndex(BlockState state)
	{
		return this.field_223008_i.computeIntIfAbsent(state, (blockState) -> {
			int i = 0;
			if (blockState.get(NORTH) || blockState.get(NORTH_UP) || blockState.get(NORTH_DOWN) ) {
				i |= getMask(Direction.NORTH);
			}

			if (blockState.get(EAST) || blockState.get(EAST_UP) || blockState.get(EAST_DOWN) ) {
				i |= getMask(Direction.EAST);
			}

			if (blockState.get(SOUTH) || blockState.get(SOUTH_UP) || blockState.get(SOUTH_DOWN) ) {
				i |= getMask(Direction.SOUTH);
			}

			if (blockState.get(WEST) || blockState.get(WEST_UP) || blockState.get(WEST_DOWN) ) {
				i |= getMask(Direction.WEST);
			}

			return i;
		});
	}
	
	@Override
	protected VoxelShape[] makeShapes(float nodeWidth, float extensionWidth, float postHeight, float base, float beamHeight)
	{
		float f = 8.0F - nodeWidth;
		float f1 = 8.0F + nodeWidth;
		float f2 = 8.0F - extensionWidth;
		float f3 = 8.0F + extensionWidth;
		VoxelShape voxelshape  = Block.makeCuboidShape((double)f,	0.0D - 8.0D,		 (double)f,  (double)f1, (double)postHeight - 8.0D, (double)f1);
		VoxelShape voxelshape1 = Block.makeCuboidShape((double)f2,	(double)base - 8.0D, 0.0D,		 (double)f3, (double)beamHeight - 8.0D, (double)f3);
		VoxelShape voxelshape2 = Block.makeCuboidShape((double)f2,	(double)base - 8.0D, (double)f2, (double)f3, (double)beamHeight - 8.0D, 16.0D);
		VoxelShape voxelshape3 = Block.makeCuboidShape(0.0D,		(double)base - 8.0D, (double)f2, (double)f3, (double)beamHeight - 8.0D, (double)f3);
		VoxelShape voxelshape4 = Block.makeCuboidShape((double)f2,	(double)base - 8.0D, (double)f2, 16.0D,		 (double)beamHeight - 8.0D, (double)f3);
		VoxelShape voxelshape5 = VoxelShapes.or(voxelshape1, voxelshape4);
		VoxelShape voxelshape6 = VoxelShapes.or(voxelshape2, voxelshape3);
		VoxelShape[] avoxelshape = new VoxelShape[]{VoxelShapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1, VoxelShapes.or(voxelshape2, voxelshape1), VoxelShapes.or(voxelshape3, voxelshape1), VoxelShapes.or(voxelshape6, voxelshape1), voxelshape4, VoxelShapes.or(voxelshape2, voxelshape4), VoxelShapes.or(voxelshape3, voxelshape4), VoxelShapes.or(voxelshape6, voxelshape4), voxelshape5, VoxelShapes.or(voxelshape2, voxelshape5), VoxelShapes.or(voxelshape3, voxelshape5), VoxelShapes.or(voxelshape6, voxelshape5)};

		for(int i = 0; i < 16; ++i)
		{
			avoxelshape[i] = VoxelShapes.or(voxelshape, avoxelshape[i]);
		}

		return avoxelshape;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(parrentBlock.asItem());
	}
}
