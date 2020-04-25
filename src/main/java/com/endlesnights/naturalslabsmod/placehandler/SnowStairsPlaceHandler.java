package com.endlesnights.naturalslabsmod.placehandler;

import java.util.Collection;
import java.util.HashMap;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowStairs;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.Half;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=NaturalSlabsMod.MODID)
public class SnowStairsPlaceHandler
{
	private static final HashMap<ResourceLocation,Block> PLACE_ENTRIES = new HashMap<>();
	
	@SubscribeEvent
	public static void onBlockEntityPlace(RightClickBlock event)
	{	
		ItemStack itemStack = event.getItemStack();
		ResourceLocation rl = itemStack.getItem().getRegistryName();
		
		if(PLACE_ENTRIES.containsKey(rl))
			placeSnow(event, itemStack, PLACE_ENTRIES.get(rl));
	}
	private static void placeSnow(RightClickBlock event, ItemStack itemStack, Block block)
	{
		BlockPos pos = event.getPos();
		Direction face = event.getFace();
		BlockPos placeAt = pos.offset(face);
		World world = event.getWorld();		
	
		if(world.getBlockState(pos).getBlock() instanceof StairsBlock && face == Direction.UP 
				&& world.getBlockState(pos).get(StairsBlock.HALF) == Half.BOTTOM
				&& (world.isAirBlock(placeAt) || world.getFluidState(placeAt).getFluid() == Fluids.WATER || world.getFluidState(placeAt).getFluid() == Fluids.FLOWING_WATER) )
		{	
			if(itemStack.getItem() ==  Blocks.SNOW.asItem())
				world.setBlockState(placeAt, block.getDefaultState()  
						.with(StairsBlock.FACING, world.getBlockState(pos).get(StairsBlock.FACING))
						.with(StairsBlock.SHAPE, world.getBlockState(pos).get(StairsBlock.SHAPE)));
			else if(itemStack.getItem() ==  Blocks.SNOW_BLOCK.asItem())
				{
					if(event.getPlayer().isCrouching())
						return;
					
					world.setBlockState(placeAt, block.getDefaultState()  
							.with(StairsBlock.FACING, world.getBlockState(pos).get(StairsBlock.FACING))
							.with(StairsBlock.SHAPE, world.getBlockState(pos).get(StairsBlock.SHAPE))
							.with(BlockSnowStairs.LAYERS, 8));
				}
			
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), block.getSoundType(world.getBlockState(pos)).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
			event.getPlayer().swingArm(event.getHand());
			
			if(!event.getPlayer().isCreative())
				itemStack.shrink(1);
			event.setCanceled(true);
		}
		else if((world.getBlockState(pos).getBlock() instanceof BlockSnowStairs && face == Direction.UP)
				|| (world.getBlockState(pos).getBlock() instanceof StairsBlock && face == Direction.UP 
				&& world.getBlockState(pos).get(StairsBlock.HALF) == Half.BOTTOM && world.getBlockState(pos.up()).getBlock() instanceof BlockSnowStairs))
		{
			if(world.getBlockState(pos).getBlock() instanceof StairsBlock)
				{
					pos = placeAt;
				}
			
			int i = world.getBlockState(pos).get(BlockSnowStairs.LAYERS);
			
			if(i<12)
			{
				if(itemStack.getItem() ==  Blocks.SNOW.asItem())
				{
					world.setBlockState(pos, world.getBlockState(pos).with(BlockSnowStairs.LAYERS, i+1));
				}
				else if(itemStack.getItem() ==  Blocks.SNOW_BLOCK.asItem())
				{
					if(event.getPlayer().isCrouching())
						return;
					
					i+=8;
					world.setBlockState(pos, world.getBlockState(pos).with(BlockSnowStairs.LAYERS, Integer.valueOf(Math.min(12, i))));
					
					if(i >12 && (world.isAirBlock(pos.up()) || world.getFluidState(pos.up()).getFluid() == Fluids.WATER || world.getFluidState(pos.up()).getFluid() == Fluids.FLOWING_WATER) )
					{
						world.setBlockState(pos.up(), Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, i-12));
					}
				}
				

				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), block.getSoundType(world.getBlockState(pos)).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
				event.getPlayer().swingArm(event.getHand());
				
				if(!event.getPlayer().isCreative())
					itemStack.shrink(1);
				event.setCanceled(true);
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
