package me.eeshe.gtmobs.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collection;

public class InventoryUtil {

  /**
   * Gives the passed player the passed ItemStack. If the player's inventory is
   * full, drop it next to them.
   *
   * @param player Player that will receive the item.
   * @param items  ItemStacks that will be given.
   */
  public static void giveItems(Player player, Collection<ItemStack> items) {
    for (ItemStack item : items) {
      giveItem(player, item);
    }
  }

  /**
   * Gives the passed player the passed ItemStack. If the player's inventory is
   * full, drop it next to them.
   *
   * @param player Player that will receive the item.
   * @param item   ItemStack that will be given.
   */
  public static void giveItem(Player player, ItemStack item) {
    if (item == null || item.getType() == Material.AIR)
      return;

    PlayerInventory playerInventory = player.getInventory();
    if (playerInventory.firstEmpty() != -1) {
      playerInventory.addItem(item);
    } else {
      player.getWorld().dropItemNaturally(player.getLocation(), item).setPickupDelay(0);
    }
  }
}
