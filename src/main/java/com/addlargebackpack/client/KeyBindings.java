package com.addlargebackpack.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBindings {
    public static final String KEY_CATEGORY = "key.categories.addlargebackpack";
    
    public static final KeyMapping OPEN_BACKPACK = new KeyMapping(
        "key.addlargebackpack.open_backpack",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_B,
        KEY_CATEGORY
    );
}
