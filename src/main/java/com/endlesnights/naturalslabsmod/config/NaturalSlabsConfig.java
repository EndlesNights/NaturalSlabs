package com.endlesnights.naturalslabsmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class NaturalSlabsConfig
{
	public static ForgeConfigSpec.BooleanValue snowSlabStair;
	public static ForgeConfigSpec.BooleanValue snowAccumilation;
	public static ForgeConfigSpec.IntValue snowMaxHeightBlock;
	public static ForgeConfigSpec.IntValue snowMaxHeightSlab;
	
	public static ForgeConfigSpec.BooleanValue softSnowLanding;
	public static ForgeConfigSpec.DoubleValue snowFallRatio;
	public static ForgeConfigSpec.IntValue snowFallMaxLayers;
	
	
	public static void init(ForgeConfigSpec.Builder server)
	{
		snowSlabStair = server
				.comment("Allows for snow to generate ontop of slab and Stair Blocks [True / False]")
				.define("naturalslabsmodconfig.SnowOnSlabStairBlockCheck", true)
				;
		
		snowAccumilation = server
				.comment("Allows for snow to accumulate higher in snow banks with layers greater than the vanilia default of one. [True / False]")
				.define("naturalslabsmodconfig.SnowAccumilationCheck", true)
				;
		
		snowMaxHeightBlock = server
				.comment("The maximum number of layers snow will naturally accumulate to on normal blocks.")
				.defineInRange("naturalslabsmodconfig.SnowMaxHeightBlock", 8, 0, 8);
		
		snowMaxHeightSlab = server
				.comment("The maximum number of layers snow will naturally accumulate to on slab and stair blocks.")
				.defineInRange("naturalslabsmodconfig.SnowMaxHeightSlab", 12, 0, 12);
		
		softSnowLanding = server
				.comment("Allows for players and mobs landing in snow to ignore some fall damage. [True / False]")
				.define("naturalslabsmodconfig.SoftSnowCheck", true)
				;
		
		snowFallRatio = server
				.comment("Multiplier that determines the ratio between the number of layers of snow times how many blocks of fall distance they cancel out.")
				.defineInRange("naturalslabsmodconfig.snowFallRatio", 1.0, 0.0, 100.0);
		
		snowFallMaxLayers = server
				.comment("The maximum number of snow layers that will be considered to negate fall damage")
				.defineInRange("naturalslabsmodconfig.snowFallLayers", 32, 0, 2048);
	}
}
