package com.existingeevee.trumpetextras.enchants;

import com.jamieswhiteshirt.trumpetskeleton.TrumpetSkeleton;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantment.Rarity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {

	public static final DeferredRegister<Enchantment> REGISTER = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, TrumpetSkeleton.MOD_ID);

	public static final RegistryObject<Enchantment> DEAFENING = REGISTER.register("deafening", () -> new TrumpetEnchantment(Rarity.UNCOMMON, 4));
	
}
