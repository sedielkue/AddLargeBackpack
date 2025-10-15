package com.addlargebackpack.items;

import com.addlargebackpack.config.BackpackConfig;
import com.addlargebackpack.menu.BackpackContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;

public class BackpackItem extends Item {

    public BackpackItem() {
        super(new Item.Properties().stacksTo(1));
    }

    public static int getBackpackSize() {
        int rows = BackpackConfig.BACKPACK_ROWS.get();
        return rows * 9; // Each row contains 9 slots
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            openBackpack(serverPlayer, itemstack, player.getInventory().selected);
        }
        
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
    
    /**
     * Opens the backpack GUI for the specified player.
     * Can be called from right-click or keybind.
     * 
     * @param player The server player opening the backpack
     * @param backpackStack The backpack ItemStack
     * @param slotIndex The inventory slot index of the backpack
     */
    public void openBackpack(ServerPlayer player, ItemStack backpackStack, int slotIndex) {
        NetworkHooks.openScreen(player, 
            new SimpleMenuProvider(
                (id, inventory, p) -> new BackpackContainer(id, inventory, backpackStack),
                Component.translatable("container.addlargebackpack.backpack")
            ));
    }
}