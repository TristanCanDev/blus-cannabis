package com.bouchard.cannabis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bouchard.cannabis.block.ModBlocks;
import com.bouchard.cannabis.item.ModItems;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class BluSCannabis implements ModInitializer {
	public static final String MOD_ID = "cannabis";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		ModItems.initialize();
		LOGGER.info("[cannabis] Initialized Cannabis Items");

		ModBlocks.initialize();
		LOGGER.info("[cannabis] Initialized Cannabis Blocks");

		// Modify the short grass and tall grass loot tables to include cannabis seed drops (They drop at a probability of 0.001)
		LootTableEvents.MODIFY.register((lootTable, builder, source, provider) -> {
			if((lootTable.identifier().equals(Blocks.SHORT_GRASS.getLootTable().get().identifier()) || lootTable.identifier().equals(Blocks.TALL_GRASS.getLootTable().get().identifier()) || lootTable.identifier().equals(Blocks.TALL_DRY_GRASS.getLootTable().get().identifier())) && source.isBuiltin()){
				LootPool poolBuilder = LootPool.lootPool().setRolls(new ConstantValue(1f)).add(LootItem.lootTableItem(ModItems.CANNABIS_SEEDS)).when(LootItemRandomChanceCondition.randomChance(0.001f)).build();
				builder.pool(poolBuilder);
			}
		});
		LOGGER.info("[cannabis] Updated short and tall grass loot pools for cannabis seed drops");

		LOGGER.info("[cannabis] I was gonna log something useful, but then I got high");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
