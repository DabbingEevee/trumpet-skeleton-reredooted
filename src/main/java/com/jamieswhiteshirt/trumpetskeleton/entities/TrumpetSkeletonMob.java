package com.jamieswhiteshirt.trumpetskeleton.entities;

import com.jamieswhiteshirt.trumpetskeleton.entities.goals.TrumpetAttackGoal;
import com.jamieswhiteshirt.trumpetskeleton.register.Items;
import com.jamieswhiteshirt.trumpetskeleton.register.SoundEvents;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class TrumpetSkeletonMob extends Skeleton {
	private final TrumpetAttackGoal<TrumpetSkeletonMob> trumpetAttackGoal = new TrumpetAttackGoal<>(this, 1, 40, 6);
	private final MeleeAttackGoal meleeAttackGoal = new MeleeAttackGoal(this, 1.2, false) {
		@Override
		public void start() { //Skeleton
			super.start();
			setAggressive(true);
		}

		@Override
		public void stop() {
			super.stop();
			setAggressive(false);
		}
	};

	private final boolean constructed;

	public TrumpetSkeletonMob(EntityType<? extends Skeleton> p_i50194_1_, Level p_i50194_2_) {
		super(p_i50194_1_, p_i50194_2_);

		constructed = true;
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource pRandom, DifficultyInstance pDifficulty) {
		super.populateDefaultEquipmentSlots(pRandom, pDifficulty);
		this.startUsingItem(InteractionHand.MAIN_HAND);
		this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.TRUMPET_ITEM.get()));
		
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.SKELETON_DOOT.get();
	}

	@Override
	public void playAmbientSound() {
		if (!isAggressive())
			super.playAmbientSound();
	}

	@Override
	public void reassessWeaponGoal() {
		if (constructed && this.level != null && !this.level.isClientSide) {
			goalSelector.removeGoal(meleeAttackGoal);
			goalSelector.removeGoal(trumpetAttackGoal);

			ItemStack stack = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, i -> i == Items.TRUMPET_ITEM.get()));

			if (stack.getItem() == Items.TRUMPET_ITEM.get()) {
				int attackInterval = 40;

				if (level.getDifficulty() != Difficulty.HARD) {
					attackInterval = 80;
				}

				trumpetAttackGoal.setAttackInterval(attackInterval);
				goalSelector.addGoal(4, trumpetAttackGoal);
			} else {
				goalSelector.addGoal(4, meleeAttackGoal);
			}
		}
	}
}
