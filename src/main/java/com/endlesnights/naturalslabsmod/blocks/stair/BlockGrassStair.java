package com.endlesnights.naturalslabsmod.blocks.stair;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowStairs;
import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.util.StairAction;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.GrassColors;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.ILightReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.lighting.LightEngine;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;

@SuppressWarnings("deprecation")
public class BlockGrassStair extends StairsBlock implements IGrowable
{

	public BlockGrassStair(BlockState state, Properties properties)
	{
		super(state,  properties.tickRandomly());
	}

	@Override
	public void tick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random)
	{
		super.tick(blockState, world, blockPos, random);
		SpreadableSnowyDirtBlock(blockState, world, blockPos, random);
	}
	
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
    	
		ItemStack itemStack = playerIn.getHeldItem(hand);
		
		if (itemStack.getItem() instanceof HoeItem )
        	return StairAction.onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
		else if (itemStack.getItem() instanceof ShovelItem)
			return StairAction.onItemUseSpade(state, worldIn, pos, playerIn, hand, p_225533_6_);

        return ActionResultType.FAIL;
    }
	
	private void SpreadableSnowyDirtBlock(BlockState state, ServerWorld world, BlockPos blockPos, Random random)
	{		
		if (!func_220257_b(state, world, blockPos))
		{
			if (!world.isAreaLoaded(blockPos, 3) || (state.getBlock() == ModBlocks.block_grass_slab && state.get(SlabBlock.TYPE) == SlabType.BOTTOM) )
				return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
			
			world.setBlockState(blockPos, ModBlocks.block_dirt_stairs.getDefaultState()
	    		.with(StairsBlock.FACING, state.get(StairsBlock.FACING))
	    		.with(StairsBlock.HALF, state.get(StairsBlock.HALF))
	    		.with(StairsBlock.SHAPE, state.get(StairsBlock.SHAPE))
	    		.with(StairsBlock.WATERLOGGED, state.get(StairsBlock.WATERLOGGED))
	    		);
			//world.setBlockState(blockPos, Blocks.DIRT.getDefaultState()); //TODO add check to change dirt block to the correct Slab
		}
		else
		{
			if (world.getLight(blockPos.up()) >= 9 && (state.get(WATERLOGGED) == false))
			{
				BlockState blockstate = this.getDefaultState();
					
				for(int i = 0; i < 4; ++i)
				{
					BlockPos blockpos = blockPos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					if (world.getBlockState(blockpos).getBlock() == Blocks.DIRT && func_220256_c(blockstate, world, blockpos))
					{
						blockstate = Blocks.GRASS_BLOCK.getDefaultState(); // TODO set up a check for Block.Dirt and different DirtSlab
						world.setBlockState(blockpos, blockstate);
					}
					else if(world.getBlockState(blockpos).getBlock() == ModBlocks.block_dirt_slab && func_220256_c(blockstate, world, blockpos))
					{
						if(world.getBlockState(blockpos).get(SlabBlock.TYPE) == SlabType.TOP)
						{
							blockstate = ModBlocks.block_grass_slab.getDefaultState()
									.with(SlabBlock.TYPE, SlabType.TOP)
									.with(WATERLOGGED, world.getBlockState(blockpos).get(WATERLOGGED)); 
							world.setBlockState(blockpos, blockstate);
						}
						else if (world.getBlockState(blockpos).get(SlabBlock.TYPE) == SlabType.BOTTOM)
						{
							blockstate = ModBlocks.block_grass_slab.getDefaultState()
									.with(WATERLOGGED, world.getBlockState(blockpos).get(WATERLOGGED));
							world.setBlockState(blockpos, blockstate);
						}
					}
					else if(world.getBlockState(blockpos).getBlock() == ModBlocks.block_dirt_stairs && func_220256_c(blockstate, world, blockpos))
					{
						blockstate = ModBlocks.block_grass_stairs.getDefaultState()
					    		.with(StairsBlock.FACING, world.getBlockState(blockpos).get(StairsBlock.FACING))
					    		.with(StairsBlock.HALF, world.getBlockState(blockpos).get(StairsBlock.HALF))
					    		.with(StairsBlock.SHAPE, world.getBlockState(blockpos).get(StairsBlock.SHAPE))
					    		.with(StairsBlock.WATERLOGGED, world.getBlockState(blockpos).get(StairsBlock.WATERLOGGED));
						world.setBlockState(blockpos, blockstate);
					}
				}
			}
		}
	}
	
	private static boolean func_220257_b(BlockState state, IWorldReader reader, BlockPos pos)
	{
		BlockPos blockpos = pos.up();
		BlockState blockstate = reader.getBlockState(blockpos);
				
		if (blockstate.getBlock() == ModBlocks.block_snow_stair && blockstate.get(BlockSnowStairs.LAYERS) == 1)
		{
			return true;
		}
		else if(blockstate.getBlock() instanceof StairsBlock && blockstate.get(HALF) == Half.BOTTOM)
		{
			return false;
		}
		else
		{	
			int i = LightEngine.func_215613_a(reader, state, pos, blockstate, blockpos,
					state.get(HALF) == Half.BOTTOM ? Direction.UP : Direction.DOWN,
					blockstate.getOpacity(reader, blockpos));
			
//			if(state.getBlock() == ModBlocks.block_dirt_slab && state.get(SlabBlock.TYPE) == SlabType.TOP)
//			{
//				
//				i = LightEngine.func_215613_a(reader, Blocks.GRASS_BLOCK.getDefaultState(), pos, blockstate, blockpos, Direction.UP, blockstate.getOpacity(reader, blockpos));
//			}
//			
			return i < reader.getMaxLightLevel();
		}
	}

	
	private static boolean func_220256_c(BlockState p_220256_0_, IWorldReader p_220256_1_, BlockPos p_220256_2_)
	{
		BlockPos blockpos = p_220256_2_.up();
		
		
		return func_220257_b(p_220256_0_, p_220256_1_, p_220256_2_) && !p_220256_1_.getFluidState(blockpos).isTagged(FluidTags.WATER);
	}
	
	
    @Override //TODO, half SLAB FIX!
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
    {
    	if(state.get(HALF) == Half.BOTTOM)
    		return false;
    	
    	BlockState plant = plantable.getPlant(world, pos.offset(facing));
    	
    	if (plant.getBlock() == Blocks.SUGAR_CANE) //Sugarcane was not working with super grass, placed it in on my own
    	{
    		if(world.getFluidState(pos).isTagged(FluidTags.WATER))
    				return true;
    		
    		if(world.getFluidState(pos.add(1,0,0)).isTagged(FluidTags.WATER))
    			return true;
    		else if(world.getFluidState(pos.add(-1,0,0)).isTagged(FluidTags.WATER))
    			return true;
    		else if(world.getFluidState(pos.add(0,0,1)).isTagged(FluidTags.WATER))
    			return true;
    		else if(world.getFluidState(pos.add(0,0,-1)).isTagged(FluidTags.WATER))
    			return true;
    		
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
		return state.get(HALF) == Half.TOP && worldIn.getBlockState(pos.up()).isAir();
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
}