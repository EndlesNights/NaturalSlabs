package com.endlesnights.naturalslabsmod.blocks.foliage;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.block.Blocks;
import net.minecraft.block.SlabBlock;
public class FlowerSlab extends FoliageSlab
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, -8.0D, 5.0D, 11.0D, 2.0D, 11.0D);
	
    public FlowerSlab(Block.Properties properties)
    {
        super(properties);
    }
    
    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext selectionContext)
    {       
    	Vec3d vec3d = state.getOffset(worldIn, pos);
    	return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
    }
    
    @Override
    public Block.OffsetType getOffsetType()
    {
        return Block.OffsetType.XZ;
    }
    
	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
	{
		return facing == Direction.DOWN && !isValidPosition(state, world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos)
	{
		return (world.getBlockState(pos.offset(Direction.DOWN)).getProperties().contains(SlabBlock.TYPE) 
				&& world.getBlockState(pos.offset(Direction.DOWN)).get(SlabBlock.TYPE) == SlabType.BOTTOM);
	}
    
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		
		if(this == ModBlocks.dandelion_slab)
		{
			return new ItemStack(Items.DANDELION);
		}
		else if (this == ModBlocks.poppy_slab)
		{
			return new ItemStack(Items.POPPY);
		}
		else if (this == ModBlocks.blue_orchid_slab)
		{
			return new ItemStack(Items.BLUE_ORCHID);
		}
		else if (this == ModBlocks.allium_slab)
		{
			return new ItemStack(Items.ALLIUM);
		}
		else if (this == ModBlocks.azure_bluet_slab)
		{
			return new ItemStack(Items.AZURE_BLUET);
		}
		else if (this == ModBlocks.red_tulip_slab)
		{
			return new ItemStack(Items.RED_TULIP);
		}
		else if (this == ModBlocks.orange_tulip_slab)
		{
			return new ItemStack(Items.ORANGE_TULIP);
		}
		else if (this == ModBlocks.white_tulip_slab)
		{
			return new ItemStack(Items.WHITE_TULIP);
		}
		else if (this == ModBlocks.oxeye_daisy_slab)
		{
			return new ItemStack(Items.OXEYE_DAISY);
		}
		else if (this == ModBlocks.cornflower_slab)
		{
			return new ItemStack(Items.CORNFLOWER);
		}
		else if (this == ModBlocks.lily_of_the_valley_slab)
		{
			return new ItemStack(Items.LILY_OF_THE_VALLEY);
		}
		
		return new ItemStack(null);
	}
}
