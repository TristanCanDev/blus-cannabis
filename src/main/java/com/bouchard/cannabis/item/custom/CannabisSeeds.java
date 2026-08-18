package com.bouchard.cannabis.item.custom;

import com.bouchard.cannabis.block.ModBlocks;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

public class CannabisSeeds extends Item {
    
    public CannabisSeeds(Properties properties){
        super(properties);
    }

    @Override
    public InteractionResult useOn(final UseOnContext context){
        
        if(context.getLevel().getBlockState(context.getClickedPos()).is(BlockTags.SUPPORTS_VEGETATION)){
            context.getLevel().setBlock(context.getClickedPos().above(), ModBlocks.CANNABIS_PLANT.defaultBlockState(), 3);
            context.getLevel().playLocalSound(context.getClickedPos().above(), SoundEvents.GRASS_PLACE, SoundSource.BLOCKS, 1f, 1f, false);
            context.getPlayer().swing(context.getHand());
            context.getItemInHand().shrink(1);
        }

        return super.useOn(context);
    }

}
