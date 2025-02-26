package me.eeshe.gtmobs.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.google.common.collect.Multimap;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagDouble;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

public class ItemUtil {

  public static ItemStack generateItemStack(Material material, String name, List<String> lore,
      Map<Enchantment, Integer> enchantments,
      Multimap<Attribute, AttributeModifier> attributes) {
    return generateItemStack(material, (short) -1, (byte) -1, name, lore, false,
        enchantments, attributes, null, null);
  }

  public static ItemStack generateItemStack(Material material, short damage,
      byte data, String name, List<String> lore, boolean unbreakable,
      Map<Enchantment, Integer> enchantments,
      Multimap<Attribute, AttributeModifier> attributes, ItemFlag[] itemFlags, Color color) {
    ItemStack item = new ItemStack(material, 1, data);
    item.setDurability(damage);
    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return null;
    if (name != null) {
      meta.setDisplayName(StringUtil.formatColor(name));
    }
    if (lore != null) {
      lore = new ArrayList<>(lore);
      lore.replaceAll(StringUtil::formatColor);
      meta.setLore(lore);
    }
    if (itemFlags != null) {
      meta.addItemFlags(itemFlags);
    }
    if (color != null && meta instanceof LeatherArmorMeta) {
      ((LeatherArmorMeta) meta).setColor(color);
    }
    item.setItemMeta(meta);
    if (enchantments != null) {
      item.addUnsafeEnchantments(enchantments);
    }
    return item;
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
   * Applies the passed attributes to the passed item
   *
   * @param item       Item to apply the attributes to
   * @param attributes Attribute to apply
   * @return Item with the applied attributes
   */
  public static ItemStack applyAttributes(ItemStack item, Multimap<Attribute, AttributeModifier> attributes) {
    net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
    NBTTagCompound compound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
    NBTTagList modifiers = new NBTTagList();
    ThreadLocalRandom random = ThreadLocalRandom.current();

    for (Entry<Attribute, AttributeModifier> entry : attributes.entries()) {
      NBTTagCompound attributeNbt = new NBTTagCompound();
      String attributeName = computeAttributeName(entry.getKey());
      AttributeModifier modifier = entry.getValue();

      attributeNbt.set("AttributeName", new NBTTagString(attributeName));
      attributeNbt.set("Name", new NBTTagString(attributeName));
      attributeNbt.set("Amount", new NBTTagDouble(modifier.getAmount()));
      attributeNbt.set("Operation", new NBTTagInt(0));
      attributeNbt.set("UUIDLeast", new NBTTagInt(random.nextInt()));
      attributeNbt.set("UUIDMost", new NBTTagInt(random.nextInt()));
      attributeNbt.set("Slot", new NBTTagString(computeEquipmentSlot(item)));

      modifiers.add(attributeNbt);
    }
    compound.set("AttributeModifiers", modifiers);
    nmsItem.setTag(compound);

    return CraftItemStack.asBukkitCopy(nmsItem);
  }

  private static String computeAttributeName(Attribute attribute) {
    String attributeString = attribute.name().toLowerCase(Locale.ROOT);
    if (!attributeString.startsWith("generic")) {
      return null;
    }
    attributeString = attributeString.replace("generic_", "");

    String attributeName = "";
    for (String word : attributeString.split("_")) {
      if (attributeName.isEmpty()) {
        attributeName += word;
      } else {
        attributeName += StringUtil.capitalize(word);
      }
    }
    return "generic." + attributeName;
  }

  private static String computeEquipmentSlot(ItemStack item) {
    if (item == null) {
      return null;
    }
    String materialName = item.getType().name();
    if (materialName.endsWith("HELMET")) {
      return "head";
    } else if (materialName.endsWith("CHESTPLATE")) {
      return "chest";
    } else if (materialName.endsWith("LEGGINGS")) {
      return "legs";
    } else if (materialName.endsWith("BOOTS")) {
      return "feet";
    } else {
      return "mainhand";
    }
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
