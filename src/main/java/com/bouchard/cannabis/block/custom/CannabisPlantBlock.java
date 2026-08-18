package com.bouchard.cannabis.block.custom;

import org.jspecify.annotations.Nullable;

import com.bouchard.cannabis.block.ModBlocks;
import com.bouchard.cannabis.item.ModItems;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmlandBlock;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.redstone.Orientation;

public class CannabisPlantBlock extends VegetationBlock implements BonemealableBlock{
    public static final MapCodec<CannabisPlantBlock> CODEC = simpleCodec(CannabisPlantBlock::new);
    public static final int MAX_AGE = 3;
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;


    @Override
    public MapCodec<CannabisPlantBlock> codec(){
        return CODEC;
    }

    public CannabisPlantBlock(final BlockBehaviour.Properties properties){
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0));
    }

    protected IntegerProperty getAgeProperty(){
        return AGE;
    }

    public int getMaxAge(){
        return 3;
    }

    public int getAge(final BlockState state){
        return state.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(final int age){
        return this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }

    public final boolean isMaxAge(final BlockState state){
        return this.getAge(state) >= this.getMaxAge();
    }

    @Override
    protected boolean isRandomlyTicking(final BlockState state){
        return !this.isMaxAge(state);
    }

    @Override
    protected void randomTick(final BlockState state, final ServerLevel level, final BlockPos pos, final RandomSource random){
        boolean aboveAir = level.getBlockState(pos.above()).is(Blocks.AIR);
        if (level.getRawBrightness(pos, 0) >= 13) {
			int age = this.getAge(state);
			if (age < this.getMaxAge()) {
				float growthSpeed = getGrowthSpeed(this, level, pos);
				if (random.nextInt((int)(25.0F / growthSpeed) + 1) == 0) {
                    level.setBlock(pos, this.getStateForAge(age + 1), 2);
					if(age+1 == getMaxAge() && aboveAir){
                        BlockState topState = ModBlocks.CANNABIS_PLANT_TOP.defaultBlockState();
                        level.setBlock(pos.above(), topState, 3);
                    }
				}
			}
		}
    }

    @Override
    protected void neighborChanged(final BlockState state, final Level level, final BlockPos pos, final Block block, final @Nullable Orientation orientation, final boolean movedByPiston) {
        if(!level.getBlockState(pos.above()).is(ModBlocks.CANNABIS_PLANT_TOP) && this.getAge(state) >= this.getMaxAge()){
            level.destroyBlock(pos, true);
        }

        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    }

    public void growCrops(final Level level, final BlockPos pos, final BlockState state) {
		int age = Math.min(this.getMaxAge(), this.getAge(state) + this.getBonemealAgeIncrease(level));
        boolean aboveAir = level.getBlockState(pos.above()).is(Blocks.AIR);
		if(age >= this.getMaxAge() && aboveAir){
            BlockState topState = ModBlocks.CANNABIS_PLANT_TOP.defaultBlockState();
            level.setBlock(pos.above(), topState, 3);
        }

        level.setBlock(pos, this.getStateForAge(age), 2);
	}

    protected int getBonemealAgeIncrease(final Level level) {
		return Mth.nextInt(level.getRandom(), 1, 3);
	}

    protected static float getGrowthSpeed(final Block type, final BlockGetter level, final BlockPos pos) {
		float speed = 11.0F;
		return speed;
	}

    @Override
	protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
		return hasSufficientLight(level, pos) && super.canSurvive(state, level, pos);
	}

    protected static boolean hasSufficientLight(final LevelReader level, final BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 8;
	}

    protected ItemLike getBaseSeedId() {
		return ModItems.CANNABIS_SEEDS;
	}

    @Override
	protected ItemStack getCloneItemStack(final LevelReader level, final BlockPos pos, final BlockState state, final boolean includeData) {
		return new ItemStack(this.getBaseSeedId());
	}

    @Override
    public boolean isValidBonemealTarget(final LevelReader level, final BlockPos pos, final BlockState state){
        return BonemealableBlock.hasSpreadableNeighbourPos(level, pos, state);
    }

    @Override
	public boolean isBonemealSuccess(final Level level, final RandomSource random, final BlockPos pos, final BlockState state) {
		return true;
	}

    @Override
    public void performBonemeal(final ServerLevel level, final RandomSource random, final BlockPos pos, final BlockState state) {
        this.growCrops(level, pos, state);
    }

    @Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(AGE);
	}
}
