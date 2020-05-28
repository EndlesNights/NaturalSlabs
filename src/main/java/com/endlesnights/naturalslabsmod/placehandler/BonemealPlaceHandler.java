package com.endlesnights.naturalslabsmod.placehandler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.endlesnights.naturalslabsmod.init.ModBlocks;
import com.endlesnights.naturalslabsmod.NaturalSlabsMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.StairsBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.Half;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.DecoratedFeatureConfig;
import net.minecraft.world.gen.feature.FlowersFeature;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=NaturalSlabsMod.MODID)
public class BonemealPlaceHandler 
{
	private static final HashMap<ResourceLocation,Block> PLACE_ENTRIES = new HashMap<>();
	
	@SubscribeEvent
	public static void onBlockEntityPlace(RightClickBlock event)
	{	
		ItemStack itemStack = event.getItemStack();
		ResourceLocation rl = itemStack.getItem().getRegistryName();
		
		if(PLACE_ENTRIES.containsKey(rl))
			placeBoneMeal(event, itemStack, PLACE_ENTRIES.get(rl));
	}
	
	private static void placeBoneMeal(RightClickBlock event, ItemStack itemStack, Block block)
	{
		BlockPos pos = event.getPos();
		World world = event.getWorld();
		
		if(!(world.getBlockState(pos).getBlock() == ModBlocks.block_grass_slab
				|| (world.getBlockState(pos).getBlock() == ModBlocks.block_grass_stairs && world.getBlockState(pos).get(StairsBlock.HALF) == Half.TOP)
				|| world.getBlockState(pos).getBlock() == Blocks.GRASS_BLOCK ))
			return;
						
		if(!world.isRemote())
			grassGrow((ServerWorld)world, new Random(), pos, world.getBlockState(pos));
		
		event.getPlayer().swingArm(event.getHand());
		
		if(!event.getPlayer().isCreative())
			itemStack.shrink(1);
		
		event.setCanceled(true);
		
	}
	
	public static void grassGrow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState blockState)
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
	               else if(blockstate2.getBlock() == ModBlocks.grass_slab && rand.nextInt(10) == 0)
	               {
	            	   ((IGrowable)ModBlocks.grass_slab).grow(worldIn, rand, blockpos1, blockstate2);
	               }
	               else if(blockstate2.getBlock() == ModBlocks.block_grass_stairs && blockstate2.get(StairsBlock.HALF) == Half.TOP && rand.nextInt(10) == 0)
	               {
	            	   ((IGrowable)ModBlocks.block_grass_stairs).grow(worldIn, rand, blockpos1, blockstate2);
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

	               if (blockstate1.isValidPosition(worldIn, blockpos1))
	               {
	            	   worldIn.setBlockState(blockpos1, blockstate1, 3);

	               }
	               else if(SlabHelper.slabBottomFoliage(blockstate1).isValidPosition(worldIn, blockpos1))
	               {
	            	   worldIn.setBlockState(blockpos1, SlabHelper.slabBottomFoliage(blockstate1), 3);
	               }
	               break;
	            }

	            blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
	            if (
	            		   worldIn.getBlockState(blockpos1.down()).getBlock() !=  ModBlocks.block_grass_slab.getBlock()
	            		&& worldIn.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS_BLOCK.getBlock()
	            		&& !(worldIn.getBlockState(blockpos1.down()).getBlock() == ModBlocks.block_grass_stairs.getBlock() && worldIn.getBlockState(blockpos1.down()).get(StairsBlock.HALF) == Half.TOP)
	            		|| worldIn.getBlockState(blockpos1).isCollisionShapeOpaque(worldIn, blockpos1))
	            {
	            	
	               break;
	            }

	            ++j;
	         }
	      }
		
	}	
	
	public static void registerPlaceEntry(ResourceLocation itemName, Block block)
	{
		if(!PLACE_ENTRIES.containsKey(itemName))
		{
			PLACE_ENTRIES.put(itemName, block);
		}
			
	}

	public static Collection<Block> getPlaceEntryBlocks()
	{
		return PLACE_ENTRIES.values();
	}
}
