package net.kaupenjoe.mccourse.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.custom.TomahawkProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class TomahawkProjectileRenderer extends EntityRenderer<TomahawkProjectileEntity> {
    private TomahawkProjectileModel model;

    public TomahawkProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TomahawkProjectileModel(context.bakeLayer(ModModelLayers.TOMAHAWK));
    }

    @Override
    public void render(TomahawkProjectileEntity pEntity, float entityYaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        if(!pEntity.isGrounded()) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, pEntity.yRotO, pEntity.getYRot())));
            poseStack.mulPose(Axis.XP.rotationDegrees(pEntity.getRenderingRotation() * 5f));
            poseStack.translate(0, -1.0f, 0);
        } else {
            poseStack.mulPose(Axis.YP.rotationDegrees(pEntity.groundedOffset.y));
            poseStack.mulPose(Axis.XP.rotationDegrees(pEntity.groundedOffset.x));
            poseStack.translate(0, -1.0f, 0);
        }

        VertexConsumer vertexconsumer = ItemRenderer.getFoilBufferDirect(
                bufferSource, this.model.renderType(this.getTextureLocation(pEntity)),false, false);
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
        super.render(pEntity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TomahawkProjectileEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/tomahawk/tomahawk.png");
    }
}
