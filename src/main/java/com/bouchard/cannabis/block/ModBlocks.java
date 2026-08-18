package com.bouchard.cannabis.block;

import java.util.function.Function;

import com.bouchard.cannabis.block.custom.CannabisPlantBlock;
import com.bouchard.cannabis.block.custom.CannabisPlantTopBlock;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.references.BlockItemId;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    private static Block register(BlockItemId id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties){
        // Create block instance
        Block block = register(id.block(), blockFactory, properties);

        // Create the block item instance
        BlockItem blockItem = new BlockItem(block, new Item.Properties().useBlockDescriptionPrefix().setId(id.item()));
        Registry.register(BuiltInRegistries.ITEM, id.item(), blockItem);

        return block;
    }

    private static Block register(ResourceKey<Block> id, Function<BlockBehaviour.Properties, Block> blockFactory, BlockBehaviour.Properties properties){
        // Create the block instance
        Block block = blockFactory.apply(properties.setId(id));

        return Registry.register(BuiltInRegistries.BLOCK, id, block);
    }

    public static final Block CANNABIS_PLANT = register(ModBlockIds.CANNABIS_PLANT, CannabisPlantBlock::new, BlockBehaviour.Properties.of().sound(SoundType.WET_GRASS).noCollision());
    public static final Block CANNABIS_PLANT_TOP = register(ModBlockIds.CANNABIS_PLANT_TOP, CannabisPlantTopBlock::new, BlockBehaviour.Properties.of().sound(SoundType.WET_GRASS).noCollision());

    public static void initialize(){

    }
}
