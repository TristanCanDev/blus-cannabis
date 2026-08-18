package com.bouchard.cannabis.block.custom;

import org.jspecify.annotations.Nullable;

import com.bouchard.cannabis.block.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Orientation;

public class CannabisPlantTopBlock extends Block{
    
    public CannabisPlantTopBlock(final BlockBehaviour.Properties properties){
        super(properties);
    }

    @Override
    protected void neighborChanged(final BlockState state, final Level level, final BlockPos pos, final Block block, final @Nullable Orientation orientation, final boolean movedByPiston) {
        if(!level.getBlockState(pos.below()).is(ModBlocks.CANNABIS_PLANT)){
            level.destroyBlock(pos, true);
        }
        super.neighborChanged(state, level, pos, block, orientation, movedByPiston);
    }

}
