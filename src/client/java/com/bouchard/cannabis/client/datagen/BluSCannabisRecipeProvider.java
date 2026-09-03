package com.bouchard.cannabis.client.datagen;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;

import com.bouchard.cannabis.item.ModItems;

public class BluSCannabisRecipeProvider extends FabricRecipeProvider{
    
    public BluSCannabisRecipeProvider (FabricPackOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture){
        super(output, registriesFuture);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registryLookup, RecipeOutput exporter){
        return new RecipeProvider(registryLookup, exporter){
            @Override
            public void buildRecipes(){
                HolderLookup.RegistryLookup<Item> itemLookup = registries.lookupOrThrow(Registries.ITEM);

                shapeless(RecipeCategory.FOOD, ModItems.CANNABIS_JOINT)
                    .requires(ModItems.CANNABIS_BUD)
                    .requires(Items.PAPER)
                    .unlockedBy(getHasName(ModItems.CANNABIS_BUD), has(ModItems.CANNABIS_BUD))
                    .save(output);
            }
        };
    }

    @Override
    public String getName(){
        return "BluSCannabisRecipeProvider";
    }

}
