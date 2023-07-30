package com.jamieswhiteshirt.trumpetskeleton.items;

import java.util.List;

import com.jamieswhiteshirt.trumpetskeleton.register.SoundEvents;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class TrumpetItem extends Item {
    public TrumpetItem(Properties properties) {
        super(properties);
    }

    public static void scare(Level world, LivingEntity user) {
        if (!world.isClientSide) {
            List<LivingEntity> spooked = world.getEntitiesOfClass(
                    LivingEntity.class,
                    user.getBoundingBox().inflate(10.0)
            );

            for (LivingEntity entity : spooked) {
                if (entity == user) continue;

                double deltaX = entity.position().x - user.position().x + world.random.nextDouble() - world.random.nextDouble();
                double deltaZ = entity.position().z - user.position().z + world.random.nextDouble() - world.random.nextDouble();

                double distance = Math.sqrt((deltaX * deltaX) + (deltaZ * deltaZ));

                entity.hurtMarked = true;
                entity.setLastHurtByMob(user);

                entity.lerpMotion(deltaX, deltaZ, distance);
                                               
                entity.setDeltaMovement(
                        0.5 * deltaX / distance,
                        5 / (10 + distance),
                        0.5 + deltaZ / distance
                );
                
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
//FoodOnAStickItem
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
    	pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.success(pPlayer.getItemInHand(pHand));
    }
}
