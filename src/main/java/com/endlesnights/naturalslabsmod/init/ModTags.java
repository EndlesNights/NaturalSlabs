package com.endlesnights.naturalslabsmod.init;

import net.minecraft.block.Block;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
public class ModTags
{
	public static class Blocks
	{
		public static final Tag<Block> FENCE_SLAB = tag("fence_slab");
		
		private static Tag<Block> tag(@Nonnull String name) {
            return new BlockTags.Wrapper(new ResourceLocation(NaturalSlabsMod.MODID, name));
        }
	}
}
