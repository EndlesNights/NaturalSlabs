package com.endlesnights.naturalslabsmod.blocks.slabs;

import java.util.Random;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.entities.FallingSlabEntity;
import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.material.Material;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.IPlantable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class FallinglSlab extends SlabBlock
{
	private static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	public static final EnumProperty<SlabType> TYPE = BlockStateProperties.SLAB_TYPE;
	
	public final int DustColor;
	public final Block ParentBlock;
	
	public FallinglSlab(Properties properties, int dust, Block parentBlock)
	{
		super(properties);
		this.DustColor = dust;
		this.ParentBlock = parentBlock;
		this.setDefaultState(this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
	{
		builder.add(SlabBlock.TYPE, WATERLOGGED);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return BOTTOM_SHAPE;	
	}
	
    @Override //TODO, half SLAB FIX!
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
    {
    	
    	if(ParentBlock == Blocks.SAND || ParentBlock == Blocks.RED_SAND)
    	{
    		BlockState plant = plantable.getPlant(world, pos.offset(facing));
    		if (plant.getBlock() == Blocks.SUGAR_CANE || plant.getBlock() == ModBlocks.sugar_cane_slab) //Sugarcane was not working with super grass, placed it in on my own
        	{
    			if((state.get(SlabBlock.TYPE) == SlabType.BOTTOM && plant.getBlock() == Blocks.SUGAR_CANE)
    					|| (state.get(SlabBlock.TYPE) == SlabType.TOP && plant.getBlock() == ModBlocks.sugar_cane_slab))
    				return false;

    			
        		int waterLevelMin = 0; //Measures the lowest level of water, default 0 for blocks
        		
        		if(state.getProperties().contains(SlabBlock.TYPE) && state.get(SlabBlock.TYPE) == SlabType.BOTTOM)
        			waterLevelMin = -1;
        		
        		if(world.getFluidState(pos).isTagged(FluidTags.WATER))
        			if(state.get(SlabBlock.TYPE) == SlabType.TOP)
        				return true;
        		

    	    	while(waterLevelMin < 1)
    	    	{
    	    		if(world.getFluidState(pos.add(1,waterLevelMin,0)).isTagged(FluidTags.WATER))
    	    			return true;
    	    		else if(world.getFluidState(pos.add(-1,waterLevelMin,0)).isTagged(FluidTags.WATER))
    	    			return true;
    	    		else if(world.getFluidState(pos.add(0,waterLevelMin,1)).isTagged(FluidTags.WATER))
    	    			return true;
    	    		else if(world.getFluidState(pos.add(1,waterLevelMin,-1)).isTagged(FluidTags.WATER))
    	    			return true;
    	    		waterLevelMin++;
    	    	}
        		
        		return true;
        	}
    	}
    	
    	
    	return false;
    }
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockPos blockpos = context.getPos();
		BlockState blockstate = context.getWorld().getBlockState(blockpos);
		
		if (blockstate.getBlock() == this)
		{
			return ParentBlock.getDefaultState();
		}
		
		
		IFluidState ifluidstate = context.getWorld().getFluidState(blockpos);
		return this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER);
	}
	
	@Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext)
	{
		BlockPos pos = useContext.getPos();
		BlockState target = useContext.getWorld().getBlockState(pos.up());
		ItemStack itemstack = useContext.getItem();
		
		if(itemstack.getItem() == this.asItem())
		{
			return true;
		}
		else if(itemstack.getItem() == ParentBlock.asItem() && target.getBlock().isReplaceable(target, useContext))
		{
			useContext.getWorld().setBlockState(pos.up(), this.getDefaultState().with(WATERLOGGED, target.getBlock() == Blocks.WATER ));
			return true;
		}
		return false;
	}

	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving)
	{
		worldIn.getPendingBlockTicks().scheduleTick(pos, this, this.tickRate(worldIn));
	}
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		if(worldIn.getBlockState(currentPos.up()).getBlock() == this)
		{
			worldIn.setBlockState(currentPos.up(),Blocks.AIR.getDefaultState() ,3);
			
			return ParentBlock.getDefaultState();
		}
		
		worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, this.tickRate(worldIn));
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}

	   public void func_225534_a_(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_) {
		      if (p_225534_2_.isAirBlock(p_225534_3_.down()) || canFallThrough(p_225534_2_.getBlockState(p_225534_3_.down())) && p_225534_3_.getY() >= 0) {
		    	  FallingSlabEntity fallingSlabEntity = new FallingSlabEntity(p_225534_2_, (double)p_225534_3_.getX() + 0.5D, (double)p_225534_3_.getY(), (double)p_225534_3_.getZ() + 0.5D, p_225534_2_.getBlockState(p_225534_3_));
		         this.onStartFalling(fallingSlabEntity);
		         p_225534_2_.addEntity(fallingSlabEntity);
		      }
		   }

	protected void onStartFalling(FallingSlabEntity fallingEntity) {
	}

		   /**
		    * How many world ticks before ticking
		    */
	public int tickRate(IWorldReader worldIn) {
		return 2;
	}

	public static boolean canFallThrough(BlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return state.isAir() || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
	}

	public void onEndFalling(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState)
	{

	}

	public void breakFall(World worldIn, BlockPos pos, BlockState fallingState, BlockState hitState, FallingSlabEntity entity)
	{
		if(worldIn.getBlockState(pos).getBlock() == this)
			worldIn.setBlockState(pos,ParentBlock.getDefaultState() ,3);
		else
			entity.entityDropItem(this.asItem());
			
	}
	
	public void onBroken(World worldIn, BlockPos pos){
	}
		   
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if (rand.nextInt(16) == 0)
		{
			BlockPos blockpos = pos.down();
			if (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos)))
			{
				double d0 = (double)pos.getX() + (double)rand.nextFloat();
				double d1 = (double)pos.getY() - 0.05D;
				double d2 = (double)pos.getZ() + (double)rand.nextFloat();
				worldIn.addParticle(new BlockParticleData(ParticleTypes.FALLING_DUST, stateIn), d0, d1, d2, 0.0D, 0.0D, 0.0D);
			}
		}

	}

	@OnlyIn(Dist.CLIENT)
	public int getDustColor(BlockState state) {
		return DustColor; //;
	}
}
