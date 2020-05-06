package com.endlesnights.naturalslabsmod.init;

import com.endlesnights.naturalslabsmod.INaturalSlabsCompat;
import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.blocks.BlockSunFlower;
import com.endlesnights.naturalslabsmod.blocks.FenceSlabBlock;
import com.endlesnights.naturalslabsmod.blocks.foliage.BeetrootSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.CarrotSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.CropsSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.DoublePlantSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.FlowerSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.FoliageSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.MushroomSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.PotatoSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.SaplingSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.SugarCaneSlab;
import com.endlesnights.naturalslabsmod.blocks.foliage.TallFlowerSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockCoarseDirtSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockDirtSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockFarmlandSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockGrassSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockPathSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowSlab;
import com.endlesnights.naturalslabsmod.blocks.slabs.BlockSnowStairs;
import com.endlesnights.naturalslabsmod.blocks.slabs.FallinglSlab;
import com.endlesnights.naturalslabsmod.placehandler.BonemealPlaceHandler;
import com.endlesnights.naturalslabsmod.placehandler.FenceSlabPlaceHandler;
import com.endlesnights.naturalslabsmod.placehandler.SnowSlabPlaceHandler;
import com.endlesnights.naturalslabsmod.placehandler.SnowStairsPlaceHandler;
import com.endlesnights.naturalslabsmod.placehandler.SugarCaneSlabPlaceHandler;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.trees.AcaciaTree;
import net.minecraft.block.trees.BirchTree;
import net.minecraft.block.trees.DarkOakTree;
import net.minecraft.block.trees.JungleTree;
import net.minecraft.block.trees.OakTree;
import net.minecraft.block.trees.SpruceTree;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;
/**
 * This class has the register event handler for all custom blocks. This class uses @Mod.EventBusSubscriber so the event handler has to be static This class uses @ObjectHolder to get a reference to the blocks
 */
