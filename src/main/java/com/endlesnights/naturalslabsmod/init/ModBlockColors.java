package com.endlesnights.naturalslabsmod.init;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.blocks.foliage.TallGrassSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockGrassSlab;
import com.endlesnights.naturalslabsmod.blocks.stair.BlockGrassStair;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.BlockItem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid=NaturalSlabsMod.MODID, bus=Bus.MOD, value=Dist.CLIENT)
public class ModBlockColors
{
	@SubscribeEvent
	public static void registerBlockColorHandler(final ColorHandlerEvent.Block event)
	{
		final BlockColors blockColors = event.getBlockColors();
		
		blockColors.register(new BlockGrassSlab.ColorHandler(), ModBlocks.block_grass_slab);
		blockColors.register(new BlockGrassStair.ColorHandler(), ModBlocks.block_grass_stairs);
		
		blockColors.register(new TallGrassSlab.ColorHandler(),  ModBlocks.grass_slab);
		blockColors.register(new TallGrassSlab.ColorHandler(),  ModBlocks.fern_slab);
		
		blockColors.register(new TallGrassSlab.ColorHandler(),  ModBlocks.tall_grass_slab);
		blockColors.register(new TallGrassSlab.ColorHandler(),  ModBlocks.large_fern_slab);
		//blockColors.register(new TallGrassSlab.ColorHandler(),  ModBlocks.sunflower_slab);
		
		blockColors.register(new TallGrassSlab.ColorHandler(),  ModBlocks.sugar_cane_slab);

	}
	
	@SubscribeEvent
	public static void registerItemColorHandler(final ColorHandlerEvent.Item event)
	{
		BlockColors blockColors = event.getBlockColors();
		ItemColors itemColors = event.getItemColors();
		
		final IItemColor itemBlockColourHandler = (stack, tintIndex) -> {
		    final BlockState state = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
		    return  blockColors.getColor(state, null, null, tintIndex);
		};
		
		itemColors.register(itemBlockColourHandler, ModBlocks.block_grass_slab);
		itemColors.register(itemBlockColourHandler, ModBlocks.block_grass_stairs);
	}
}
