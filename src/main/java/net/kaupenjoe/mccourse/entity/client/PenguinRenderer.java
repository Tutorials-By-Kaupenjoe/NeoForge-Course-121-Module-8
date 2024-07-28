package net.kaupenjoe.mccourse.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.custom.PenguinEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PenguinRenderer extends MobRenderer<PenguinEntity, PenguinModel> {
    public PenguinRenderer(EntityRendererProvider.Context pContext) {
        super(pContext, new PenguinModel(pContext.bakeLayer(ModModelLayers.PENGUIN)), 0.75f);
    }

    @Override
    public ResourceLocation getTextureLocation(PenguinEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/penguin/penguin.png");
    }

    @Override
    public void render(PenguinEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack,
                       MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pPoseStack.scale(0.45f, 0.45f, 0.45f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }
}
