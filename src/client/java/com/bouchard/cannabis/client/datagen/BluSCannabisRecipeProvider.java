package com.bouchard.cannabis.client.datagen;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;

import com.bouchard.cannabis.BluSCannabis;
import com.bouchard.cannabis.item.ModItems;
import com.bouchard.cannabis.item.custom.CannabisJoint;

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
