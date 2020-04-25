package com.endlesnights.naturalslabsmod.placehandler;

import java.util.Collection;
import java.util.HashMap;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.DoubleBlockHalf;
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
public class SlabPlantPlaceHandler
{
	private static final HashMap<ResourceLocation,Block> PLACE_ENTRIES = new HashMap<>();
	
	@SubscribeEvent
	public static void onBlockEntityPlace(RightClickBlock event)
	{	
		ItemStack held = event.getItemStack();
		ResourceLocation rl = held.getItem().getRegistryName();

		if(PLACE_ENTRIES.containsKey(rl))
			placePlant(event, held, PLACE_ENTRIES.get(rl));
	}

	private static void placePlant(RightClickBlock event, ItemStack held, Block block)
	{		
		
		BlockPos pos = event.getPos();
		Direction face = event.getFace();
		BlockPos placeAt = pos.offset(face);
		World world = event.getWorld();		
		
		if(world.getBlockState(placeAt.down()).getProperties().contains(SlabBlock.TYPE)
				&& world.getBlockState(placeAt.down()).get(SlabBlock.TYPE) == SlabType.BOTTOM 
				&& face == Direction.UP && (world.isAirBlock(placeAt) || world.getFluidState(placeAt).getFluid() == Fluids.WATER) )
		{	
			
			world.setBlockState(placeAt, block.getDefaultState());
			if(block == ModBlocks.tall_grass_slab || block == ModBlocks.large_fern_slab)
				world.setBlockState(placeAt.up(), block.getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
			
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), block.getSoundType(world.getBlockState(placeAt)).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
			event.getPlayer().swingArm(event.getHand());
			event.setCanceled(true);
		}
	}

	public static void registerPlaceEntry(ResourceLocation itemName, Block torchSlab)
	{
		if(!PLACE_ENTRIES.containsKey(itemName))
			PLACE_ENTRIES.put(itemName, torchSlab);
	}

	public static Collection<Block> getPlaceEntryBlocks()
	{
		return PLACE_ENTRIES.values();
	}
}