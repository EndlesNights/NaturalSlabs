package com.endlesnights.naturalslabsmod.init;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
//import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = NaturalSlabsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(NaturalSlabsMod.MODID)
public class ModItems
{

    public static final Item tutorial_dust = null;
    
    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        event.getRegistry().registerAll
        (
        	//Items
//			new Item(new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(NaturalSlabsMod.MODID, "tutorial_dust"),
            
            //Item of Blocks
//			createItemBlockForBlock(ModBlocks.tutorial_block, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            
        	createItemBlockForBlock(ModBlocks.block_grass_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            createItemBlockForBlock(ModBlocks.block_dirt_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            createItemBlockForBlock(ModBlocks.block_path_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            createItemBlockForBlock(ModBlocks.block_farmland_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            createItemBlockForBlock(ModBlocks.block_coarse_dirt_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            createItemBlockForBlock(ModBlocks.block_gravel_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            createItemBlockForBlock(ModBlocks.block_sand_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
            createItemBlockForBlock(ModBlocks.block_red_sand_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab))
            
//			createItemBlockForBlock(ModBlocks.SUNFLOWER, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab)),
//
//			createItemBlockForBlock(ModBlocks.block_snow_slab, new Item.Properties().group(NaturalSlabsMod.instance.creativeTab))
        );
    }
    
    private static BlockItem createItemBlockForBlock (Block block, Item.Properties properties)
    {
        return (BlockItem) new BlockItem(block, properties).setRegistryName(block.getRegistryName());
    }

}