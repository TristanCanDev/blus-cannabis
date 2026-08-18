package com.bouchard.cannabis.block;

import com.bouchard.cannabis.BluSCannabis;

import net.minecraft.references.BlockItemId;
import net.minecraft.resources.Identifier;

public class ModBlockItemIds {
    private static BlockItemId create(String name){
        Identifier id = Identifier.fromNamespaceAndPath(BluSCannabis.MOD_ID, name);
        return BlockItemId.create(id, id);
    }
}
