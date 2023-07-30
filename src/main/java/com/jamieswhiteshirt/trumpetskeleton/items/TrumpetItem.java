package com.jamieswhiteshirt.trumpetskeleton.items;

import java.util.List;

import com.existingeevee.trumpetextras.enchants.ModEnchantments;
import com.jamieswhiteshirt.trumpetskeleton.register.Items;
import com.jamieswhiteshirt.trumpetskeleton.register.SoundEvents;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrumpetItem extends Item {
	public TrumpetItem(Properties properties) {
		super(properties);
	}

	public static void scare(Level world, LivingEntity user) {
		if (!world.isClientSide) {
			List<LivingEntity> spooked = world.getEntitiesOfClass(LivingEntity.class, user.getBoundingBox().inflate(10.0));

			for (LivingEntity entity : spooked) {
				if (entity == user)
					continue;

				double deltaX = entity.position().x - user.position().x + world.random.nextDouble() - world.random.nextDouble();
				double deltaZ = entity.position().z - user.position().z + world.random.nextDouble() - world.random.nextDouble();

				double distance = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

				//ExistingEevee start
				float damage = 0;
				float knockback = 1;

				ItemStack stack = user.getItemInHand(ProjectileUtil.getWeaponHoldingHand(user, i -> i == Items.TRUMPET_ITEM.get()));

				if (stack != null && !stack.isEmpty()) {
					int knockbackLevel = stack.getEnchantmentLevel(Enchantments.KNOCKBACK);
					if (knockbackLevel > 0) {
						knockback += (0.25 * knockbackLevel);
					}

					int deafeningLevel = stack.getEnchantmentLevel(ModEnchantments.DEAFENING.get());
					if (deafeningLevel > 0) {
						damage += deafeningLevel * 0.5f;
					}
				}

				Vec3 delta = entity.getDeltaMovement(); //We have this here to prevent the entity.hurt from messing with this

				if (damage > 0) {
					entity.hurt(new EntityDamageSource("trumpet_damage", user), damage * (1f - (float) distance / 10));
				}

				entity.setDeltaMovement(delta.add(0.5 * deltaX * knockback / distance, 5 * knockback / (10 + distance), 0.5 + deltaZ * knockback / distance));
				//ExistingEevee end

				entity.hurtMarked = true;
				entity.setLastHurtByMob(user);
			}
		}
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.DRINK;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 55;
	}

	@Override
	public SoundEvent getDrinkingSound() {
		return null;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
		super.onUsingTick(stack, player, count);

		int useTime = getUseDuration(stack) - count;

		if (useTime == 10) {
			player.playSound(SoundEvents.TRUMPET_DOOT.get(), 1, 0.9F + player.level.random.nextFloat() * 0.2F);
			TrumpetItem.scare(player.level, player);
			stack.hurtAndBreak(1, player, (entity) -> entity.broadcastBreakEvent(entity.getUsedItemHand()));
		} else if (useTime >= 15) {
			player.stopUsingItem();
		}
	}

	@Override
	public int getEnchantmentValue(ItemStack stack) {
		return 14; 
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
		pPlayer.startUsingItem(pHand);
		return InteractionResultHolder.success(pPlayer.getItemInHand(pHand));
	}

	//ExistingEevee Start

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment == Enchantments.KNOCKBACK;
	}
}
