package com.bouchard.cannabis.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.level.Level;

public class CannabisJoint extends Item{
    
    public CannabisJoint(Properties properties){
        super(properties);
    }

    @Override
    public InteractionResult use(final Level level, final Player player, final InteractionHand hand){

        // Some stuff to do the consumable check.. I'm pretty sure I need this to keep the cooldown working..
        //  I'm not 100% sure if I need it because I haven't tried this without it.. 
        ItemStack stack = player.getItemInHand(hand);
        Consumable consumable = stack.get(DataComponents.CONSUMABLE);
        if(consumable != null) {
            // I don't really know if this check is necessary.. It works in singleplayer and I'd assume it works multiplayer as well..?
            if(level instanceof ServerLevel){
                // Ensure the particles are spawned serverwide. Because this isn't a block, it needs to send the particles to the server
                //  so everyone else may see them generate upon item use
                ((ServerLevel) level).sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, player.getX(), player.getY() + 2.0, player.getZ(), 20, 0.001, 0.05, 0.001, 0.01);
            }
        }

        return super.use(level, player, hand);
    }

}
