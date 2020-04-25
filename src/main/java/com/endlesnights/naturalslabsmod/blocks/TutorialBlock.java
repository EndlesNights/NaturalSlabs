package com.endlesnights.naturalslabsmod.blocks;

import java.util.Random;

import com.endlesnights.naturalslabsmod.NaturalSlabsMod;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;


@EventBusSubscriber(modid=NaturalSlabsMod.MODID, bus=Bus.MOD, value=Dist.CLIENT)
public class TutorialBlock extends Block 
{
	
	public TutorialBlock()
	{
		super(Block.Properties.create(Material.ORGANIC).tickRandomly().hardnessAndResistance(0.6F).sound(SoundType.PLANT));
		this.setDefaultState(this.stateContainer.getBaseState());
		
		this.setRegistryName(NaturalSlabsMod.MODID, "tutorial_block");
		
	}
	
	@Override //Random Tick
	public void tick(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random)
	{
		super.tick(blockState, world, blockPos, random);//Supertick
		//grassGrow(blockState, world, blockPos, random);
		//System.out.println("TEST ONE");
		
	}
	
//	@Override //Tick
//	public void func_225542_b_(BlockState blockState, ServerWorld world, BlockPos blockPos, Random random)
//	{
//		
//		//System.out.println("TEST TWO");
//		
//	}
	
    @Override
    public boolean isToolEffective(BlockState state, ToolType tool)
    {
    	return tool == ToolType.SHOVEL;
    }
    
  //On Block Activated
    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
    	
		ItemStack itemStack = playerIn.getHeldItem(hand);
		if (itemStack.getItem() instanceof HoeItem && !worldIn.isRemote)
        	return onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
		else if (itemStack.getItem() instanceof ShovelItem && !worldIn.isRemote)
			return onItemUseSpade(state, worldIn, pos, playerIn, hand, p_225533_6_);
			
//		else
//			return super.func_225533_a_(state, worldIn, pos, playerIn, hand, p_225533_6_);
		
//        if(!worldIn.isRemote)
//        {
//	        playerIn.sendMessage(new StringTextComponent("RIGHT-CLICKED!!!"));
//	        //return ActionResultType.SUCCESS;
//    		ItemStack itemStack = playerIn.getHeldItem(hand);
//    		if (itemStack.getItem() instanceof HoeItem)
//	        	return onItemUseHoe(state, worldIn, pos, playerIn, hand, p_225533_6_);
//    		else
//    			return super.func_225533_a_(state, worldIn, pos, playerIn, hand, p_225533_6_);
//        }    
//        
        return ActionResultType.FAIL;
        
    }
    public boolean Test()
    {
    	return false || false;
    }
    public static ActionResultType onItemUseHoe(BlockState state,  World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
		
		ItemUseContext context = new ItemUseContext(playerIn, hand, p_225533_6_);
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		
		if (context.getFace() != Direction.DOWN && world.getBlockState(blockpos.up()).isAir())
		{
		    BlockState blockstate = Blocks.FARMLAND.getDefaultState();
		    
		    if (blockstate != null)
		    {
				PlayerEntity playerentity = context.getPlayer();
				world.playSound(playerentity, blockpos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);			    
				world.setBlockState(blockpos, blockstate);
			    if (playerentity != null)
			    {
				context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
					    p_220043_1_.sendBreakAnimation(context.getHand());
					});
			    }
			    return ActionResultType.SUCCESS;
		    }
		}
    	return ActionResultType.FAIL;
    }
    
    public static ActionResultType onItemUseSpade(BlockState state,  World worldIn, BlockPos pos,
            PlayerEntity playerIn, Hand hand, BlockRayTraceResult p_225533_6_)
    {
		
		ItemUseContext context = new ItemUseContext(playerIn, hand, p_225533_6_);
		World world = context.getWorld();
		BlockPos blockpos = context.getPos();
		
		if (context.getFace() != Direction.DOWN && world.getBlockState(blockpos.up()).isAir())
		{
		    BlockState blockstate = Blocks.GRASS_PATH.getDefaultState();
		    
		    if (blockstate != null)
		    {
				PlayerEntity playerentity = context.getPlayer();
				world.playSound(playerentity, blockpos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);			    
				world.setBlockState(blockpos, blockstate);
			    if (playerentity != null)
			    {
				context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
					    p_220043_1_.sendBreakAnimation(context.getHand());
					});
			    }
			    return ActionResultType.SUCCESS;
		    }
		}
    	return ActionResultType.FAIL;
    }
	
    public void grassGrow(BlockState blockState, World world, BlockPos blockPos, Random random)
    {
        for(int x = -1; x <= 1; x++)
        {
        	for(int y = -1; y <= 1; y++)
        	{
        		for(int z = -1; z <= 1; z++)
        		{
        			if(x == 0 && y == 0 & z ==0)//This is the block, no need to read this.
        				continue;
        			
        			if(random.nextInt(10) == 0)
        			{
        				BlockPos check = blockPos.add(x, y, z);
        				
        				if(world.getBlockState(check).getBlock() == Blocks.GRASS_BLOCK && world.getBlockState(blockPos.up()).isAir())
        				{
        					
        					//world.removeBlock(blockPos, false);
        					world.setBlockState(blockPos, Blocks.GRASS_BLOCK.getDefaultState());
        					//System.out.println("IS THIS WORLD REMOTE?" + this.ticksRandomly(getDefaultState()));
        					//this.ticksRandomly(getDefaultState());

        					
        					//System.out.println( blockState.getLightValue(world, blockPos));
        					//System.out.println(world.getLight(blockPos.add(0, 1, 0)));        					
        				}
        			}
        		}
        	}
        }
    }
    //@Override
