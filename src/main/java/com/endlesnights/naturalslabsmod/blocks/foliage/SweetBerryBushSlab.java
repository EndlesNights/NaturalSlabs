package com.endlesnights.naturalslabsmod.blocks.foliage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SweetBerryBushBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class SweetBerryBushSlab extends SweetBerryBushBlock
{
	private static final VoxelShape SMALLBUSH = Block.makeCuboidShape(3.0D, -8.0D, 3.0D, 13.0D, 1.0D, 13.0D);
	private static final VoxelShape MEDBUSH = Block.makeCuboidShape(1.0D, -8.0D, 1.0D, 15.0D, 8.0D, 15.0D);
	private static final VoxelShape LARGEBUSH = Block.makeCuboidShape(0.0D, -8.0D, 0.0D, 16.0D, 8.0D, 16.0D);

	public SweetBerryBushSlab(Block.Properties properties) {
	      super(properties);
	}
	
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		if (state.get(AGE) == 0)
			return SMALLBUSH;
		 else 
			return state.get(AGE) < 3 ? MEDBUSH : LARGEBUSH;
		
	}
}
