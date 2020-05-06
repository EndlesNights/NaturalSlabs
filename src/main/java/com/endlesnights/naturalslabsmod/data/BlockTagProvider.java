package com.endlesnights.naturalslabsmod.data;

import net.minecraft.block.Block;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;

import javax.annotation.Nonnull;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.blocks.FenceSlabBlock;
import com.endlesnights.naturalslabsmod.init.ModTags;

import java.util.Comparator;
import java.util.function.Predicate;

public class BlockTagProvider extends BlockTagsProvider
{

    public BlockTagProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags()
    {
        Predicate<Block> extendedMushrooms = block -> NaturalSlabsMod.MODID.equals(block.getRegistryName().getNamespace());
        
        getBuilder(ModTags.Blocks.FENCE_SLAB).add(registry.stream().filter(extendedMushrooms)
                .filter(block -> block instanceof FenceSlabBlock)
                .sorted(Comparator.comparing(Block::getRegistryName))
                .toArray(Block[]::new));
        
        getBuilder(BlockTags.FENCES).add(ModTags.Blocks.FENCE_SLAB);
        getBuilder(BlockTags.WOODEN_FENCES).add(ModTags.Blocks.FENCE_SLAB);

    }

    @Nonnull
    @Override
    public String getName() {
        return "Natural Slab Mod Block Tags";
    }
    
}
