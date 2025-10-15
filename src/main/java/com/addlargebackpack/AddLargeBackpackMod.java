package com.addlargebackpack;

import com.addlargebackpack.config.BackpackConfig;
import com.addlargebackpack.items.BackpackItem;
import com.addlargebackpack.menu.BackpackMenuType;
import com.addlargebackpack.menu.BackpackContainer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(AddLargeBackpackMod.MODID)
public class AddLargeBackpackMod {
    public static final String MODID = "addlargebackpack";

    // DeferredRegister for items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    
    // DeferredRegister for menu types
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, MODID);

    // Register the backpack item
    public static final RegistryObject<Item> BACKPACK_ITEM = ITEMS.register("backpack", BackpackItem::new);
    
    // Register the backpack menu type
    public static final RegistryObject<MenuType<BackpackContainer>> BACKPACK_MENU_TYPE = 
        MENU_TYPES.register("backpack", () -> BackpackMenuType.create());

    public AddLargeBackpackMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the deferred registers to the mod event bus
        ITEMS.register(modEventBus);
        MENU_TYPES.register(modEventBus);

        // Register config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BackpackConfig.SPEC);

        // Register network packets
        com.addlargebackpack.network.PacketHandler.register();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(BACKPACK_ITEM);
        }
    }
}