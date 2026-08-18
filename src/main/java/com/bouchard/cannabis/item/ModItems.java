package com.bouchard.cannabis.item;

import java.util.function.Function;

import com.bouchard.cannabis.item.custom.CannabisJoint;
import com.bouchard.cannabis.item.custom.CannabisSeeds;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

public class ModItems{

    public static final Item CANNABIS_BUD = register(ModItemIds.CANNABIS_BUD, Item::new, new Item.Properties());
    public static final Item CANNABIS_SEEDS = register(ModItemIds.CANNABIS_SEEDS, CannabisSeeds::new, new Item.Properties());
    
    // Consumables

    // Joint
    static Holder<SoundEvent> inhaleSound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.BREEZE_INHALE);
    public static final Consumable MARIJUANA_POOR_CONSUMABLE_COMPONENT = Consumable.builder()
        .hasConsumeParticles(false)
        .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.RESISTANCE, 20 * 120, 1), 1.0f))
        .onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.NAUSEA, 20 * 120, 2), 1.0f))
        .consumeSeconds(1.3f)
        .animation(ItemUseAnimation.EAT)
        .sound(inhaleSound)
        .build();
    public static final FoodProperties MARIJUANA_POOR_FOOD_COMPONENT = new FoodProperties.Builder().alwaysEdible().build();
    public static final Item CANNABIS_JOINT = register(ModItemIds.CANNABIS_JOINT, CannabisJoint::new, new Item.Properties().useCooldown(2f).food(MARIJUANA_POOR_FOOD_COMPONENT, MARIJUANA_POOR_CONSUMABLE_COMPONENT));
    
    // End Consumables

    public static Item register(ResourceKey<Item> itemKey, Function<Item.Properties, Item> itemFactory, Item.Properties settings){
        // Create the item instance
        Item item = itemFactory.apply(settings.setId(itemKey));

        Registry.register(BuiltInRegistries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize(){

        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.INGREDIENTS)
        .register(entries -> {
            entries.accept(CANNABIS_BUD);
            entries.accept(CANNABIS_SEEDS);
        });
        CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.FOOD_AND_DRINKS)
        .register(entries -> {
            entries.accept(CANNABIS_JOINT);
        });

    }

}