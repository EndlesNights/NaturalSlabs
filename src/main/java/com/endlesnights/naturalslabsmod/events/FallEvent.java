package com.endlesnights.naturalslabsmod.events;

import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowStairs;
import com.endlesnights.naturalslabsmod.config.Config;
import com.endlesnights.naturalslabsmod.config.NaturalSlabsConfig;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod.EventBusSubscriber
public class FallEvent
{
	@SubscribeEvent
	public void onFall(LivingFallEvent event)
	{
		Config.loadConfig(Config.SERVER, FMLPaths.CONFIGDIR.get().resolve("naturalslabsmod-server.toml").toString());
		
		if(!NaturalSlabsConfig.softSnowLanding.get())
			return;
		
		Entity entity = event.getEntity();
        
		if (!(entity instanceof PlayerEntity))
		{
			return;
		}
		World worldIn = event.getEntity().getEntityWorld();
        
		BlockPos blockPos = new BlockPos(entity.getPosition().getX(), (Math.ceil(entity.getPositionVec().y) - 1),entity.getPosition().getZ());
		BlockPos playerPos = entity.getPosition();
		Block block = worldIn.getBlockState(blockPos).getBlock();
		
		int snowLayers = 0;
		int blockIndex = 0;
		
		//Check blockPos y+1 if it's a SnowBlock since they will not register as solid if layer is value of only one.
		
		if(worldIn.getBlockState(playerPos).getBlock() instanceof BlockSnowStairs) // Will detect top step 1 to 11, bottom 5 to 11
		{
			event.setDistance(event.getDistance() - (worldIn.getBlockState(playerPos).get(BlockSnowSlab.LAYERS).intValue() * NaturalSlabsConfig.snowFallRatio.get().floatValue()));
			return;
		}
		
		else if (worldIn.getBlockState(blockPos).getBlock() instanceof BlockSnowStairs //will detect stair/slab 12
				|| worldIn.getBlockState(blockPos).getBlock() instanceof BlockSnowSlab)
		{
			if(worldIn.getBlockState(blockPos.up()).getBlock() instanceof SnowBlock)
				event.setDistance(event.getDistance() - ((worldIn.getBlockState(blockPos).get(BlockSnowSlab.LAYERS).intValue() + 1) * NaturalSlabsConfig.snowFallRatio.get().floatValue()));
			else
				event.setDistance(event.getDistance() - (worldIn.getBlockState(blockPos).get(BlockSnowSlab.LAYERS).intValue() * NaturalSlabsConfig.snowFallRatio.get().floatValue()));
			return;
		}
		
		else if (worldIn.getBlockState(blockPos.up()).getBlock() instanceof BlockSnowStairs //will detect bottom step 1 to 4, bottom slab 1 to 11
				|| worldIn.getBlockState(blockPos.up()).getBlock() instanceof BlockSnowSlab)
		{
			event.setDistance(event.getDistance() - (worldIn.getBlockState(blockPos.up()).get(BlockSnowSlab.LAYERS).intValue() * NaturalSlabsConfig.snowFallRatio.get().floatValue()));
			return;
		}
		
		
		else  if(worldIn.getBlockState(playerPos).getBlock() instanceof SnowBlock && playerPos != blockPos)//will detect the single snow layer on normal block
		{
			if(worldIn.getBlockState(playerPos).get(SnowBlock.LAYERS).intValue() == 1)
				snowLayers++; //Should only ever be a value of one if this hits
		}
		
		int maxLayers = NaturalSlabsConfig.snowFallMaxLayers.get();
		
		boolean snowBlock = true;
		while(snowBlock)
		{
			if(snowLayers >= maxLayers)
			{
				snowLayers = maxLayers;
				break;
			}
			
			if(block instanceof SnowBlock)
			{
				if(blockIndex > 0 && worldIn.getBlockState(blockPos.down(blockIndex)).get(SnowBlock.LAYERS).intValue() < 8)
					break; //There would be an air gap in the blocks at this point, alternative idea would be to compress all the snow down?
				
				snowLayers += worldIn.getBlockState(blockPos.down(blockIndex)).get(SnowBlock.LAYERS).intValue();
				
			}
			else if(block.getRegistryName() == Blocks.SNOW_BLOCK.getRegistryName())
			{
				snowLayers +=8;
				
			}
			else if(block instanceof BlockSnowStairs || block instanceof BlockSnowSlab)
			{
				if(worldIn.getBlockState(blockPos.down(blockIndex)).get(BlockSnowSlab.LAYERS).intValue() == 12)
					snowLayers += 12;
				break;
			}
			else
			{
				snowBlock = false;
				break;
			}
			
			blockIndex++;
			block = worldIn.getBlockState(blockPos.down(blockIndex)).getBlock();
		}
		
		event.setDistance(event.getDistance() - (snowLayers * NaturalSlabsConfig.snowFallRatio.get().floatValue()));		
		//event.setDistance(event.getDistance() - snowLayers);
    }
}
