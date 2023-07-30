package com.jamieswhiteshirt.trumpetskeleton.register;

import com.jamieswhiteshirt.trumpetskeleton.TrumpetSkeleton;
import com.jamieswhiteshirt.trumpetskeleton.entities.TrumpetSkeletonMob;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Entities {
    public static final DeferredRegister<EntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TrumpetSkeleton.MOD_ID);
    
	public static final RegistryObject<EntityType<TrumpetSkeletonMob>> TRUMPET_SKELETON_ENTITY = register("trumpet_skeleton",
			EntityType.Builder.<TrumpetSkeletonMob>of(TrumpetSkeletonMob::new, MobCategory.MONSTER).sized(0.6f, 1.99F).clientTrackingRange(8)
			);
	
	private static <T extends Entity> RegistryObject<EntityType<T>> register(String registryname, EntityType.Builder<T> entityTypeBuilder) {
		return REGISTER.register(registryname, () -> (EntityType<T>) entityTypeBuilder.build(registryname));
	}
}
