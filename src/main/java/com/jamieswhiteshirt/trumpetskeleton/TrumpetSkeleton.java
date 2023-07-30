package com.jamieswhiteshirt.trumpetskeleton;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jamieswhiteshirt.trumpetskeleton.register.Entities;
import com.jamieswhiteshirt.trumpetskeleton.register.Items;
import com.jamieswhiteshirt.trumpetskeleton.register.SoundEvents;

import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent.RegisterRenderers;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

@Mod(TrumpetSkeleton.MOD_ID)
public class TrumpetSkeleton {
	public static final String MOD_ID = "trumpetskeleton";
	public static final Logger LOGGER = LogManager.getLogger("TrumpetSkeleton");

	public TrumpetSkeleton() {
		final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		eventBus.register(this);
		
		Entities.REGISTER.register(eventBus);
		Items.REGISTER.register(eventBus);
		SoundEvents.REGISTER.register(eventBus);
		
		TrumpetSkeletonSpawningModifier.BIOME_MODIFIER_SERIALIZERS.register(eventBus);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
	}

	@SubscribeEvent
	public void newEntityAttributes(EntityAttributeCreationEvent event) {
	    event.put(Entities.TRUMPET_SKELETON_ENTITY.get(),
	    		AbstractSkeleton.createAttributes().build()
	    );
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void setupClient(final RegisterRenderers event) {
		event.registerEntityRenderer(Entities.TRUMPET_SKELETON_ENTITY.get(), SkeletonRenderer::new);
	}

	@SubscribeEvent
	public void setupCommon(final FMLCommonSetupEvent event) throws NoSuchFieldException, IllegalAccessException {
		addSpawns(event);

		// Apparently, this is the recommended way to do this. Now using reflection correctly.
		Field f = ObfuscationReflectionHelper.findField(Parrot.class, "f_29358_");

		try {
			// Get a reference to the underlying HashMap
			@SuppressWarnings("unchecked")
			HashMap<EntityType<?>, SoundEvent> imitationSound = (HashMap<EntityType<?>, SoundEvent>) f.get(Parrot.class);

			// Add our sound event to the map
			imitationSound.put(Entities.TRUMPET_SKELETON_ENTITY.get(), SoundEvents.PARROT_DOOT.get());
		} catch (IllegalAccessException e) {
			// If it didn't work, we should log the problem and then re-throw the exception.
			LOGGER.error("Failed to set up parrot imitation sound", e);
			throw e;
		}
	}

	private void addSpawns(FMLCommonSetupEvent event) {
		event.enqueueWork(() -> { 
			double relativeWeight = Config.RELATIVE_SPAWN_WEIGHT.get();
			if (relativeWeight <= 0) {
				LOGGER.info("Trumpet skeletons have been configured not to spawn; not registering spawn entries.");
			}

			//SpawnPlacements.register(Entities.TRUMPET_SKELETON_ENTITY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, TrumpetSkeletonMob::checkMonsterSpawnRules);
		});
	}
}
