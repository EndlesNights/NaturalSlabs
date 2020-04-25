package com.endlesnights.naturalslabsmod.placehandler;

import java.util.Collection;
import java.util.HashMap;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowSlab;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.SnowBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=NaturalSlabsMod.MODID)
public class SnowSlabPlaceHandler
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

		if(world.getBlockState(pos).getBlock() instanceof SlabBlock && face == Direction.UP 
				&& world.getBlockState(pos).get(SlabBlock.TYPE) == SlabType.BOTTOM 
				&& (world.isAirBlock(placeAt) || world.getFluidState(placeAt).getFluid() == Fluids.WATER || world.getFluidState(placeAt).getFluid() == Fluids.FLOWING_WATER) )
		{	
			if(itemStack.getItem() ==  Blocks.SNOW.asItem())
				world.setBlockState(placeAt, block.getDefaultState());
			else if(itemStack.getItem() ==  Blocks.SNOW_BLOCK.asItem())
				{
					if(event.getPlayer().isCrouching())
						return;
					world.setBlockState(placeAt, block.getDefaultState().with(BlockSnowSlab.LAYERS, 8));
				}
			
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), block.getSoundType(world.getBlockState(pos)).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
			event.getPlayer().swingArm(event.getHand());
			
			if(!event.getPlayer().isCreative())
				itemStack.shrink(1);
			event.setCanceled(true);
		}
		else if((world.getBlockState(pos).getBlock() instanceof BlockSnowSlab && face == Direction.UP)
				|| (world.getBlockState(pos).getBlock() instanceof SlabBlock && face == Direction.UP 
				&& world.getBlockState(pos).get(SlabBlock.TYPE) == SlabType.BOTTOM && world.getBlockState(pos.up()).getBlock() instanceof BlockSnowSlab))
		{
			if(world.getBlockState(pos).getBlock() instanceof SlabBlock)
				{
					pos = placeAt;
				}
			
			int i = world.getBlockState(pos).get(BlockSnowSlab.LAYERS);
			
			if(i<12)
			{
				if(itemStack.getItem() ==  Blocks.SNOW.asItem())
				{
					world.setBlockState(pos, block.getDefaultState().with(BlockSnowSlab.LAYERS, i+1));
				}
				else if(itemStack.getItem() ==  Blocks.SNOW_BLOCK.asItem())
				{
					if(event.getPlayer().isCrouching())
						return;
					
					i+=8;
					world.setBlockState(pos, block.getDefaultState().with(BlockSnowSlab.LAYERS, Integer.valueOf(Math.min(12, i))));
					
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
		else if(itemStack.getItem() ==  Blocks.SNOW_BLOCK.asItem()
				&& world.getBlockState(pos).getBlock() instanceof SnowBlock && face == Direction.UP
				&& world.getBlockState(pos).get(SnowBlock.LAYERS) < 8)
					 
			{
				
				int i = world.getBlockState(pos).get(SnowBlock.LAYERS);
				
				world.setBlockState(pos, Blocks.SNOW_BLOCK.getDefaultState());
				
				if(world.isAirBlock(pos.up()) || world.getFluidState(pos.up()).getFluid() == Fluids.WATER || world.getFluidState(pos.up()).getFluid() == Fluids.FLOWING_WATER)
					world.setBlockState(pos.up(), Blocks.SNOW.getDefaultState().with(SnowBlock.LAYERS, i));

				world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), block.getSoundType(world.getBlockState(pos)).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
				event.getPlayer().swingArm(event.getHand());
				
				if(!event.getPlayer().isCreative())
					itemStack.shrink(1);
				event.setCanceled(true);
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
