package me.eeshe.gtmobs.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ItemUtil {

  /**
   * Generates the passed ItemStack with the passed information.
   *
   * @param material  Material of the ItemStack.
   * @param name      Name of the ItemStack.
   * @param loreLines Lore of the ItemStack.
   * @return ItemStack with the passed information.
   */
  public static ItemStack generateItemStack(Material material, String name, String... loreLines) {
    return generateItemStack(material, name, Arrays.asList(loreLines));
  }

  /**
   * Generates the passed ItemStack with the passed information.
   *
   * @param material  Material of the ItemStack.
   * @param name      Name of the ItemStack.
   * @param loreLines Lore of the ItemStack.
   * @return ItemStack with the passed information.
   */
  public static ItemStack generateItemStack(Material material, String name, List<String> loreLines) {
    ItemStack item = new ItemStack(material);
    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return item;
    if (name != null) {
      meta.setDisplayName(StringUtil.formatColor(name));
    }
    if (loreLines != null) {
      List<String> lore = new ArrayList<>();
      for (String loreLine : loreLines) {
        lore.add(StringUtil.formatColor(loreLine));
      }
      meta.setLore(lore);
    }
    item.setItemMeta(meta);

    return item;
  }

  /**
   * Edits the passed ItemStack with the passed information.
   *
   * @param item    ItemStack that will be edited.
   * @param newName New name of the item.
   * @param newLore New lore of the item.
   */
  public static void editItemStack(ItemStack item, String newName, List<String> newLore) {
    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return;
    if (newName != null) {
      meta.setDisplayName(StringUtil.formatColor(newName));
    }
    if (newLore != null) {
      newLore.replaceAll(StringUtil::formatColor);
      meta.setLore(newLore);
    }
    item.setItemMeta(meta);
  }

  /**
   * Formats the passed placeholders on the passed item's name and lore.
   *
   * @param item         Item to format.
   * @param placeholders Placeholders to format.
   * @return Formatted ItemStack.
   */
  public static ItemStack formatPlaceholders(ItemStack item, Map<String, String> placeholders) {
    if (placeholders == null || placeholders.isEmpty())
      return item;
    if (item == null || item.getItemMeta() == null)
      return item;

    ItemMeta meta = item.getItemMeta();
    String displayName = meta.getDisplayName();
    List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      String placeholder = entry.getKey();
      displayName = displayName.replace(placeholder, entry.getValue());
      lore.replaceAll(loreLine -> loreLine.replace(placeholder, StringUtil.formatColor(entry.getValue())));
    }
    meta.setDisplayName(displayName);
    meta.setLore(lore);
    item.setItemMeta(meta);

    return item;
  }

  /**
   * Formats the passed list placeholders on the passed item's name and lore.
   *
   * @param item         Item to format.
   * @param placeholders List placeholders to format.
   * @return Formatted ItemStack.
   */
  public static ItemStack formatListPlaceholders(ItemStack item, Map<String, List<String>> placeholders) {
    if (placeholders == null || placeholders.isEmpty())
      return item;
    if (item == null || item.getItemMeta() == null)
      return item;

    ItemMeta meta = item.getItemMeta();
    String displayName = meta.getDisplayName();
    List<String> lore = meta.getLore() != null ? meta.getLore() : new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : placeholders.entrySet()) {
      String placeholder = entry.getKey();
      List<String> values = new ArrayList<>(entry.getValue());
      if (values.isEmpty())
        continue;

      displayName = displayName.replace(placeholder, values.get(0));
      for (String loreLine : new ArrayList<>(lore)) {
        if (!loreLine.contains(placeholder))
          continue;

        int index = lore.indexOf(loreLine);
        lore.remove(loreLine);
        values.replaceAll(StringUtil::formatColor);
        if (values.size() == 1) {
          lore.add(index, loreLine.replace(placeholder, values.get(0)));
        } else {
          lore.addAll(index, values);
        }
      }
    }
    meta.setDisplayName(displayName);
    meta.setLore(lore);
    item.setItemMeta(meta);

    return item;
  }
}
