package com.endlesnights.naturalslabsmod;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;

public interface INaturalSlabsCompat 
{
	public void registerBlocks(RegistryEvent.Register<Block> event);
	public void registerPlaceEntries();
}