@Mod.EventBusSubscriber(modid = NaturalSlabsMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(NaturalSlabsMod.MODID)
public class ModBlocks implements INaturalSlabsCompat
{
	public static final Block tutorial_block = null;
	
	public static Block block_grass_slab = null;
	public static Block block_dirt_slab = null;
	public static Block block_path_slab = null;
	public static Block block_farmland_slab = null;
	public static Block block_coarse_dirt_slab = null;
	
	public static Block block_gravel_slab = null;
	public static Block block_sand_slab = null;
	public static Block block_red_sand_slab = null;
	
	public static Block grass_slab = null;
	public static Block fern_slab = null;
	
	public static Block tall_grass_slab = null;
	public static Block large_fern_slab = null;
	
	public static Block dandelion_slab = null;
	public static Block poppy_slab = null;
	public static Block blue_orchid_slab = null;
	public static Block allium_slab = null;
	public static Block azure_bluet_slab = null;
	public static Block red_tulip_slab = null;
	public static Block orange_tulip_slab = null;
	public static Block white_tulip_slab = null;
	public static Block pink_tulip_slab = null;
	public static Block oxeye_daisy_slab = null;
	public static Block cornflower_slab = null;
	public static Block lily_of_the_valley_slab = null;
	public static Block wither_rose_slab = null;
	
	public static Block red_mushroom_slab = null;
	public static Block brown_mushroom_slab = null;
	
	public static Block wheat_slab = null;
	public static Block carrots_slab = null;
	public static Block potatoes_slab = null;
	public static Block beetroots_slab = null;
	
	public static Block sunflower_slab = null;
	public static Block lilac_slab = null;
	public static Block rose_bush_slab = null;
	public static Block peony_slab = null;
	public static Block sugar_cane_slab = null;

	public static Block oak_sapling_slab = null;
	public static Block spruce_sapling_slab = null;
	public static Block birch_sapling_slab = null; 
	public static Block jungle_sapling_slab = null;
	public static Block acacia_sapling_slab = null;
	public static Block dark_oak_sapling_slab = null;
	
	public static Block block_snow_slab = null;
	public static Block block_snow_stair = null;
	
	
	public static Block oak_fence_slab = null;
	public static Block SUNFLOWER = null;
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> event)
	{	
		oak_fence_slab = registerBlock(new FenceSlabBlock(Blocks.OAK_FENCE, Block.Properties.from(Blocks.OAK_FENCE)), "oak_fence_slab");
		
		block_grass_slab = registerBlock(new BlockGrassSlab(), "block_grass_slab");
		block_dirt_slab = registerBlock(new BlockDirtSlab(), "block_dirt_slab");
		block_path_slab = registerBlock(new BlockPathSlab(), "block_path_slab");
		block_farmland_slab = registerBlock(new BlockFarmlandSlab(), "block_farmland_slab");;
		block_coarse_dirt_slab = registerBlock(new BlockCoarseDirtSlab(), "block_coarse_dirt_slab");
		
		
		block_gravel_slab = registerBlock(new FallinglSlab(Block.Properties.from(Blocks.GRAVEL), -16777216, Blocks.GRAVEL), "block_gravel_slab");
		block_sand_slab = registerBlock(new FallinglSlab(Block.Properties.from(Blocks.SAND), 14406560, Blocks.SAND), "block_sand_slab");
		block_red_sand_slab = registerBlock(new FallinglSlab(Block.Properties.from(Blocks.RED_SAND), 11098145, Blocks.RED_SAND), "block_red_sand_slab");
		
		grass_slab = registerBlock(new FoliageSlab(Block.Properties.from(Blocks.GRASS)), "grass_slab");
		fern_slab = registerBlock(new FoliageSlab(Block.Properties.from(Blocks.FERN)), "fern_slab");
		
		tall_grass_slab = registerBlock(new DoublePlantSlab(Block.Properties.from(Blocks.TALL_GRASS)), "tall_grass_slab");
		large_fern_slab = registerBlock(new DoublePlantSlab(Block.Properties.from(Blocks.LARGE_FERN)), "large_fern_slab");
		
		dandelion_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.DANDELION)), "dandelion_slab");
		poppy_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.POPPY)), "poppy_slab");
		blue_orchid_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.BLUE_ORCHID)), "blue_orchid_slab");
		allium_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.ALLIUM)), "allium_slab");
		azure_bluet_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.AZURE_BLUET)), "azure_bluet_slab");
		red_tulip_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.RED_TULIP)), "red_tulip_slab");
		orange_tulip_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.ORANGE_TULIP)), "orange_tulip_slab");
		white_tulip_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.WHITE_TULIP)), "white_tulip_slab");
		pink_tulip_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.PINK_TULIP)), "pink_tulip_slab");
		oxeye_daisy_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.OXEYE_DAISY)), "oxeye_daisy_slab");
		cornflower_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.CORNFLOWER)), "cornflower_slab");
		lily_of_the_valley_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.LILY_OF_THE_VALLEY)), "lily_of_the_valley_slab");
		wither_rose_slab = registerBlock(new FlowerSlab(Block.Properties.from(Blocks.WITHER_ROSE)), "wither_rose_slab");
		
		red_mushroom_slab = registerBlock(new MushroomSlab(Block.Properties.from(Blocks.RED_MUSHROOM)), "red_mushroom_slab");
		brown_mushroom_slab = registerBlock(new MushroomSlab(Block.Properties.from(Blocks.BROWN_MUSHROOM)), "brown_mushroom_slab");
				
		wheat_slab = registerBlock(new CropsSlab(Block.Properties.from(Blocks.WHEAT)), "wheat_slab");
		carrots_slab = registerBlock(new CarrotSlab(Block.Properties.from(Blocks.CARROTS)), "carrots_slab");
		potatoes_slab = registerBlock(new PotatoSlab(Block.Properties.from(Blocks.POTATOES)), "potatoes_slab");
		beetroots_slab = registerBlock(new BeetrootSlab(Block.Properties.from(Blocks.BEETROOTS)), "beetroots_slab");
		
		sunflower_slab = registerBlock(new TallFlowerSlab(Block.Properties.from(Blocks.SUNFLOWER)), "sunflower_slab");
		lilac_slab = registerBlock(new TallFlowerSlab(Block.Properties.from(Blocks.LILAC)), "lilac_slab");
		rose_bush_slab = registerBlock(new TallFlowerSlab(Block.Properties.from(Blocks.ROSE_BUSH)), "rose_bush_slab");
		peony_slab = registerBlock(new TallFlowerSlab(Block.Properties.from(Blocks.PEONY)), "peony_slab");
		
		oak_sapling_slab = registerBlock(new SaplingSlab(new OakTree(), Block.Properties.from(Blocks.OAK_SAPLING)), "oak_sapling_slab");
		spruce_sapling_slab = registerBlock(new SaplingSlab(new SpruceTree(), Block.Properties.from(Blocks.SPRUCE_SAPLING)), "spruce_sapling_slab");
		birch_sapling_slab = registerBlock(new SaplingSlab(new BirchTree(), Block.Properties.from(Blocks.BIRCH_SAPLING)), "birch_sapling_slab");
		jungle_sapling_slab = registerBlock(new SaplingSlab(new JungleTree(), Block.Properties.from(Blocks.JUNGLE_SAPLING)), "jungle_sapling_slab");
		acacia_sapling_slab = registerBlock(new SaplingSlab(new AcaciaTree(), Block.Properties.from(Blocks.ACACIA_SAPLING)), "acacia_sapling_slab");
		dark_oak_sapling_slab = registerBlock(new SaplingSlab(new DarkOakTree(), Block.Properties.from(Blocks.DARK_OAK_SAPLING)), "dark_oak_sapling_slab");
		
		block_snow_slab = registerBlock(new BlockSnowSlab(Block.Properties.from(Blocks.SNOW).lootFrom(Blocks.SNOW).notSolid()), "block_snow_slab");
		block_snow_stair = registerBlock(new BlockSnowStairs(Block.Properties.from(Blocks.SNOW).lootFrom(Blocks.SNOW).notSolid()), "block_snow_stairs");
		
		sugar_cane_slab = registerBlock(new SugarCaneSlab(Block.Properties.from(Blocks.SUGAR_CANE).lootFrom(Blocks.SUGAR_CANE)), "sugar_cane_slab");
		
		SUNFLOWER = registerBlock(new BlockSunFlower (Block.Properties.from(Blocks.SUNFLOWER).lootFrom(Blocks.SUNFLOWER)), "sunflower");
		
        if (FMLEnvironment.dist == Dist.CLIENT)
        {
        	
        	RenderType transparentRenderType = RenderType.getCutoutMipped();
            //RenderType cutoutRenderType = RenderType.func_228643_e_();
            //RenderType translucentRenderType = RenderType.func_228645_f_();
        	
			RenderTypeLookup.setRenderLayer(block_grass_slab, transparentRenderType);
        	
			RenderTypeLookup.setRenderLayer(grass_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(fern_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(tall_grass_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(large_fern_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(dandelion_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(poppy_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(blue_orchid_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(allium_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(azure_bluet_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(red_tulip_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(orange_tulip_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(white_tulip_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(pink_tulip_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(oxeye_daisy_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(cornflower_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(lily_of_the_valley_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(wither_rose_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(red_mushroom_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(brown_mushroom_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(wheat_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(carrots_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(potatoes_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(beetroots_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(sunflower_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(lilac_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(rose_bush_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(peony_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(oak_sapling_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(spruce_sapling_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(birch_sapling_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(jungle_sapling_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(acacia_sapling_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(dark_oak_sapling_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(block_snow_slab, transparentRenderType);
			RenderTypeLookup.setRenderLayer(block_snow_stair, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(sugar_cane_slab, transparentRenderType);
			
			RenderTypeLookup.setRenderLayer(SUNFLOWER, transparentRenderType);
            
        }
	}
	@Override
	public void registerPlaceEntries()
	{		
		BonemealPlaceHandler.registerPlaceEntry(Items.BONE_MEAL.getRegistryName(), null);
		SugarCaneSlabPlaceHandler.registerPlaceEntry(Items.SUGAR_CANE.getRegistryName(), sugar_cane_slab);

		SnowSlabPlaceHandler.registerPlaceEntry(Items.SNOW.getRegistryName(), block_snow_slab);
		SnowSlabPlaceHandler.registerPlaceEntry(Items.SNOW_BLOCK.getRegistryName(), block_snow_slab);
		
		SnowStairsPlaceHandler.registerPlaceEntry(Items.SNOW.getRegistryName(), block_snow_stair);
		SnowStairsPlaceHandler.registerPlaceEntry(Items.SNOW_BLOCK.getRegistryName(), block_snow_stair);
		
		FenceSlabPlaceHandler.registerPlaceEntry(Items.OAK_FENCE.getRegistryName(), oak_fence_slab);
	}
	
    public static Block registerBlock(Block block, String name)
    {
        block.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        return block;
    }

    public static Block registerBlockNoGroup(Block block, String name)
    {
        BlockItem itemBlock = new BlockItem(block, new Item.Properties().group(null));
        block.setRegistryName(name);
        itemBlock.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);
        ForgeRegistries.ITEMS.register(itemBlock);
        return block;
    }
    
    public static Block registerBlock(Block block, BlockItem itemBlock, String name) {
        block.setRegistryName(name);
        ForgeRegistries.BLOCKS.register(block);

        if (itemBlock != null) {
            itemBlock.setRegistryName(name);
            ForgeRegistries.ITEMS.register(itemBlock);
        }

        return block;
    }

}