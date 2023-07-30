package com.jamieswhiteshirt.trumpetskeleton;

import com.jamieswhiteshirt.trumpetskeleton.register.Entities;
import com.mojang.serialization.Codec;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings.SpawnerData;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo.BiomeInfo.Builder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TrumpetSkeletonSpawningModifier implements BiomeModifier {

    public static final TrumpetSkeletonSpawningModifier INSTANCE = new TrumpetSkeletonSpawningModifier();
	
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, TrumpetSkeleton.MOD_ID);

    public static final RegistryObject<Codec<TrumpetSkeletonSpawningModifier>> SPAWNING_MODIFIER_TYPE = 
    		BIOME_MODIFIER_SERIALIZERS.register("trumpet_spawning", () -> Codec.unit(INSTANCE));

	@Override
	public void modify(Holder<Biome> biomeHolder, Phase phase, Builder builder) {		
		if (phase == Phase.ADD) {
			Biome biome = biomeHolder.get();

			int skeletonWeight = 0;
			double relativeWeight = Config.RELATIVE_SPAWN_WEIGHT.get();

			if (relativeWeight <= 0) 
				return;
			
			for (SpawnerData entry : biome.getMobSettings().getMobs(MobCategory.MONSTER).unwrap()) {
				if (entry.type == EntityType.SKELETON) {
					skeletonWeight += entry.getWeight().asInt();
				}
			}

			if (skeletonWeight > 0) {
				int computedWeight = (int) Math.ceil(skeletonWeight * relativeWeight);

				SpawnerData data = new SpawnerData(
						Entities.TRUMPET_SKELETON_ENTITY.get(),
						computedWeight,
						1,
						1);
				
				builder.getMobSpawnSettings().addSpawn(MobCategory.MONSTER, data);

			}
		}
	}

	@Override
	public Codec<? extends BiomeModifier> codec() {
		return SPAWNING_MODIFIER_TYPE.get();
	}

}
