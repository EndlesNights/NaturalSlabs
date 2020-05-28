package com.endlesnights.naturalslabsmod.blocks.slabs;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.placehandler.SlabHelper;
import com.endlesnights.naturalslabsmod.util.SlabAction;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.renderer.color.IBlockColor;
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
import net.minecraft.state.properties.Half;
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
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;


public class BlockGrassSlab extends SlabBlock implements IWaterLoggable, IGrowable
{
	
	private static final VoxelShape BOTTOM_SHAPE = Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D);
	private static final VoxelShape TOP_SHAPE = Block.makeCuboidShape(0.0D, 8.0D, 0.0D, 16.0D, 16.0D, 16.0D);
	private static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
	
	public static final BooleanProperty SNOWY = BlockStateProperties.SNOWY;

	public BlockGrassSlab()
	{
		super(Block.Properties.from(Blocks.GRASS_BLOCK));
		//super(Block.Properties.create(Material.ORGANIC).tickRandomly().hardnessAndResistance(0.6F).sound(SoundType.PLANT));
		this.setDefaultState(this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, Boolean.FALSE).with(SNOWY, Boolean.FALSE));
	}
	
	@Override //Random Tick
	public void tick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random)
	{
		super.tick(blockState, world, blockPos, random);//Supertick
		SpreadableSnowyDirtBlock(blockState, world, blockPos, random);
	}
	
	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) 
	{
		builder.add(SlabBlock.TYPE, WATERLOGGED, SNOWY);
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
				return BlockGrassSlab.TOP_SHAPE;
			default:
				return BlockGrassSlab.BOTTOM_SHAPE;
		}
	}
	
	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		Block block = context.getWorld().getBlockState(context.getPos().up()).getBlock();
		//return this.getDefaultState().with(SNOWY, Boolean.valueOf(block == Blocks.SNOW_BLOCK || block == Blocks.SNOW));
		
		BlockPos blockpos = context.getPos();
		BlockState blockstate = context.getWorld().getBlockState(blockpos);
		
		SlabType slabtype = null;
		if(blockstate.getProperties().contains(SlabBlock.TYPE))
			slabtype = blockstate.get(SlabBlock.TYPE);
		
		if (blockstate.getBlock() == this || slabtype == SlabType.BOTTOM
				&&(blockstate.getBlock() == ModBlocks.block_farmland_slab
				|| blockstate.getBlock() == ModBlocks.block_dirt_slab
				|| blockstate.getBlock() == ModBlocks.block_path_slab
				|| blockstate.getBlock() == ModBlocks.block_coarse_dirt_slab))
		{
			return Blocks.GRASS_BLOCK.getDefaultState().with(SNOWY, Boolean.valueOf(block == Blocks.SNOW_BLOCK || block == Blocks.SNOW));
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_dirt_slab)
		{
			return Blocks.DIRT.getDefaultState();
		}
		else if (slabtype == SlabType.TOP && blockstate.getBlock() == ModBlocks.block_farmland_slab)
		{
			int moistLevel = blockstate.get(BlockFarmlandSlab.MOISTURE);
			return Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, Integer.valueOf(moistLevel));
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
			BlockState blockstate1 = this.getDefaultState().with(SlabBlock.TYPE, SlabType.BOTTOM).with(WATERLOGGED, ifluidstate.getFluid() == Fluids.WATER).with(SNOWY, Boolean.valueOf(block == Blocks.SNOW_BLOCK || block == Blocks.SNOW));
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
	    		  || itemstack.getItem() == ModBlocks.block_dirt_slab.asItem()
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
	
	public static class ColorHandler implements IBlockColor 
	{
		public int getColor(BlockState state, @Nullable ILightReader reader , @Nullable BlockPos blockPos, int tintIndex)
		{
			// TODO Auto-generated method stub
			if(reader != null && blockPos != null)
			{
				return BiomeColors.getGrassColor(reader, blockPos);
				//return reader.getBiome(blockPos).getGrassColor(blockPos);
			}
			return GrassColors.get(0.5D, 1.0D);
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
	    		else if(world.getFluidState(pos.add(0,waterLevelMin,-1)).isTagged(FluidTags.WATER))
	    			return true;
	    		waterLevelMin++;
	    	}
    		
    		return false;
    	}
    	else if(plant.getBlock() == Blocks.BAMBOO_SAPLING  || plant.getBlock() == Blocks.BAMBOO )
    	{
    		return true;
    	}
    	
