package com.endlesnights.naturalslabsmod.events;

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

@Mod.EventBusSubscriber
public class FallEvent
{
	@SubscribeEvent
	public void onFall(LivingFallEvent event)
	{
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
		
		boolean snowBlock = true;
		
		//Check blockPos y+1 if it's a SnowBlock since they will not register as solid if layer is value of only one.
		
		if(worldIn.getBlockState(playerPos).getBlock() instanceof SnowBlock && playerPos != blockPos)
		{
			if(worldIn.getBlockState(playerPos).get(SnowBlock.LAYERS).intValue() == 1)
				snowLayers++; //Should only ever be a value of one if this hits
		}
		
		while(snowBlock)
		{
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
			else
			{
				snowBlock = false;
				break;
			}
			
			blockIndex++;
			block = worldIn.getBlockState(blockPos.down(blockIndex)).getBlock();
		}
				
		event.setDistance(event.getDistance() - snowLayers);
    }
}
