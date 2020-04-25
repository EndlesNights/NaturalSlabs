package com.endlesnights.naturalslabsmod.blocks.foliage;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SlabBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.state.properties.SlabType;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;

import java.util.Random;

import javax.annotation.Nullable;

import com.endlesnights.naturalslabsmod.init.ModBlocks;

public class FoliageSlab extends BushBlock implements IPlantable, IGrowable
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, -8.0D, 2.0D, 14.0D, 5.0D, 14.0D);
	protected static final VoxelShape SHORT = Block.makeCuboidShape(1.0D, -8.0D, 1.0D, 15.0D, -1.0D, 15.0D);
	
    public FoliageSlab(Block.Properties properties)
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
    public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack)
    {
        if (!worldIn.isRemote && stack.getItem() == Items.SHEARS)
        {
           player.addStat(Stats.BLOCK_MINED.get(this));
           player.addExhaustion(0.005F);
           spawnAsEntity(worldIn, pos, new ItemStack(this));
        }
        else
        {
           super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
     }
    
    public java.util.List<ItemStack> onSheared(ItemStack item, net.minecraft.world.IWorld world, BlockPos pos, int fortune)
    {
       world.setBlockState(pos, Blocks.AIR.getDefaultState(), 11);
       return java.util.Arrays.asList(new ItemStack(this));
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
    public Block.OffsetType getOffsetType()
    {
        return Block.OffsetType.XYZ;
    }
    
    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos)
    {
    	return PlantType.Plains;
    }

	@Override
	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
	{
		if(this == ModBlocks.grass_slab
				|| this == ModBlocks.fern_slab)
			return true;
		
		return false;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
	{
		if(this == ModBlocks.grass_slab
				|| this == ModBlocks.fern_slab)
			return true;
		
		return false;
	}

	@Override
	public void grow(ServerWorld worldIn, Random rand, BlockPos pos, BlockState state)
	{
		if(!worldIn.isAirBlock(pos.up()))
			return;
		
		if(this == ModBlocks.grass_slab)
		{
			worldIn.setBlockState(pos, ModBlocks.tall_grass_slab.getDefaultState());
			worldIn.setBlockState(pos.up(), ModBlocks.tall_grass_slab.getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
			return;
		}
		else if (this == ModBlocks.fern_slab)
		{
			worldIn.setBlockState(pos, ModBlocks.large_fern_slab.getDefaultState());
			worldIn.setBlockState(pos.up(), ModBlocks.large_fern_slab.getDefaultState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER));
			return;
		}
			
		// TODO Auto-generated method stub
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		if(this == ModBlocks.grass_slab)
		{
			return new ItemStack(Items.GRASS);
		}
		else if (this == ModBlocks.fern_slab)
		{
			return new ItemStack(Items.FERN);
		}
		
		return new ItemStack(null);
	}
}