package net.kaupenjoe.mccourse.entity.client;

import net.kaupenjoe.mccourse.MCCourseMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation PENGUIN = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "penguin"), "main");

    public static final ModelLayerLocation GIRAFFE = new ModelLayerLocation(
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "giraffe"), "main");

}