//    	if(state.get(SlabBlock.TYPE) == SlabType.BOTTOM)
//    	{
//    		ResourceLocation tagId = new ResourceLocation(NaturalSlabsMod.MODID, "block_grass_slab_bottom");
//    		boolean isPlantableOnGrass = BlockTags.getCollection().getOrCreate(tagId).contains((Block) plantable);
//    		return isPlantableOnGrass;
//    	}
//    	else
//    		return super.canSustainPlant(Blocks.GRASS_BLOCK.getDefaultState(), world, pos, facing, plantable);
    	return super.canSustainPlant(Blocks.GRASS_BLOCK.getDefaultState(), world, pos, facing, plantable);
    }
    
	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		return worldIn.getBlockState(pos.up()).isAir();
	}


	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		return true;
	}


	@Override //Grass & Flower Growing Function!
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState blockState)
	{
		// TODO Auto-generated method stub
	      BlockPos blockpos = pos.up();
	      BlockState blockstate = Blocks.GRASS.getDefaultState();

	      for(int i = 0; i < 128; ++i) {
	         BlockPos blockpos1 = blockpos;
	         int j = 0;

	         while(true) {
	            if (j >= i / 16)
	            {
	               BlockState blockstate2 = worldIn.getBlockState(blockpos1);
	               
	               if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0)
	               {

	                  ((IGrowable)blockstate.getBlock()).grow(worldIn, rand, blockpos1, blockstate2);
	               }

	               if (!blockstate2.isAir()) {
	                  break;
	               }

	               BlockState blockstate1;
	               if (rand.nextInt(8) == 0) {
	                  List<ConfiguredFeature<?, ?>> list = worldIn.getBiome(blockpos1).getFlowers();
	                  if (list.isEmpty()) {
	                     break;
	                  }

	                  ConfiguredFeature<?, ?> configuredfeature = ((DecoratedFeatureConfig)(list.get(0)).config).feature;
	                  blockstate1 = ((FlowersFeature)configuredfeature.feature).getFlowerToPlace(rand, blockpos1, configuredfeature.config);
	               } else {
	                  blockstate1 = blockstate;
	               }

	               if (blockstate1.isValidPosition(worldIn, blockpos1)) {
	                  worldIn.setBlockState(blockpos1, blockstate1, 3);
	               }
	               break;
	            }

	            blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
	            if (worldIn.getBlockState(blockpos1.down()).getBlock() != this
	            		&& worldIn.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS_BLOCK.getBlock()
	            		&& (worldIn.getBlockState(blockpos1.down()).getBlock() != ModBlocks.block_grass_stairs.getBlock() && worldIn.getBlockState(blockpos1.down()).get(StairsBlock.HALF) == Half.TOP)
	            		|| worldIn.getBlockState(blockpos1).isCollisionShapeOpaque(worldIn, blockpos1))
	            {
	            	
	               break;
	            }

	            ++j;
	         }
	      }
		
	}
	
	private void SpreadableSnowyDirtBlock(BlockState state, ServerWorld world, BlockPos blockPos, Random random)
	{
		if (!func_220257_b(state, world, blockPos))
		{
			if (!world.isAreaLoaded(blockPos, 3) || (state.getBlock() == ModBlocks.block_grass_slab && state.get(SlabBlock.TYPE) == SlabType.BOTTOM) )
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			
			if(world.getBlockState(blockPos) == this.getDefaultState())
				world.setBlockState(blockPos, ModBlocks.block_dirt_slab.getDefaultState());
			else if(world.getBlockState(blockPos) == this.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP))
				world.setBlockState(blockPos, ModBlocks.block_dirt_slab.getDefaultState().with(SlabBlock.TYPE, SlabType.TOP));
			
			//world.setBlockState(blockPos, Blocks.DIRT.getDefaultState()); //TODO add check to change dirt block to the correct Slab
		}
		else
		{
			if (world.getLight(blockPos.up()) >= 9 )
			{
				if(state.get(TYPE) == SlabType.BOTTOM && (state.get(WATERLOGGED) == false))
					return;
				
				BlockState blockstate = this.getDefaultState();
					
				for(int i = 0; i < 4; ++i)
				{
					BlockPos blockpos = blockPos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					if (world.getBlockState(blockpos).getBlock() == Blocks.DIRT && func_220256_c(blockstate, world, blockpos))
					{
						blockstate = Blocks.GRASS_BLOCK.getDefaultState(); // TODO set up a check for Block.Dirt and different DirtSlab
						world.setBlockState(blockpos, blockstate.with(SNOWY, Boolean.valueOf(world.getBlockState(blockpos.up()).getBlock() == Blocks.SNOW)));
					}
					else if(world.getBlockState(blockpos).getBlock() == ModBlocks.block_dirt_slab && func_220256_c(blockstate, world, blockpos))
					{
						if(world.getBlockState(blockpos).get(SlabBlock.TYPE) == SlabType.TOP)
						{
							blockstate = ModBlocks.block_grass_slab.getDefaultState() // TODO set up a check for Block.Dirt and different DirtSlab
									.with(SlabBlock.TYPE, SlabType.TOP)
									.with(WATERLOGGED, world.getBlockState(blockpos).get(WATERLOGGED)); 
							world.setBlockState(blockpos, blockstate);
						}
						else if (world.getBlockState(blockpos).get(SlabBlock.TYPE) == SlabType.BOTTOM)
						{
							blockstate = ModBlocks.block_grass_slab.getDefaultState() // TODO set up a check for Block.Dirt and different DirtSlab
									.with(WATERLOGGED, world.getBlockState(blockpos).get(WATERLOGGED)); 
							world.setBlockState(blockpos, blockstate);
						}
					}
				}
			}
		}
	}
	
	//SpradableSnowyDirtBlock
	private static boolean func_220257_b(BlockState state, IWorldReader reader, BlockPos pos)
	{
		BlockPos blockpos = pos.up();
		BlockState blockstate = reader.getBlockState(blockpos);
				
		if (blockstate.getBlock() == ModBlocks.block_snow_slab && blockstate.get(BlockSnowSlab.LAYERS) == 1)
		{
			return true;
		}
		else
		{	
			int i = LightEngine.func_215613_a(reader, state, pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(reader, blockpos));
			
			if(state.getBlock() == ModBlocks.block_grass_slab && state.get(SlabBlock.TYPE) == SlabType.TOP)
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
	
	//SnowyDirtBlock
	
	@Override
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos)
	{
		
		if(stateIn.get(TYPE) == SlabType.BOTTOM )
		{
			SlabHelper.slabCheck(worldIn,currentPos);
		}
		
		if (facing != Direction.UP)
		{
			return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
		}
		else
		{
			Block block = facingState.getBlock();
			return stateIn.with(SNOWY, Boolean.valueOf(block == Blocks.SNOW_BLOCK || block == Blocks.SNOW));
		}
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
}
