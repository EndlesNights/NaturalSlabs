package com.endlesnights.naturalslabsmod.world.biome;

import com.endlesnights.naturalslabsmod.world.gen.surfacebuilders.ModSurfaceBuilder;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;

public class SlabPlainsBiome extends Biome
{

	public SlabPlainsBiome()
	{
		//SurfaceBuilder.DEFAULT
		//ModSurfaceBuilder.DEFAULT_SLAB
		super((new Biome.Builder()).surfaceBuilder(ModSurfaceBuilder.DEFAULT_SLAB,SurfaceBuilder
				.GRASS_DIRT_GRAVEL_CONFIG)
				.precipitation(Biome.RainType.RAIN)
				.category(Biome.Category.PLAINS)
				.depth(0.125F).scale(0.05F)
				.temperature(0.8F).downfall(0.4F)
				.waterColor(4159204).waterFogColor(329011)
				.parent((String)null)
				);
		
//		this.func_226711_a_(Feature.VILLAGE.func_225566_b_(new VillageConfig("village/plains/town_centers",6)));
//		this.func_226711_a_(Feature.PILLAGER_OUTPOST.func_225566_b_(IFeatureConfig.NO_FEATURE_CONFIG));
//		this.func_226711_a_(Feature.MINESHAFT.func_225566_b_(new MineshaftConfig(0.004D,MineshaftStructure.Type.NORMAL)));
//		this.func_226711_a_(Feature.STRONGHOLD.func_225566_b_(IFeatureConfig.NO_FEATURE_CONFIG));
//		
//		DefaultBiomeFeatures.addCarvers(this);
//		DefaultBiomeFeatures.addStructures(this);
//		DefaultBiomeFeatures.addLakes(this);
//		DefaultBiomeFeatures.addMonsterRooms(this);
//		DefaultBiomeFeatures.addStoneVariants(this);
//		DefaultBiomeFeatures.addOres(this);
//		DefaultBiomeFeatures.addSedimentDisks(this);
//		DefaultBiomeFeatures.addMushrooms(this);
//		DefaultBiomeFeatures.addReedsAndPumpkins(this);
//		DefaultBiomeFeatures.addSprings(this);
//		DefaultBiomeFeatures.addFreezeTopLayer(this);
//		
//		DefaultBiomeFeatures.func_222283_Y(this);
//		DefaultBiomeFeatures.func_222299_R(this);
//		
//		this.addSpawn(EntityClassification.CREATURE,new Biome.SpawnListEntry(EntityType.SHEEP,12,4,4));
//		this.addSpawn(EntityClassification.CREATURE,new Biome.SpawnListEntry(EntityType.PIG,10,4,4));
//		this.addSpawn(EntityClassification.CREATURE,new Biome.SpawnListEntry(EntityType.CHICKEN,10,4,4));
//		this.addSpawn(EntityClassification.CREATURE,new Biome.SpawnListEntry(EntityType.COW,8,4,4));
//		this.addSpawn(EntityClassification.CREATURE,new Biome.SpawnListEntry(EntityType.HORSE,5,2,6));
//		this.addSpawn(EntityClassification.CREATURE,new Biome.SpawnListEntry(EntityType.DONKEY,1,1,3));
//		this.addSpawn(EntityClassification.AMBIENT,new Biome.SpawnListEntry(EntityType.BAT,10,8,8));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.SPIDER,100,4,4));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.ZOMBIE,95,4,4));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.ZOMBIE_VILLAGER,5,1,1));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.SKELETON,100,4,4));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.CREEPER,100,4,4));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.SLIME,100,4,4));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.ENDERMAN,10,1,4));
//		this.addSpawn(EntityClassification.MONSTER,new Biome.SpawnListEntry(EntityType.WITCH,5,1,1));
	}

}