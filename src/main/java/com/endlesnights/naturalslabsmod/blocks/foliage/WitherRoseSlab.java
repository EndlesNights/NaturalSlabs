package com.endlesnights.naturalslabsmod.blocks.foliage;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WitherRoseBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.Difficulty;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class WitherRoseSlab extends WitherRoseBlock
{
	protected static final VoxelShape SHAPE = Block.makeCuboidShape(5.0D, -8.0D, 5.0D, 11.0D, 2.0D, 11.0D);
	
	   public WitherRoseSlab(Effect effectIn, Block.Properties propertiesIn) {
		      super(effectIn, propertiesIn);
		   }

	protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
		Block block = state.getBlock();
		return super.isValidGround(state, worldIn, pos) || block == Blocks.NETHERRACK || block == Blocks.SOUL_SAND;
	}

		   /**
		    * Called periodically clientside on blocks near the player to show effects (like furnace fire particles). Note that
		    * this method is unrelated to {@link randomTick} and {@link #needsRandomTick}, and will always be called regardless
		    * of whether the block can receive random update ticks
		    */
	
	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		VoxelShape voxelshape = this.getShape(stateIn, worldIn, pos, ISelectionContext.dummy());
		Vec3d vec3d = voxelshape.getBoundingBox().getCenter();
		double d0 = (double)pos.getX() + vec3d.x;
		double d1 = (double)pos.getZ() + vec3d.z;

		for(int i = 0; i < 3; ++i) {
			if (rand.nextBoolean()) {
				worldIn.addParticle(ParticleTypes.SMOKE, d0 + (double)(rand.nextFloat() / 5.0F), (double)pos.getY() + (0.5D - (double)rand.nextFloat()), d1 + (double)(rand.nextFloat() / 5.0F), 0.0D, 0.0D, 0.0D);
			}
		}

	}

	public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
		if (!worldIn.isRemote && worldIn.getDifficulty() != Difficulty.PEACEFUL) {
			if (entityIn instanceof LivingEntity) {
				LivingEntity livingentity = (LivingEntity)entityIn;
				if (!livingentity.isInvulnerableTo(DamageSource.WITHER)) {
					livingentity.addPotionEffect(new EffectInstance(Effects.WITHER, 40));
				}
			}

		}
	}
	
	@Override
	public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player)
	{
		return new ItemStack(Items.WITHER_ROSE);
	}

}
