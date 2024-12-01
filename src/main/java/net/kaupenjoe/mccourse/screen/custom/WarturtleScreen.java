package net.kaupenjoe.mccourse.screen.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.custom.WarturtleEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WarturtleScreen extends AbstractContainerScreen<WarturtleMenu> {
    private static final ResourceLocation GUI_TEXTURE_T0 =
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID,"textures/entity/warturtle/gui/warturtle_gui_tier0.png");
    private static final ResourceLocation GUI_TEXTURE_T1 =
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID,"textures/entity/warturtle/gui/warturtle_gui_tier1.png");
    private static final ResourceLocation GUI_TEXTURE_T2 =
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID,"textures/entity/warturtle/gui/warturtle_gui_tier2.png");
    private static final ResourceLocation GUI_TEXTURE_T3 =
            ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID,"textures/entity/warturtle/gui/warturtle_gui_tier3.png");
    private final WarturtleEntity warturtle;
    private float xMouse;
    private float yMouse;

    public WarturtleScreen(WarturtleMenu pMenu, Inventory pPlayerInventory, Component title) {
        super(pMenu, pPlayerInventory, title);
        this.warturtle = pMenu.warturtle;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = 72;
        titleLabelY = 12;

        inventoryLabelX = 1000;
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        if(!warturtle.hasTier1Chest()) {
            RenderSystem.setShaderTexture(0, GUI_TEXTURE_T0);
            pGuiGraphics.blit(GUI_TEXTURE_T0, x, y, 0, 0, imageWidth, imageHeight);
        } else if(warturtle.hasTier1Chest()) {
            RenderSystem.setShaderTexture(0, GUI_TEXTURE_T1);
            pGuiGraphics.blit(GUI_TEXTURE_T1, x, y, 0, 0, imageWidth, imageHeight);
        }

        if(warturtle.hasTier2Chest()) {
            RenderSystem.setShaderTexture(0, GUI_TEXTURE_T2);
            pGuiGraphics.blit(GUI_TEXTURE_T2, x, y, 0, 0, imageWidth, imageHeight);
        }
        if(warturtle.hasTier3Chest()) {
            RenderSystem.setShaderTexture(0, GUI_TEXTURE_T3);
            pGuiGraphics.blit(GUI_TEXTURE_T3, x, y, 0, 0, imageWidth, imageHeight);
        }

        InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, x + 8, y + 9, x + 60, y + 58, 20, 0.05F,
                this.xMouse, this.yMouse, this.warturtle);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        this.xMouse = (float)mouseX;
        this.yMouse = (float)mouseY;

        renderBackground(guiGraphics, mouseX, mouseY, delta);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
