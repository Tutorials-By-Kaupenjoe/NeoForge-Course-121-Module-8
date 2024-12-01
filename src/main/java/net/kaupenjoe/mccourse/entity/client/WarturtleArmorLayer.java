package net.kaupenjoe.mccourse.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.custom.WarturtleEntity;
import net.kaupenjoe.mccourse.item.ModItems;
import net.kaupenjoe.mccourse.item.custom.WarturtleArmorItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class WarturtleArmorLayer extends RenderLayer<WarturtleEntity, WarturtleModel<WarturtleEntity>> {
    private final WarturtleModel<WarturtleEntity> model;
    private Map<Item, ResourceLocation> ARMOR_MAP = Map.of(
            ModItems.IRON_WARTURTLE_ARMOR.get(), ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/iron_warturtle.png"),
            ModItems.GOLD_WARTURTLE_ARMOR.get(), ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/gold_warturtle.png"),
            ModItems.DIAMOND_WARTURTLE_ARMOR.get(), ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/diamond_warturtle.png"),
            ModItems.NETHERITE_WARTURTLE_ARMOR.get(), ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/netherite_warturtle.png"),
            ModItems.BLACK_OPAL_WARTURTLE_ARMOR.get(), ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/black_opal_warturtle.png")
    );

    public WarturtleArmorLayer(RenderLayerParent<WarturtleEntity, WarturtleModel<WarturtleEntity>> renderer, EntityModelSet models) {
        super(renderer);
        model = new WarturtleModel<>(models.bakeLayer(ModModelLayers.WARTURTLE_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, WarturtleEntity livingEntity,
                       float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (livingEntity.isWearingBodyArmor()) {
            ItemStack itemstack = livingEntity.getBodyArmorItem();
            if (itemstack.getItem() instanceof WarturtleArmorItem armorItem) {
                this.getParentModel().copyPropertiesTo(this.model);
                this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
                this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
                VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(ARMOR_MAP.get(armorItem)));
                this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY);
                this.maybeRenderColoredLayer(poseStack, bufferSource, packedLight, livingEntity, armorItem);
            }
        }
    }

    private static final ResourceLocation[] DYE_LOCATION = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/white.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/orange.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/magenta.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/light_blue.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/yellow.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/lime.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/pink.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/gray.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/light_gray.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/cyan.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/purple.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/blue.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/brown.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/green.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/red.png"),
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID, "textures/entity/warturtle/armor/blankies/black.png")
    };

    private void maybeRenderColoredLayer(PoseStack poseStack, MultiBufferSource buffer, int packedLight, WarturtleEntity livingEntity, WarturtleArmorItem armorItem) {
        DyeColor dyecolor = livingEntity.getSwag();
        ResourceLocation resourcelocation;
        if (dyecolor != null) {
            resourcelocation = DYE_LOCATION[dyecolor.getId()];
        } else {
            resourcelocation = ARMOR_MAP.get(armorItem);
        }
        this.model.renderToBuffer(poseStack,buffer.getBuffer(RenderType.entityCutoutNoCull(resourcelocation)), packedLight,OverlayTexture.NO_OVERLAY);
    }
}
