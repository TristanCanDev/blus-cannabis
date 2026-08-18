package com.bouchard.cannabis.item;

import com.bouchard.cannabis.BluSCannabis;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

public class ModItemIds {
    
    public static final ResourceKey<Item> CANNABIS_BUD = create("cannabis_bud");
    public static final ResourceKey<Item> CANNABIS_JOINT = create("cannabis_joint");
    public static final ResourceKey<Item> CANNABIS_SEEDS = create("cannabis_seeds");
    
    public static ResourceKey<Item> create(String name){
        // Create item keys
        return ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(BluSCannabis.MOD_ID, name));
    }
}