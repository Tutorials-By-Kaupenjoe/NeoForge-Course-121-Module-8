package net.kaupenjoe.mccourse.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.custom.GiraffeEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class GiraffeRenderer extends MobRenderer<GiraffeEntity, GiraffeModel<GiraffeEntity>> {
    public GiraffeRenderer(EntityRendererProvider.Context context) {
        super(context, new GiraffeModel<>(context.bakeLayer(ModModelLayers.GIRAFFE)), 0.6f);
    }

    @Override
    public ResourceLocation getTextureLocation(GiraffeEntity entity) {
        if(entity.isSaddled()) {
            return ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/giraffe/giraffe_saddled.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/giraffe/giraffe.png");
        }
    }

    @Override
    public void render(GiraffeEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if(entity.isBaby()) {
            poseStack.scale(0.5f, 0.6f, 0.5f);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
