package com.endlesnights.naturalslabsmod.blocks.slabs;

import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.util.SlabAction;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.FenceGateBlock;

public class BlockPathSlab extends SlabBlock implements IWaterLoggable
{
	
	private static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	private static final VoxelShape DOUBLE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public BlockPathSlab()
	{
		super(Block.Properties.from(Blocks.GRASS_PATH));
		//super(Block.Properties.create(Material.EARTH).hardnessAndResistance(0.65F).sound(SoundType.PLANT));
		this.setDefaultState(this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE));
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
	{
		builder.add(SlabBlock.TYPE, WATERLOGGED);
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return state.get(SlabBlock.TYPE) != SlabType.DOUBLE;
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		SlabType slabtype = state.get(SlabBlock.TYPE);
		switch (slabtype)
		{
			case DOUBLE:
				return BlockPathSlab.DOUBLE_SHAPE;
			case TOP:
				return BlockPathSlab.TOP_SHAPE;
			default:
				return BlockPathSlab.BOTTOM_SHAPE;
		}
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{

		BlockPos blockpos = context.getPos();
		BlockState blockstate = context.getWorld().getBlockState(blockpos);
		
		SlabType slabtype = null;
		if(blockstate.getProperties().contains(SlabBlock.TYPE))
			slabtype = blockstate.get(SlabBlock.TYPE);
		
		if (blockstate.getBlock() == this || slabtype == SlabType.BOTTOM
				&&(blockstate.getBlock() == ModBlocks.block_dirt_slab
				|| blockstate.getBlock() == ModBlocks.block_grass_slab
				|| blockstate.getBlock() == ModBlocks.block_farmland_slab
				|| blockstate.getBlock() == ModBlocks.block_coarse_dirt_slab))
		{
			return Blocks.GRASS_PATH.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_dirt_slab)
		{
			return Blocks.DIRT.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_grass_slab)
		{
			return Blocks.GRASS_BLOCK.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_farmland_slab)
		{
			int moistLevel = blockstate.get(BlockFarmlandSlab.MOISTURE);
			return Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, Integer.valueOf(moistLevel));
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_coarse_dirt_slab)
		{
			return Blocks.COARSE_DIRT.getDefaultState();
		}
		else
		{
			IFluidState ifluidstate = context.getWorld().getFluidState(blockpos);
			BlockState blockstate1 = this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
			Direction direction = context.getFace();
			return direction != Direction.DOWN && (direction == Direction.UP || !(context.getHitVec().y - (double) blockpos.getY() > 0.5D)) ? blockstate1 : blockstate1.with(SlabBlock.TYPE, SlabType.TOP);
		}
	}
	
	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		
	      ItemStack itemstack = useContext.getItem();
	      SlabType slabtype = state.get(TYPE);
	      if (slabtype != SlabType.DOUBLE && (itemstack.getItem() == this.asItem() 
	    		  || itemstack.getItem() == ModBlocks.block_dirt_slab.asItem()
	    		  || itemstack.getItem() == ModBlocks.block_grass_slab.asItem()
	    		  || itemstack.getItem() == ModBlocks.block_farmland_slab.asItem()
	    		  || itemstack.getItem() == ModBlocks.block_coarse_dirt_slab.asItem()))
	      {
	         if (useContext.replacingClickedOnBlock())
	         {
	        	 
	            boolean flag = useContext.getHitVec().y - (double)useContext.getPos().getY() > 0.5D;
	            Direction direction = useContext.getFace();
	            if (slabtype == SlabType.BOTTOM) {
	               return direction == Direction.UP || flag && direction.getAxis().isHorizontal();
	            } else {
	               return direction == Direction.DOWN || !flag && direction.getAxis().isHorizontal();
	            }
	         } else {
	            return true;
	         }
	      } else {
	         return false;
	      }
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
		BlockState blockstate = worldIn.getBlockState(pos.up());
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock;
	}
	
	public boolean allowsMovement(BlockState state, IBlockReader worldIn, BlockPos pos, PathType type) {
		return false;
	}

	@OnlyIn(Dist.CLIENT)
	public boolean func_229870_f_(BlockState p_229870_1_, IBlockReader p_229870_2_, BlockPos p_229870_3_) {
		return true;
	}
	
	@Override //
	public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random p_225534_4_) 
	{
		BlockState stateTo = Blocks.DIRT.getDefaultState();
		if(state.get(SlabBlock.TYPE) == SlabType.BOTTOM)
			stateTo = ModBlocks.block_dirt_slab.getDefaultState();
		else if(state.get(SlabBlock.TYPE) == SlabType.TOP)
			stateTo = ModBlocks.block_dirt_slab.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP);
		
		worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, Blocks.DIRT.getDefaultState(), worldIn, pos));
		worldIn.setBlockState(pos, stateTo);
	}
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (facing == Direction.UP && !stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
    	
		ItemStack itemStack = playerIn.getHeldItem(hand);
	
		if (itemStack.getItem() instanceof HoeItem )
        	return SlabAction.onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
			   
        return ActionResultType.FAIL;
        
    }
}
