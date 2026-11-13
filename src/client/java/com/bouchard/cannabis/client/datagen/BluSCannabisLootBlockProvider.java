package com.bouchard.cannabis.client.datagen;

import java.util.concurrent.CompletableFuture;

import com.bouchard.cannabis.block.ModBlocks;
import com.bouchard.cannabis.block.custom.CannabisPlantBlock;
import com.bouchard.cannabis.item.ModItems;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootSubProvider;
import net.minecraft.advancements.predicates.StatePropertiesPredicate.Builder;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchBlock;

public class BluSCannabisLootBlockProvider extends FabricBlockLootSubProvider{
    public BluSCannabisLootBlockProvider(FabricPackOutput dataOutput, CompletableFuture<HolderLookup.Provider> registryLookup){
        super(dataOutput, registryLookup);
    }

    
    @Override
    public void generate(){
        
        // Temporary fix to the issue of weird drops. Should now drop like wheat. Not exactly ideal as I'd like more bud to drop than seeds.. we shall modify later
        LootItemCondition.Builder matureCond = MatchBlock.blockMatches(blocks, ModBlocks.CANNABIS_PLANT, Builder.properties().hasProperty(CannabisPlantBlock.AGE, 3));
        add(ModBlocks.CANNABIS_PLANT, createCropDrops(ModBlocks.CANNABIS_PLANT, ModItems.CANNABIS_BUD, ModItems.CANNABIS_SEEDS, matureCond));
        
    }
}
