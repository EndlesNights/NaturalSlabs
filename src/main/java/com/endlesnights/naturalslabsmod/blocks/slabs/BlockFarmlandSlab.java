package com.endlesnights.naturalslabsmod.blocks.slabs;

import java.util.Random;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.placehandler.SlabHelper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.MovingPistonBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public class BlockFarmlandSlab extends SlabBlock implements IWaterLoggable
{
	private static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	private static final VoxelShape DOUBLE_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 15.0D, 16.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final IntegerProperty MOISTURE = BlockStateProperties.MOISTURE_0_7;
	
	public BlockFarmlandSlab()
	{
		super(Block.Properties.from(Blocks.FARMLAND));
		this.setDefaultState(this.stateContainer.getBaseState()
				.with(SlabBlock.TYPE, SlabType.BOTTOM)
				.with(WATERLOGGED, Boolean.FALSE)
				.with(MOISTURE, Integer.valueOf(0)));
	}
	
	
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
	{
		builder.add(SlabBlock.TYPE, WATERLOGGED, MOISTURE);
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
				return BlockFarmlandSlab.DOUBLE_SHAPE;
			case TOP:
				return BlockFarmlandSlab.TOP_SHAPE;
			default:
				return BlockFarmlandSlab.BOTTOM_SHAPE;
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
				|| blockstate.getBlock() == ModBlocks.block_path_slab
				|| blockstate.getBlock() == ModBlocks.block_coarse_dirt_slab))
		{
			return Blocks.FARMLAND.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_dirt_slab)
		{
			return Blocks.DIRT.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_grass_slab)
		{
			return Blocks.GRASS_BLOCK.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_path_slab)
		{
			return Blocks.GRASS_PATH.getDefaultState();
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
	    		  || itemstack.getItem() == ModBlocks.block_path_slab.asItem()
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
		return !blockstate.getMaterial().isSolid() || blockstate.getBlock() instanceof FenceGateBlock || blockstate.getBlock() instanceof MovingPistonBlock;
	}
	
	@Override
	public void tick(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
		if (!p_225534_1_.isValidPosition(p_225534_2_, p_225534_3_)) {
			turnToDirt(p_225534_1_, p_225534_2_, p_225534_3_);
		} else {
			int i = p_225534_1_.get(MOISTURE);
			if (!hasWater(p_225534_2_, p_225534_3_, p_225534_1_) && !p_225534_2_.isRainingAt(p_225534_3_.up())) {
				if (i > 0) {
					p_225534_2_.setBlockState(p_225534_3_, p_225534_1_.with(MOISTURE, Integer.valueOf(i - 1)), 2);
				} else if (!hasCrops(p_225534_2_, p_225534_3_)) {
					turnToDirt(p_225534_1_, p_225534_2_, p_225534_3_);
				}
			} else if (i < 7) {
				p_225534_2_.setBlockState(p_225534_3_, p_225534_1_.with(MOISTURE, Integer.valueOf(7)), 2);
			}

		}
	}
	
	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		if (!worldIn.isRemote && net.minecraftforge.common.ForgeHooks.onFarmlandTrample(worldIn, pos, Blocks.DIRT.getDefaultState(), fallDistance, entityIn)) { // Forge: Move logic to Entity#canTrample
			turnToDirt(worldIn.getBlockState(pos), worldIn, pos);
		}

		super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
	}
	
	public static void turnToDirt(BlockState state, World worldIn, BlockPos pos)
	{
		//System.out.println(state.get(SlabBlock.TYPE));
		if(state.get(SlabBlock.TYPE) == SlabType.TOP)
			worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, ModBlocks.block_dirt_slab.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)), worldIn, pos));
		else if(state.get(SlabBlock.TYPE) == SlabType.BOTTOM)
		{
			worldIn.setBlockState(pos, ModBlocks.block_dirt_slab.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED)));
		}		

	}
	
	private boolean hasCrops(IBlockReader worldIn, BlockPos pos) {
		BlockState state = worldIn.getBlockState(pos.up());
		return state.getBlock() instanceof net.minecraftforge.common.IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable)state.getBlock());
	}
	
	private static boolean hasWater(IWorldReader worldIn, BlockPos pos, BlockState state) 
	{
		int waterLevelMin = 0; //Measures the lowest level of water, default 0 for blocks
		
		if(state.getProperties().contains(SlabBlock.TYPE) && state.get(SlabBlock.TYPE) == SlabType.BOTTOM)
			waterLevelMin = -1;
		
		for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, waterLevelMin, -4), pos.add(4, 1, 4))) {
			if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
				return true;
			}
		}

		return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
	}
	
    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, net.minecraftforge.common.IPlantable plantable)
    {
//		PlantType type = plantable.getPlantType(world, pos.offset(facing));
//	
//		ResourceLocation tagId = new ResourceLocation(NaturalSlabsMod.MODID, "block_farmland_slab_top");
//		boolean isPlantableOnGrass = BlockTags.getCollection().getOrCreate(tagId).contains((Block) plantable);
//		return isPlantableOnGrass || type == PlantType.Crop || super.canSustainPlant(state, world, pos, facing, plantable);
    	return super.canSustainPlant(Blocks.FARMLAND.getDefaultState(), world, pos, facing, plantable);
    }
    
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		
		if(stateIn.get(TYPE) == SlabType.BOTTOM )
		{
			SlabHelper.slabCheck(worldIn,currentPos);
//			BlockState stateUp = worldIn.getBlockState(currentPos.up());
//			if(SlabHelper.slabBottom(stateUp) != stateUp)
//				worldIn.setBlockState(currentPos.up(), SlabHelper.slabBottom(stateUp), 0);
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}
