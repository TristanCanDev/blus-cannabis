package com.bouchard.cannabis.block.custom;

import org.jspecify.annotations.Nullable;

import com.bouchard.cannabis.block.ModBlocks;
import com.bouchard.cannabis.item.ModItems;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
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
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
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

    // Easy helper for determining where the majority of light is coming from..
    protected enum LightType {
        SKY,
        BLOCK
    }

    // Helper function for quickly determining whether the majority light comes from sky or block..
    protected static LightType getLightType(final ServerLevel level, final BlockPos pos){
        int blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        int skyLight = level.getBrightness(LightLayer.SKY, pos);

        if(blockLight > skyLight){
            return LightType.BLOCK;
        } else {
            return LightType.SKY;
        }

    }

    protected static boolean isSuitableEnvironment(final ServerLevel level, final BlockPos pos){
        Holder<Biome> currentBiome = level.getBiome(pos);
        
        // Unsuitible
        if(currentBiome.is(Biomes.BADLANDS) || currentBiome.is(Biomes.BASALT_DELTAS) || currentBiome.is(Biomes.BEACH) || currentBiome.is(Biomes.COLD_OCEAN) || currentBiome.is(Biomes.CRIMSON_FOREST) || currentBiome.is(Biomes.DEEP_COLD_OCEAN) || currentBiome.is(Biomes.DEEP_DARK) || currentBiome.is(Biomes.DEEP_FROZEN_OCEAN) || currentBiome.is(Biomes.DESERT) || currentBiome.is(Biomes.DRIPSTONE_CAVES) || currentBiome.is(Biomes.END_BARRENS) || currentBiome.is(Biomes.END_HIGHLANDS) || currentBiome.is(Biomes.END_MIDLANDS) || currentBiome.is(Biomes.ERODED_BADLANDS) || currentBiome.is(Biomes.FROZEN_OCEAN) || currentBiome.is(Biomes.FROZEN_PEAKS) || currentBiome.is(Biomes.FROZEN_RIVER) || currentBiome.is(Biomes.ICE_SPIKES) || currentBiome.is(Biomes.JAGGED_PEAKS) || currentBiome.is(Biomes.NETHER_WASTES) || currentBiome.is(Biomes.SNOWY_BEACH) || currentBiome.is(Biomes.SNOWY_PLAINS) || currentBiome.is(Biomes.SNOWY_SLOPES) || currentBiome.is(Biomes.SNOWY_TAIGA) || currentBiome.is(Biomes.SOUL_SAND_VALLEY) || currentBiome.is(Biomes.STONY_PEAKS) || currentBiome.is(Biomes.STONY_SHORE) || currentBiome.is(Biomes.SULFUR_CAVES) || currentBiome.is(Biomes.THE_END) || currentBiome.is(Biomes.THE_VOID) || currentBiome.is(Biomes.WARPED_FOREST) || currentBiome.is(Biomes.WOODED_BADLANDS)){
            return false;
        }

        // Otherwise generally suitable
        return true;

    }

    protected static float getGrowthSpeed(final Block type, final ServerLevel level, final BlockPos pos) {
		// We'll set some speed here.. this method is required for cropblocks I think..  
        
        // This was 11 before.. way fast but may be fairer in enclosed spaces with artificial lighting
        float speed = 1.0F;
        
        

		return speed;
	}

    @Override
	protected boolean canSurvive(final BlockState state, final LevelReader level, final BlockPos pos) {
		return hasSufficientLight(level, pos) && super.canSurvive(state, level, pos);
	}

    protected static boolean hasSufficientLight(final LevelReader level, final BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 10;
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
