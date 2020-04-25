package com.endlesnights.naturalslabsmod.blocks.foliage;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class DoublePlantSlab extends DoublePlantBlock
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 24.0D, 16.0D);
	protected static final VoxelShape SHAPE_TOP = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 0.0D, 16.0D);
	
    public DoublePlantSlab(Block.Properties properties)
    {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(HALF, DoubleBlockHalf.LOWER));
    }

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		if (state.get(HALF) == DoubleBlockHalf.LOWER)
		{
			return (worldIn.getBlockState(pos.offset(Direction.DOWN)).getProperties().contains(SlabBlock.TYPE) 
					&& worldIn.getBlockState(pos.offset(Direction.DOWN)).get(SlabBlock.TYPE) == SlabType.BOTTOM
					&&(worldIn.getBlockState(pos.up()).getBlock() == this && worldIn.getBlockState(pos.up()).get(HALF) == DoubleBlockHalf.UPPER));
		}
		
		return worldIn.getBlockState(pos.down()).getBlock() == this && worldIn.getBlockState(pos.down()).get(HALF) == DoubleBlockHalf.LOWER;
	}
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext)
    {
    	Vec3d vec3d = state.getOffset(worldIn, pos);
    	//return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
    	if (state.get(HALF) == DoubleBlockHalf.LOWER)
    		return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
    	else
    		return SHAPE_TOP.withOffset(vec3d.x, vec3d.y, vec3d.z);
    }
    
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		if (this == ModBlocks.tall_grass_slab)
		{
			return new ItemStack(Items.TALL_GRASS);
		}
		else if (this == ModBlocks.large_fern_slab)
		{
			return new ItemStack(Items.LARGE_FERN);
		}
		
		return new ItemStack(null);
	}
	
	//
	//
	//
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn,
			BlockPos currentPos, BlockPos facingPos) 
	{
		if(isValidPosition(stateIn, worldIn, currentPos))
			return stateIn;
		else
			return Blocks.AIR.getDefaultState();
		
//		DoubleBlockHalf doubleblockhalf = stateIn.get(HALF);
//		if (facing.getAxis() != Direction.Axis.Y || doubleblockhalf == DoubleBlockHalf.LOWER != (facing == Direction.UP) || facingState.getBlock() == this && facingState.get(HALF) != doubleblockhalf) {
//			System.out.println("TEST: 1");
//			//return stateIn;
//			return doubleblockhalf == DoubleBlockHalf.LOWER && facing == Direction.DOWN && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
//		} else {
//			System.out.println("TEST: 2");
//			return Blocks.AIR.getDefaultState();
//		}
	}

	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockPos blockpos = context.getPos();
		return blockpos.getY() < context.getWorld().getDimension().getHeight() - 1 && context.getWorld().getBlockState(blockpos.up()).isReplaceable(context) ? super.getStateForPlacement(context) : null;
		   
	}

		   /**
		    * Called by ItemBlocks after a block is set in the world, to allow post-place logic
		    */
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
			   
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), 3);
	}

	public void placeAt(IWorld worldIn, BlockPos pos, int flags) {
			   
		worldIn.setBlockState(pos, this.getDefaultState().with(HALF, DoubleBlockHalf.LOWER), flags);
		worldIn.setBlockState(pos.up(), this.getDefaultState().with(HALF, DoubleBlockHalf.UPPER), flags);
	}

		   /**
		    * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
		    * Block.removedByPlayer
		    */
	public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		super.harvestBlock(worldIn, player, pos, Blocks.AIR.getDefaultState(), te, stack);
	}

		   /**
		    * Called before the Block is set to air in the world. Called regardless of if the player's tool can actually collect
		    * this block
		    */
	public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleBlockHalf doubleblockhalf = state.get(HALF);
		BlockPos blockpos = doubleblockhalf == DoubleBlockHalf.LOWER ? pos.up() : pos.down();
		BlockState blockstate = worldIn.getBlockState(blockpos);
		if (blockstate.getBlock() == this && blockstate.get(HALF) != doubleblockhalf) {
			worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 35);
			worldIn.playEvent(player, 2001, blockpos, Block.getStateId(blockstate));
			if (!worldIn.isRemote && !player.isCreative()) {
				spawnDrops(state, worldIn, pos, (TileEntity)null, player, player.getHeldItemMainhand());
				spawnDrops(blockstate, worldIn, blockpos, (TileEntity)null, player, player.getHeldItemMainhand());
			}
		}

		super.onBlockHarvested(worldIn, pos, state, player);
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HALF);
	}

		   /**
		    * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
		    */
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.XZ;
	}

		   /**
		    * Return a random long to be passed to {@link IBakedModel#getQuads}, used for random model rotations
		    */
		@OnlyIn(Dist.CLIENT)
		public long getPositionRandom(BlockState state, BlockPos pos) {
			return MathHelper.getCoordinateRandom(pos.getX(), pos.down(state.get(HALF) == DoubleBlockHalf.LOWER ? 0 : 1).getY(), pos.getZ());
		}
}
