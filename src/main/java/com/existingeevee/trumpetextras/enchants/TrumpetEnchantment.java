package com.existingeevee.trumpetextras.enchants;

import com.jamieswhiteshirt.trumpetskeleton.items.TrumpetItem;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class TrumpetEnchantment extends Enchantment {

	public static final EnchantmentCategory TRUMPET_CATEGORY = EnchantmentCategory.create("TRUMPET", item -> item instanceof TrumpetItem);

	private int maxLevel = 1;

	public TrumpetEnchantment(Rarity pRarity, int maxLevel) {
		super(pRarity, TRUMPET_CATEGORY, new EquipmentSlot[] { EquipmentSlot.MAINHAND });
		this.maxLevel = maxLevel;
	}

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}
}
