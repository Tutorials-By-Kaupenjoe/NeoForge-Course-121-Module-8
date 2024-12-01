package net.kaupenjoe.mccourse.item;

import net.kaupenjoe.mccourse.MCCourseMod;
import net.kaupenjoe.mccourse.entity.ModEntities;
import net.kaupenjoe.mccourse.item.custom.ChainsawItem;
import net.kaupenjoe.mccourse.item.custom.FuelItem;
import net.kaupenjoe.mccourse.item.custom.TomahawkItem;
import net.kaupenjoe.mccourse.item.custom.WarturtleArmorItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MCCourseMod.MOD_ID);

    public static final DeferredItem<Item> BLACK_OPAL = ITEMS.registerSimpleItem("black_opal");
    public static final DeferredItem<Item> RAW_BLACK_OPAL =
            ITEMS.registerItem("raw_black_opal", Item::new, new Item.Properties());

    public static final DeferredItem<Item> CHAINSAW =
            ITEMS.registerItem("chainsaw", ChainsawItem::new, new Item.Properties().durability(32));

    public static final DeferredItem<Item> TOMATO =
            ITEMS.registerItem("tomato", properties -> new Item(properties) {
                @Override
                public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
                    pTooltipComponents.add(Component.translatable("tooltip.mccourse.tomato.1"));
                    super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
                }
            }, new Item.Properties().food(ModFoodProperties.TOMATO));
    public static final DeferredItem<Item> FROSTFIRE_ICE =
            ITEMS.registerItem("frostfire_ice", properties -> new FuelItem(properties, 800), new Item.Properties());

    public static final DeferredItem<Item> PENGUIN_SPAWN_EGG = ITEMS.register("penguin_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PENGUIN, 0xdebd47, 0xccbfbe,
                    new Item.Properties()));
    public static final DeferredItem<Item> GIRAFFE_SPAWN_EGG = ITEMS.register("giraffe_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GIRAFFE, 0xe7d7a5, 0x7e5b41,
                    new Item.Properties()));
    public static final DeferredItem<Item> WARTURTLE_SPAWN_EGG = ITEMS.register("warturtle_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.WARTURTLE, 0x7e9680, 0xc5d1c5,
                    new Item.Properties()));

    public static final DeferredItem<Item> TOMAHAWK = ITEMS.register("tomahawk",
            () -> new TomahawkItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<Item> IRON_WARTURTLE_ARMOR = ITEMS.registerItem("iron_warturtle_armor",
            properties -> new WarturtleArmorItem(ArmorMaterials.IRON, properties.durability(200)));
    public static final DeferredItem<Item> GOLD_WARTURTLE_ARMOR = ITEMS.registerItem("gold_warturtle_armor",
            properties -> new WarturtleArmorItem(ArmorMaterials.GOLD, properties.durability(400)));
    public static final DeferredItem<Item> DIAMOND_WARTURTLE_ARMOR = ITEMS.registerItem("diamond_warturtle_armor",
            properties -> new WarturtleArmorItem(ArmorMaterials.DIAMOND, properties.durability(600)));
    public static final DeferredItem<Item> NETHERITE_WARTURTLE_ARMOR = ITEMS.registerItem("netherite_warturtle_armor",
            properties -> new WarturtleArmorItem(ArmorMaterials.NETHERITE, properties.durability(800)));
    public static final DeferredItem<Item> BLACK_OPAL_WARTURTLE_ARMOR = ITEMS.registerItem("black_opal_warturtle_armor",
            properties -> new WarturtleArmorItem(ArmorMaterials.IRON, properties.durability(1000)));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
