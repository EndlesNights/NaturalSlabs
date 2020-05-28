package com.endlesnights.naturalslabsmod.blocks.slabs;

import java.util.Random;

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
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=NaturalSlabsMod.MODID, bus=Bus.MOD, value=Dist.CLIENT)
public class BlockDirtSlab extends SlabBlock implements IWaterLoggable
{
	
	private static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	
	public BlockDirtSlab()
	{
		super(Block.Properties.from(Blocks.DIRT).tickRandomly());
		//super(Block.Properties.create(Material.EARTH, MaterialColor.DIRT).tickRandomly().hardnessAndResistance(0.5F).sound(SoundType.GROUND));
		this.setDefaultState(this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE));		
	}
	
	@SuppressWarnings("deprecation")
	@Override //Random Tick
	public void tick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random)
	{
		super.tick(blockState, world, blockPos, random);
		SpreadableSnowyDirtBlock(blockState, world, blockPos, random);
	}
	
	private void SpreadableSnowyDirtBlock(BlockState state, ServerWorld world, BlockPos blockPos, Random random)
	{
		if (!func_220257_b(state, world, blockPos))
		{
			return;
		}
		else
		{
			if (world.getLight(blockPos.up()) >= 9)
			{
				BlockState blockstate = this.getDefaultState();
					
				for(int i = 0; i < 4; ++i)
				{
					BlockPos blockpos = blockPos.add(random.nextInt(3) - 1, 3 - random.nextInt(5), random.nextInt(3) - 1);
					if (world.getBlockState(blockpos).getBlock() == Blocks.GRASS_BLOCK && func_220256_c(blockstate, world, blockpos))
					{
						if(world.getBlockState(blockPos).get(SlabBlock.TYPE) == SlabType.TOP
								&& !(world.getBlockState(blockPos.up()).getFluidState() == Fluids.WATER))
						{
							blockstate = ModBlocks.block_grass_slab.getDefaultState()// TODO set up a check for Block.Dirt and different DirtSlab
									.with(SlabBlock.TYPE, SlabType.TOP)
									.with(WATERLOGGED, state.get(WATERLOGGED));
							
							world.setBlockState(blockPos, blockstate);
						}
						else if (world.getBlockState(blockPos).get(SlabBlock.TYPE) == SlabType.BOTTOM
								&& !world.getBlockState(blockPos).get(WATERLOGGED))
						{
							blockstate = ModBlocks.block_grass_slab.getDefaultState() // TODO set up a check for Block.Dirt and different DirtSlab
									.with(WATERLOGGED, state.get(WATERLOGGED));
							world.setBlockState(blockPos, blockstate);
						}
					}
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
			int i = LightEngine.func_215613_a(reader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(reader, blockpos));
			
			if(state.getBlock() == ModBlocks.block_dirt_slab && state.get(SlabBlock.TYPE) == SlabType.TOP)
			{
				
				i = LightEngine.func_215613_a(reader, Blocks.GRASS_BLOCK.getDefaultState(), pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(reader, blockpos));
			}
			
			return i < reader.getMaxLightLevel();
		}
	}

	
	private static boolean func_220256_c(BlockState p_220256_0_, IWorldReader p_220256_1_, BlockPos p_220256_2_)
	{
		BlockPos blockpos = p_220256_2_.up();
		
		
		return func_220257_b(p_220256_0_, p_220256_1_, p_220256_2_) && !p_220256_1_.getFluidState(blockpos).isTagged(FluidTags.WATER);
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
				return VoxelShapes.fullCube();
			case TOP:
				return BlockDirtSlab.TOP_SHAPE;
			default:
				return BlockDirtSlab.BOTTOM_SHAPE;
		}
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		//Block block = context.getWorld().getBlockState(context.getPos().up()).getBlock();
		//return this.getDefaultState().with(SNOWY, Boolean.valueOf(block == Blocks.SNOW_BLOCK || block == Blocks.SNOW));
		BlockPos blockpos = context.getPos();
		BlockState blockstate = context.getWorld().getBlockState(blockpos);
						
		SlabType slabtype = null;
		if(blockstate.getProperties().contains(SlabBlock.TYPE))
			slabtype = blockstate.get(SlabBlock.TYPE);
		
		if (blockstate.getBlock() == this || slabtype == SlabType.BOTTOM
				&&(blockstate.getBlock() == ModBlocks.block_farmland_slab
				|| blockstate.getBlock() == ModBlocks.block_grass_slab
				|| blockstate.getBlock() == ModBlocks.block_path_slab
				|| blockstate.getBlock() == ModBlocks.block_coarse_dirt_slab))
		{
			return Blocks.DIRT.getDefaultState();
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
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext)
	{
		
	      ItemStack itemstack = useContext.getItem();
	      SlabType slabtype = state.get(TYPE);
	      if (slabtype != SlabType.DOUBLE && (itemstack.getItem() == this.asItem() 
	    		  || itemstack.getItem() == ModBlocks.block_path_slab.asItem()
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

    	return super.canSustainPlant(Blocks.DIRT.getDefaultState(), world, pos, facing, plantable);
    }
    
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
    	
		ItemStack itemStack = playerIn.getHeldItem(hand);
		
		if (itemStack.getItem() instanceof HoeItem )
        	return SlabAction.onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
		else if (itemStack.getItem() instanceof ShovelItem)
			return SlabAction.onItemUseSpade(state, worldIn, pos, playerIn, hand, p_225533_6_);

        return ActionResultType.FAIL;
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
