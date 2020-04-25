package com.endlesnights.naturalslabsmod.blocks.slabs;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.placehandler.SlabHelper;
import com.endlesnights.naturalslabsmod.util.SlabAction;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=NaturalSlabsMod.MODID, bus=Bus.MOD, value=Dist.CLIENT)
public class BlockCoarseDirtSlab extends SlabBlock implements IWaterLoggable
{
	
	private static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	
	public BlockCoarseDirtSlab()
	{
		super(Block.Properties.from(Blocks.COARSE_DIRT));
		//super(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).tickRandomly().hardnessAndResistance(0.5F).sound(SoundType.GROUND));
		this.setDefaultState(this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE));		
	}
	
//	@Override //Random Tick
//	public void func_225534_a_(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random)
//	{
//		super.func_225534_a_(blockState, world, blockPos, random);
//	}
	
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
				return VoxelShapes.fullCube();
			case TOP:
				return BlockCoarseDirtSlab.TOP_SHAPE;
			default:
				return BlockCoarseDirtSlab.BOTTOM_SHAPE;
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
				&&(blockstate.getBlock() == ModBlocks.block_farmland_slab
				|| blockstate.getBlock() == ModBlocks.block_grass_slab
				|| blockstate.getBlock() == ModBlocks.block_path_slab
				|| blockstate.getBlock() == ModBlocks.block_dirt_slab))
		{
			return Blocks.COARSE_DIRT.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_farmland_slab)
		{
			int moistLevel = blockstate.get(BlockFarmlandSlab.MOISTURE);
			return Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, Integer.valueOf(moistLevel));
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_grass_slab)
		{
			return Blocks.GRASS_BLOCK.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_path_slab)
		{
			return Blocks.GRASS_PATH.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_dirt_slab)
		{
			return Blocks.DIRT.getDefaultState();
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
	    		  || itemstack.getItem() == ModBlocks.block_path_slab.asItem()
	    		  || itemstack.getItem() == ModBlocks.block_grass_slab.asItem()
	    		  || itemstack.getItem() == ModBlocks.block_farmland_slab.asItem()
	    		  || itemstack.getItem() == ModBlocks.block_dirt_slab.asItem()))
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
	
    @Override //TODO, half SLAB FIX!
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
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
    	else if(plant.getBlock() == Blocks.BAMBOO_SAPLING  || plant.getBlock() == Blocks.BAMBOO )
    	{
    		return true;
    	}

    	return super.canSustainPlant(Blocks.COARSE_DIRT.getDefaultState(), world, pos, facing, plantable);
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
    	
		ItemStack itemStack = playerIn.getHeldItem(hand);
		
		if (itemStack.getItem() instanceof HoeItem )
        	return onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
		else if (itemStack.getItem() instanceof ShovelItem)
			return SlabAction.onItemUseSpade(state, worldIn, pos, playerIn, hand, p_225533_6_);

			   
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
		    BlockState blockstate = ModBlocks.block_dirt_slab.getDefaultState().with(SlabBlock.TYPE, state.get(SlabBlock.TYPE)).with(SlabBlock.WATERLOGGED, state.get(SlabBlock.WATERLOGGED));
		    
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
    
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		
		if(stateIn.get(TYPE) == SlabType.BOTTOM )
		{
			BlockState stateUp = worldIn.getBlockState(currentPos.up());
			SlabHelper.slabCheck(worldIn,currentPos);
			
//			if(SlabHelper.slabBottom(stateUp) != stateUp)
//				worldIn.setBlockState(currentPos.up(), SlabHelper.slabBottom(stateUp), 0);
		}
		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
}
