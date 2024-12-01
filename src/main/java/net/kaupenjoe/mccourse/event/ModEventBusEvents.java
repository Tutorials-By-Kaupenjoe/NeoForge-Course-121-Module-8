package net.kaupenjoe.mccourse.event;

import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.ModEntities;
import net.kaupenjoe.mccourse.entity.client.*;
import net.kaupenjoe.mccourse.entity.custom.GiraffeEntity;
import net.kaupenjoe.mccourse.entity.custom.PenguinEntity;
import net.kaupenjoe.mccourse.entity.custom.WarturtleEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.PENGUIN, PenguinModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.GIRAFFE, GiraffeModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WARTURTLE, WarturtleModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WARTURTLE_ARMOR, WarturtleModel::createBodyLayer);

        event.registerLayerDefinition(ModModelLayers.TOMAHAWK, TomahawkProjectileModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.PENGUIN.get(), PenguinEntity.createAttributes().build());
        event.put(ModEntities.GIRAFFE.get(), GiraffeEntity.createAttributes().build());
        event.put(ModEntities.WARTURTLE.get(), WarturtleEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.PENGUIN.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(ModEntities.GIRAFFE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);

        event.register(ModEntities.WARTURTLE.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }
}
