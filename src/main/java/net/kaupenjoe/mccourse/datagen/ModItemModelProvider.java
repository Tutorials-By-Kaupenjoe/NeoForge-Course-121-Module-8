package net.kaupenjoe.mccourse.datagen;

import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.block.ModBlocks;
import net.kaupenjoe.mccourse.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MCCourseMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.BLACK_OPAL.get());
        basicItem(ModItems.RAW_BLACK_OPAL.get());
        basicItem(ModItems.CHAINSAW.get());
        basicItem(ModItems.TOMATO.get());
        basicItem(ModItems.FROSTFIRE_ICE.get());

        basicItem(ModItems.IRON_WARTURTLE_ARMOR.get());
        basicItem(ModItems.GOLD_WARTURTLE_ARMOR.get());
        basicItem(ModItems.DIAMOND_WARTURTLE_ARMOR.get());
        basicItem(ModItems.NETHERITE_WARTURTLE_ARMOR.get());
        basicItem(ModItems.BLACK_OPAL_WARTURTLE_ARMOR.get());

        buttonItem(ModBlocks.BLACK_OPAL_BUTTON, ModBlocks.BLACK_OPAL_BLOCK);
        fenceItem(ModBlocks.BLACK_OPAL_FENCE, ModBlocks.BLACK_OPAL_BLOCK);
        wallItem(ModBlocks.BLACK_OPAL_WALL, ModBlocks.BLACK_OPAL_BLOCK);

        basicItem(ModBlocks.BLACK_OPAL_DOOR.asItem());

        withExistingParent(ModItems.PENGUIN_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.GIRAFFE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
        withExistingParent(ModItems.WARTURTLE_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));
    }

    public void buttonItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void fenceItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

    public void wallItem(DeferredBlock<Block> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  ResourceLocation.fromNamespaceAndPath(MCCourseMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));
    }

}
