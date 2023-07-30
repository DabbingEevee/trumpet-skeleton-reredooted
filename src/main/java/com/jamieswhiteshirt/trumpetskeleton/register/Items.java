package com.jamieswhiteshirt.trumpetskeleton.register;

import com.jamieswhiteshirt.trumpetskeleton.TrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.items.TrumpetItem;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Items {
	public static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, TrumpetSkeleton.MOD_ID);

	public static final RegistryObject<Item> TRUMPET_ITEM = REGISTER.register("trumpet", () -> new TrumpetItem(new Item.Properties().tab(CreativeModeTab.TAB_MISC).durability(200)));

	public static final RegistryObject<Item> TRUMPET_SKELETON_SPAWN_EGG = REGISTER.register(
			"trumpet_skeleton_spawn_egg",

			() -> new ForgeSpawnEggItem(
					Entities.TRUMPET_SKELETON_ENTITY,
					0xC1C1C1,
					0xFCFC00,
					new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
}
