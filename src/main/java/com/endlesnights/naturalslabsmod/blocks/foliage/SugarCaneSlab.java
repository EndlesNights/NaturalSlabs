package com.endlesnights.naturalslabsmod.blocks.foliage;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.server.ServerWorld;

public class SugarCaneSlab extends Block implements net.minecraftforge.common.IPlantable
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(2.0D, -8.0D, 2.0D, 14.0D, 8.0D, 14.0D);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_0_15;
	
	public SugarCaneSlab(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(AGE, Integer.valueOf(0)));
	}
	public void func_225534_a_(BlockState p_225534_1_, ServerWorld p_225534_2_, BlockPos p_225534_3_, Random p_225534_4_)
	{
	      if (!p_225534_1_.isValidPosition(p_225534_2_, p_225534_3_)) {
	          p_225534_2_.destroyBlock(p_225534_3_, true);
	       } else if (p_225534_2_.isAirBlock(p_225534_3_.up())) {
	          int i;
	          for(i = 1; p_225534_2_.getBlockState(p_225534_3_.down(i)).getBlock() == this; ++i) {
	             ;
	          }

	          if (i < 3) {
	             int j = p_225534_1_.get(AGE);
	             if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(p_225534_2_, p_225534_3_, p_225534_1_, true)) {
	             if (j == 15) {
	                p_225534_2_.setBlockState(p_225534_3_.up(), this.getDefaultState());
	                p_225534_2_.setBlockState(p_225534_3_, p_225534_1_.with(AGE, Integer.valueOf(0)), 4);
	             } else {
	                p_225534_2_.setBlockState(p_225534_3_, p_225534_1_.with(AGE, Integer.valueOf(j + 1)), 4);
	             }
	             net.minecraftforge.common.ForgeHooks.onCropsGrowPost(p_225534_2_, p_225534_3_, p_225534_1_);
	             }
	          }
	       }
	}
	
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos)
	{
		if(worldIn.getBlockState(pos.down()).canSustainPlant(worldIn, pos.down(), Direction.UP, this)
				|| worldIn.getBlockState(pos.down()).getBlock() == this)
			return true;
		
		return false;
	}
	
	
	public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
		if (!stateIn.isValidPosition(worldIn, currentPos)) {
			worldIn.getPendingBlockTicks().scheduleTick(currentPos, this, 1);
		}

		return super.updatePostPlacement(stateIn, facing, facingState, worldIn, currentPos, facingPos);
	}
	   
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		return SHAPE;
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(Items.SUGAR_CANE);
	}
	
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}

	@Override
	public net.minecraftforge.common.PlantType getPlantType(IBlockReader world, BlockPos pos) {
		return net.minecraftforge.common.PlantType.Beach;
	}

	@Override
	public BlockState getPlant(IBlockReader world, BlockPos pos)
	{
		return getDefaultState();
	}

}
