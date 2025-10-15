package com.addlargebackpack.client;

import com.addlargebackpack.AddLargeBackpackMod;
import com.addlargebackpack.client.gui.BackpackScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = AddLargeBackpackMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Register the screen for the backpack menu
            MenuScreens.register(AddLargeBackpackMod.BACKPACK_MENU_TYPE.get(), BackpackScreen::new);
        });
    }
}