//	public void grow(IWorld worldIn, Random rand, BlockPos pos, BlockState state)
//	{
//    	
//    }
	
//    @Override
//    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
//        if (state.getBlock() != newState.getBlock()) {
//        	
//        }
//        super.onReplaced(state, worldIn, pos, newState, isMoving);
//    }
    
//	@Override
//	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos currentPos, BlockPos facingPos)
//	{
//		return facing == Direction.UP && !isValidPosition(state, world, currentPos) ? Blocks.AIR.getDefaultState() : super.updatePostPlacement(state, facing, facingState, world, currentPos, facingPos);
//	}
	
    @Override
    public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction facing, IPlantable plantable)
    {
    	BlockState plant = plantable.getPlant(world, pos.offset(facing));
    	
    	if (plant.getBlock() == Blocks.SUGAR_CANE ) //Sugarcane was not working with super grass, placed it in on my own
    	{
    		if(world.getFluidState(pos.add(1,0,0)).isTagged(FluidTags.WATER))
    			return true;
    		else if(world.getFluidState(pos.add(-1,0,0)).isTagged(FluidTags.WATER))
    			return true;
    		else if(world.getFluidState(pos.add(0,0,1)).isTagged(FluidTags.WATER))
    			return true;
    		else if(world.getFluidState(pos.add(1,0,-1)).isTagged(FluidTags.WATER))
    			return true;
    		
    		return false;
    	}
    	else if(plant.getBlock() == Blocks.BAMBOO_SAPLING  || plant.getBlock() == Blocks.BAMBOO )
    	{
    		return true;
    	}

    	return super.canSustainPlant(Blocks.GRASS_BLOCK.getDefaultState(), world, pos, facing, plantable);
//        BlockState plant = plantable.getPlant(world, pos.offset(facing));
//
//        if (plant.getBlock() == Blocks.SUGAR_CANE && this == Blocks.SUGAR_CANE)
//            return true;
//        if (plantable instanceof BushBlock)
//            return true;
//        
//        return false;
    	
//    	PlantType type = plantable.getPlantType(world, pos.offset(facing));
//    	// TODO change this to test each tag (flowers, mushrooms, etc...) as they become
//    	// available
//    	ResourceLocation tagId = new ResourceLocation(NaturalSlabsMod.MODID, "natural_slabs_plants");
//
//    	boolean isPlantableOnGrass = BlockTags.getCollection().getOrCreate(tagId).contains((Block) plantable);
//
//    	return isPlantableOnGrass || type == PlantType.Crop || super.canSustainPlant(state, world, pos, facing, plantable);
    }


//	@Override
//	public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient)
//	{
//		// TODO Auto-generated method stub
//		return worldIn.getBlockState(pos.up()).isAir();
//	}
//
//
//	@Override
//	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state)
//	{
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//
//	@Override //Grass & Flower Growing Function!
//	public void func_225535_a_(ServerWorld worldIn, Random rand, BlockPos pos,
//			BlockState blockState)
//	{
//		// TODO Auto-generated method stub
//	      BlockPos blockpos = pos.up();
//	      BlockState blockstate = Blocks.GRASS.getDefaultState();
//
//	      for(int i = 0; i < 128; ++i) {
//	         BlockPos blockpos1 = blockpos;
//	         int j = 0;
//
//	         while(true) {
//	            if (j >= i / 16)
//	            {
//	               BlockState blockstate2 = worldIn.getBlockState(blockpos1);
//	               
//	               if (blockstate2.getBlock() == blockstate.getBlock() && rand.nextInt(10) == 0)
//	               {
//
//	                  ((IGrowable)blockstate.getBlock()).func_225535_a_(worldIn, rand, blockpos1, blockstate2);
//	               }
//
//	               if (!blockstate2.isAir()) {
//	                  break;
//	               }
//
//	               BlockState blockstate1;
//	               if (rand.nextInt(8) == 0) {
//	                  List<ConfiguredFeature<?, ?>> list = worldIn.func_226691_t_(blockpos1).getFlowers();
//	                  if (list.isEmpty()) {
//	                     break;
//	                  }
//
//	                  ConfiguredFeature<?, ?> configuredfeature = ((DecoratedFeatureConfig)(list.get(0)).config).feature;
//	                  blockstate1 = ((FlowersFeature)configuredfeature.feature).func_225562_b_(rand, blockpos1, configuredfeature.config);
//	               } else {
//	                  blockstate1 = blockstate;
//	               }
//
//	               if (blockstate1.isValidPosition(worldIn, blockpos1)) {
//	                  worldIn.setBlockState(blockpos1, blockstate1, 3);
//	               }
//	               break;
//	            }
//
//	            blockpos1 = blockpos1.add(rand.nextInt(3) - 1, (rand.nextInt(3) - 1) * rand.nextInt(3) / 2, rand.nextInt(3) - 1);
//	            if (worldIn.getBlockState(blockpos1.down()).getBlock() != this && worldIn.getBlockState(blockpos1.down()).getBlock() != Blocks.GRASS_BLOCK.getBlock() || worldIn.getBlockState(blockpos1).func_224756_o(worldIn, blockpos1))
//	            {
//	            	
//	               break;
//	            }
//
//	            ++j;
//	         }
//	      }
//		
//	}
    
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance)
    {
    	System.out.println("\nFALL DISTANCE: " + fallDistance);
    	fallDistance /=2;
    	super.onFallenUpon(worldIn, pos, entityIn, fallDistance);
    	
     }
	

}
