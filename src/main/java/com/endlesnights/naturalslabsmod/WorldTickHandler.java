package com.endlesnights.naturalslabsmod;

import java.util.Random;

import org.apache.logging.log4j.LogManager;

import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowStairs;
import com.endlesnights.naturalslabsmod.init.ModBlocks;

import java.lang.reflect.Method;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.state.properties.Half;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ChunkManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class WorldTickHandler 
{
	boolean stackingSnow = false;
	int randLCG;
	
	public WorldTickHandler(){
		randLCG = (new Random()).nextInt();
	}
	
	@SubscribeEvent
	public void worldTickEvent(WorldEvent event){
	    if(event.getWorld() instanceof ServerWorld)
	    {
	    	ServerWorld world = (ServerWorld) event.getWorld();
	    	if(world.isRaining())
	    	{	
	    		try
	    		{
	    			Method getChunkHolderIterator = ObfuscationReflectionHelper.findMethod(ChunkManager.class, "func_223491_f");
	    			getChunkHolderIterator.setAccessible(true);
	    			@SuppressWarnings("unchecked")
	    			Iterable<ChunkHolder> chunkContainer = (Iterable<ChunkHolder>) getChunkHolderIterator.invoke(world.getChunkProvider().chunkManager);
	    			
//	    			if(world.rand.nextInt(16) > 0)
//	    				return;
	    			
	    			for (ChunkHolder chunkHolder : chunkContainer)
		    		{
	    				Chunk chunk = chunkHolder.getChunkIfComplete();
	    				if (chunk == null || !world.getChunkProvider().isChunkLoaded(chunk.getPos()))
	    				{
	    					continue;
	    				}
		    			int chunk_min_x = chunk.getPos().getXStart();
		    			int chunk_min_y = chunk.getPos().getZStart();
		    			//If it can rain here, there is a 1/16 chance of trying to add snow
		    			if (world.dimension.canDoRainSnowIce(chunk) && world.rand.nextInt(64) == 0 )
		    			{
		    				randLCG = randLCG * 3 + 1013904223;
		    				int j2 = randLCG >> 2;
		    				//Get rain height at random position in chunk, splits the random val j2 to use for both parts of position
		    				BlockPos pos = world.getHeight(Heightmap.Type.MOTION_BLOCKING, new BlockPos(chunk_min_x + (j2 & 15), 0, chunk_min_y + (j2 >> 8 & 15))).down();
		    				BlockState state = world.getBlockState(pos);
		    				
		    				
		    				//Check if valid Y, correct light, and correct temp for snow formation
		    				if(pos.getY() >= 0 && pos.getY() < 256 && world.getLightFor(LightType.BLOCK, pos ) < 10 &&world.getBiome(pos).getTemperature(pos) < 0.15F) 
		    			    {
		    					//Check if block at positioin is a snow layer block
		    			    	if(stackingSnow && state.getBlock() instanceof SnowBlock)
		    			    	{
		    			    		//Calculate mean surrounding block height
		    			    		int height = state.get(SnowBlock.LAYERS);
		    			    		if(height == 8) return;
		    			    		BlockState north = world.getBlockState(pos.north());
		    			    		BlockState south = world.getBlockState(pos.south());
		    			    		BlockState east = world.getBlockState(pos.east());
		    			    		BlockState west = world.getBlockState(pos.west());
		    			    		float surroundings = 0;
		    			    		if(north.getBlock() instanceof SnowBlock)
		    			    		{
		    			    			surroundings += north.get(SnowBlock.LAYERS);
		    			    		}else if(north.isSolid())
		    			    		{
		    			    			surroundings += 8;
		    			    		}
		    			    		if(south.getBlock() instanceof SnowBlock)
		    			   			{
		    			   				surroundings += south.get(SnowBlock.LAYERS);
		    			   			}else if(south.isSolid())
		    			   			{
		    		    				surroundings += 8;
		    		    			}
		    		    			if(east.getBlock() instanceof SnowBlock)
		    		    			{
		    		    				surroundings += east.get(SnowBlock.LAYERS);
		    		    			}else if(east.isSolid())
		    		    			{
		    		    				surroundings += 8;
		    		    			}
		    		    			if(west.getBlock() instanceof SnowBlock)
		    		    			{
		    		    				surroundings += west.get(SnowBlock.LAYERS);
		    		    			}else if(west.isSolid())
		    		    			{
		    		    				surroundings += 8;
		    		    			}
		    		    			surroundings /= 4;
		    		    			//Done calculating surroundings
		    		    			if(surroundings >= height)
		   			   				{
		   			   					float weight = (surroundings - height) / 2 + 0.05f;
		   			   					if(world.rand.nextFloat() <= weight)
		    			   				{
		   			   						//Add layer!
		    		    					world.setBlockState(pos, Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, height + 1));
		    		    				}
		    		    			}	
		    		    		}
		    					else if(world.getBlockState(pos).getBlock() instanceof SlabBlock
	    							&& world.getBlockState(pos).get(SlabBlock.TYPE) == SlabType.BOTTOM)
        	                    {
		    						if(stackingSnow && world.getBlockState(pos.up()).getBlock() instanceof BlockSnowSlab)
		    						{
		    							world.setBlockState(pos.up(), ModBlocks.block_snow_slab.getDefaultState()
		    									.with(BlockSnowSlab.LAYERS, world.getBlockState(pos.up()).get(BlockSnowSlab.LAYERS) +1));
		    						}
		    						else
		    						{
		    							world.setBlockState(pos.up(), ModBlocks.block_snow_slab.getDefaultState());
		    						}
        	                    }
		    					else if(world.getBlockState(pos).getBlock() instanceof StairsBlock 
        	                    	&& world.getBlockState(pos).get(StairsBlock.HALF) == Half.BOTTOM)
        	                    {
		    						
		    						if(stackingSnow && world.getBlockState(pos.up()).getBlock() instanceof BlockSnowStairs)
		    						{
	        	                    	world.setBlockState(pos.up(), ModBlocks.block_snow_stair.getDefaultState()  
	        	        						.with(StairsBlock.FACING, world.getBlockState(pos).get(StairsBlock.FACING))
	        	        						.with(StairsBlock.SHAPE, world.getBlockState(pos).get(StairsBlock.SHAPE))
		    									.with(BlockSnowSlab.LAYERS, world.getBlockState(pos.up()).get(BlockSnowSlab.LAYERS) +1));
		    						}
		    						else
		    						{
	        	                    	world.setBlockState(pos.up(), ModBlocks.block_snow_stair.getDefaultState()  
	        	        						.with(StairsBlock.FACING, world.getBlockState(pos).get(StairsBlock.FACING))
	        	        						.with(StairsBlock.SHAPE, world.getBlockState(pos).get(StairsBlock.SHAPE)));
		    						}
        	                    }
		   			    	}
		    	
		    				
		   			    }
		   			}
	    		} catch (Exception ex) {
	    			LogManager.getLogger().fatal("COULD NOT ACCESS LOADED CHUNKS!");
	    			LogManager.getLogger().fatal(ex.getMessage());
	    		}
	    	}
	    }
	}
}
