package com.bouchard.cannabis.block;

import com.bouchard.cannabis.BluSCannabis;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;

public class ModBlockIds {
    
    public static final ResourceKey<Block> CANNABIS_PLANT = create("cannabis_plant");
    public static final ResourceKey<Block> CANNABIS_PLANT_TOP = create("cannabis_plant_top");
    
    private static ResourceKey<Block> create(String name){
        Identifier id = Identifier.fromNamespaceAndPath(BluSCannabis.MOD_ID, name);
        return ResourceKey.create(Registries.BLOCK, id);
    }

}
