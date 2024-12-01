package net.kaupenjoe.mccourse.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.custom.WarturtleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class WarturtleRenderer extends MobRenderer<WarturtleEntity, WarturtleModel<WarturtleEntity>> {
    public WarturtleRenderer(EntityRendererProvider.Context context) {
        super(context, new WarturtleModel<>(context.bakeLayer(ModModelLayers.WARTURTLE)), 1.25f);
        this.addLayer(new WarturtleArmorLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(WarturtleEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/warturtle.png");
    }

    @Override
    public void render(WarturtleEntity pEntity, float pEntityYaw, float pPartialTicks,
                       PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        if(pEntity.isBaby()) {
            pMatrixStack.scale(0.45f, 0.45f, 0.45f);
        }

        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }
}
