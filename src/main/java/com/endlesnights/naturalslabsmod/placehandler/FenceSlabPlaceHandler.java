package com.endlesnights.naturalslabsmod.placehandler;

import java.util.Collection;
import java.util.HashMap;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;
import com.endlesnights.naturalslabsmod.blocks.FenceSlabBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.SlabType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber(modid=NaturalSlabsMod.MODID)
public class FenceSlabPlaceHandler
{
	private static final HashMap<ResourceLocation,Block> PLACE_ENTRIES = new HashMap<>();
	
	@SubscribeEvent
	public static void onBlockEntityPlace(RightClickBlock event)
	{	
		ItemStack itemStack = event.getItemStack();
		ResourceLocation rl = itemStack.getItem().getRegistryName();
		
		if(PLACE_ENTRIES.containsKey(rl))
			placeBoneMeal(event, itemStack, PLACE_ENTRIES.get(rl));
	}
	
	private static void placeBoneMeal(RightClickBlock event, ItemStack itemStack, Block block)
	{
		BlockPos pos = event.getPos();
		Direction face = event.getFace();
		BlockPos placeAt = pos.offset(face);
		World world = event.getWorld();		
		
		if( 
					(face == Direction.UP && world.getBlockState(pos).getBlock() instanceof SlabBlock &&  world.getBlockState(pos).get(SlabBlock.TYPE) == SlabType.BOTTOM)
//				|| 	(face == Direction.DOWN && world.getBlockState(pos).getBlock() instanceof SlabBlock &&  world.getBlockState(pos).get(SlabBlock.TYPE) == SlabType.TOP)
				|| 	(world.getBlockState(pos).getBlock() instanceof FenceSlabBlock)
				&& 	(world.isAirBlock(placeAt) || world.getFluidState(placeAt).getFluid() == Fluids.WATER || world.getFluidState(placeAt).getFluid() == Fluids.FLOWING_WATER)
				)
		{	
			
			if(Block.hasEnoughSolidSide(world, placeAt.down(), Direction.UP) && !(world.getBlockState(placeAt.down()).getBlock() instanceof FenceSlabBlock))
			{
				return;
			}
			
			if (block instanceof IWaterLoggable)
				world.setBlockState(placeAt, getStateForPlacement(placeAt, world, block).with(BlockStateProperties.WATERLOGGED, (world.getFluidState(placeAt).getFluid() == Fluids.WATER) ));
			else
				world.setBlockState(placeAt, getStateForPlacement(placeAt, world, block));

			
			
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), block.getSoundType(world.getBlockState(pos)).getPlaceSound(), SoundCategory.BLOCKS, 1.0F, 1.0F);
			event.getPlayer().swingArm(event.getHand());
			
			if(!event.getPlayer().isCreative())
				itemStack.shrink(1);
			event.setCanceled(true);
		}
		
	}	
	
	
	public static BlockState getStateForPlacement(BlockPos currentPos, World worldIn, Block block)
	{
		BlockPos blockpos1 = currentPos.north();
		BlockPos blockpos2 = currentPos.east();
		BlockPos blockpos3 = currentPos.south();
		BlockPos blockpos4 = currentPos.west();
		
		BlockState blockstate = worldIn.getBlockState(blockpos1);
		BlockState blockstate1 = worldIn.getBlockState(blockpos2);
		BlockState blockstate2 = worldIn.getBlockState(blockpos3);
		BlockState blockstate3 = worldIn.getBlockState(blockpos4);
		
		BlockPos blockpos1Down = currentPos.north().down();
		BlockPos blockpos2Down = currentPos.east().down();
		BlockPos blockpos3Down = currentPos.south().down();
		BlockPos blockpos4Down = currentPos.west().down();
		
		BlockState blockstateDown = worldIn.getBlockState(blockpos1Down);
		BlockState blockstate1Down = worldIn.getBlockState(blockpos2Down);
		BlockState blockstate2Down = worldIn.getBlockState(blockpos3Down);
		BlockState blockstate3Down = worldIn.getBlockState(blockpos4Down);
		
		
		return block.getDefaultState()
				.with(FenceSlabBlock.NORTH, Boolean.valueOf(checkConnection(blockstate, blockstate.isSolidSide(worldIn, blockpos1, Direction.SOUTH), Direction.SOUTH)))
				.with(FenceSlabBlock.EAST, Boolean.valueOf(checkConnection(blockstate1, blockstate1.isSolidSide(worldIn, blockpos2, Direction.WEST), Direction.WEST)))
				.with(FenceSlabBlock.SOUTH, Boolean.valueOf(checkConnection(blockstate2, blockstate2.isSolidSide(worldIn, blockpos3, Direction.NORTH), Direction.NORTH)))
				.with(FenceSlabBlock.WEST, Boolean.valueOf(checkConnection(blockstate3, blockstate3.isSolidSide(worldIn, blockpos4, Direction.EAST), Direction.EAST)))
				
				.with(FenceSlabBlock.NORTH_UP, Boolean.valueOf(checkConnectionUpDown(blockstate)))
				.with(FenceSlabBlock.EAST_UP, Boolean.valueOf(checkConnectionUpDown(blockstate1)))
				.with(FenceSlabBlock.SOUTH_UP, Boolean.valueOf(checkConnectionUpDown(blockstate2)))
				.with(FenceSlabBlock.WEST_UP, Boolean.valueOf(checkConnectionUpDown(blockstate3)))
				
				.with(FenceSlabBlock.NORTH_DOWN, Boolean.valueOf(checkConnectionUpDown(blockstateDown)))
				.with(FenceSlabBlock.EAST_DOWN, Boolean.valueOf(checkConnectionUpDown(blockstate1Down)))
				.with(FenceSlabBlock.SOUTH_DOWN, Boolean.valueOf(checkConnectionUpDown(blockstate2Down)))
				.with(FenceSlabBlock.WEST_DOWN, Boolean.valueOf(checkConnectionUpDown(blockstate3Down)));
	}
	
	public static boolean checkConnection(BlockState blockState, boolean solidWall, Direction facing) {
		Block block = blockState.getBlock();
		boolean flag = block instanceof FenceSlabBlock;// .isIn(BlockTags.FENCES) && blockState.getMaterial() == this.material;
		boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.isParallel(blockState, facing);
		return !Block.cannotAttach(block) && solidWall || flag || flag1;
	}
	
	public static boolean checkConnectionUpDown(BlockState blockState) {
		Block block = blockState.getBlock();
		boolean flag = block.isIn(BlockTags.FENCES) && blockState.getMaterial() == Material.WOOD;
		//boolean flag1 = block instanceof FenceGateBlock && FenceGateBlock.isParallel(blockState, facing);
		return flag;//!cannotAttach(block) && solidWall || flag || flag1;
	}
	
	public static void registerPlaceEntry(ResourceLocation itemName, Block block)
	{
		if(!PLACE_ENTRIES.containsKey(itemName))
		{
			PLACE_ENTRIES.put(itemName, block);
		}
			
	}

	public static Collection<Block> getPlaceEntryBlocks()
	{
		return PLACE_ENTRIES.values();
	}
}
