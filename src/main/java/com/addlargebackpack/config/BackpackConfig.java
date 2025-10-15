package com.addlargebackpack.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class BackpackConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.IntValue BACKPACK_ROWS;

    static {
        BUILDER.push("Backpack Settings");
        
        BACKPACK_ROWS = BUILDER
            .comment("Number of rows in the backpack (each row contains 9 slots)")
            .defineInRange("backpackRows", 16, 6, 1111);
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}