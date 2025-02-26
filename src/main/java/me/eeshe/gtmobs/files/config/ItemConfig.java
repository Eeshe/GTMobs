package me.eeshe.gtmobs.files.config;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.util.ConfigUtil;
import me.eeshe.gtmobs.util.ItemUtil;
import me.eeshe.gtmobs.util.LogUtil;

public class ItemConfig extends ConfigWrapper {

  public ItemConfig(GTMobs plugin) {
    super(plugin, null, "items.yml");
  }

  @Override
  public void writeDefaults() {
    writeDefaultItems();
    writeDefaultAttributes();

    getConfig().options().copyDefaults(true);
    saveConfig();
    reloadConfig();
  }

  /**
   * Writes the default items to the items.yml
   */
  private void writeDefaultItems() {
    FileConfiguration config = getConfig();
    if (config.contains("items")) {
      return;
    }
    ConfigUtil.writeConfigItemStack(config, "items.Test_Helmet", ItemUtil.generateItemStack(
        Material.IRON_HELMET,
        (short) 0,
        (byte) 0,
        "&7Test Helmet",
        List.of("&eLore Line"),
        false,
        Map.of(Enchantment.PROTECTION_ENVIRONMENTAL, 4),
        ImmutableMultimap.of(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(
            UUID.randomUUID(),
            "armor_toughness",
            20,
            Operation.ADD_NUMBER)),
        new ItemFlag[] {
            ItemFlag.HIDE_UNBREAKABLE
        },
        null));
    ConfigUtil.writeConfigItemStack(config, "items.Test_Sword", ItemUtil.generateItemStack(
        Material.DIAMOND_SWORD,
        (short) 0,
        (byte) 0,
        "&7Test Sword",
        List.of("&eLore Line"),
        true,
        Map.of(Enchantment.DAMAGE_ALL, 5),
        ImmutableMultimap.of(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
            UUID.randomUUID(),
            "attack_damage",
            15,
            Operation.ADD_NUMBER)),
        new ItemFlag[] {
            ItemFlag.HIDE_UNBREAKABLE
        }, null));
  }

  private void writeDefaultAttributes() {
    Map<Material, Multimap<Attribute, AttributeModifier>> defaultAttributes = Map.ofEntries(
        Map.entry(Material.WOOD_SWORD, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                4,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.6,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.WOOD_PICKAXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.WOOD_AXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                7,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                0.8,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.WOOD_SPADE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.WOOD_HOE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.STONE_SWORD, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.6,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.STONE_PICKAXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                3.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.STONE_AXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                9,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                0.8,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.STONE_SPADE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                3.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.STONE_HOE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_SWORD, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                6,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.6,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_PICKAXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_AXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                9,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                0.9,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_SPADE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_HOE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_SWORD, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                6,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.6,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_PICKAXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_AXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                7,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                0.8,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_SPADE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_HOE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_SWORD, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                7,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.6,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_PICKAXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_AXE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                9,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_SPADE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5.5,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1.0,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_HOE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ATTACK_DAMAGE, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ATTACK_SPEED, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                4,
                AttributeModifier.Operation.ADD_NUMBER))))),

        // Leather Armor
        Map.entry(Material.LEATHER_HELMET, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.LEATHER_CHESTPLATE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                3,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.LEATHER_LEGGINGS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.LEATHER_BOOTS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER))))),

        // Chainmail Armor
        Map.entry(Material.CHAINMAIL_HELMET, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.CHAINMAIL_CHESTPLATE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.CHAINMAIL_LEGGINGS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                4,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.CHAINMAIL_BOOTS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER))))),

        // Iron Armor
        Map.entry(Material.IRON_HELMET, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_CHESTPLATE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                6,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_LEGGINGS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.IRON_BOOTS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),

        // Gold Armor
        Map.entry(Material.GOLD_HELMET, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_CHESTPLATE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                5,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_LEGGINGS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                3,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.GOLD_BOOTS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                1,
                AttributeModifier.Operation.ADD_NUMBER))))),

        // Diamond Armor
        Map.entry(Material.DIAMOND_HELMET, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                3,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_CHESTPLATE, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                8,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_LEGGINGS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                6,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))),
        Map.entry(Material.DIAMOND_BOOTS, Multimaps.forMap(Map.ofEntries(
            Map.entry(Attribute.GENERIC_ARMOR, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                3,
                AttributeModifier.Operation.ADD_NUMBER)),
            Map.entry(Attribute.GENERIC_ARMOR_TOUGHNESS, new AttributeModifier(
                UUID.randomUUID(),
                "1",
                2,
                AttributeModifier.Operation.ADD_NUMBER))))));
    for (Map.Entry<Material, Multimap<Attribute, AttributeModifier>> entry : defaultAttributes.entrySet()) {
      writeDefaultAttribute(entry);
    }
  }

  /**
   * Writes the passed attribute
   *
   * @param entry Attribute to write
   */
  private void writeDefaultAttribute(Map.Entry<Material, Multimap<Attribute, AttributeModifier>> entry) {
    FileConfiguration config = getConfig();
    String path = "default-attributes." + entry.getKey().name();
    if (config.contains(path))
      return;

    Multimap<Attribute, AttributeModifier> attributes = entry.getValue();
    for (Map.Entry<Attribute, AttributeModifier> attributeEntry : attributes.entries()) {
      String subPath = path + "." + attributeEntry.getKey().name();
      AttributeModifier attributeModifier = attributeEntry.getValue();
      config.addDefault(subPath + ".amount", attributeModifier.getAmount());
      config.addDefault(subPath + ".operation", attributeModifier.getOperation().name());
    }
  }

  public ItemStack fetchItem(String itemId) {
    ItemStack item = ConfigUtil.fetchConfigItemStack(getConfig(), "items." + itemId);
    if (item == null) {
      LogUtil.sendWarnLog("Item '" + itemId + "' could't be fetched.");
    }
    return item;
  }

  /**
   * Fetches the configured default attributes for the passed Material
   *
   * @param material Material to fetch the attributes of
   * @return Default attributes of the passed Material
   */
  public Multimap<Attribute, AttributeModifier> fetchDefaultAttributes(Material material) {
    Multimap<Attribute, AttributeModifier> attributes = ArrayListMultimap.create();
    ConfigurationSection attributeSection = getConfig()
        .getConfigurationSection("default-attributes." + material.name());
    if (attributeSection == null)
      return attributes;
    for (String attributeName : attributeSection.getKeys(false)) {
      Attribute attribute;
      try {
        attribute = Attribute.valueOf(attributeName);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Unknown attribute '" + attributeName + "' configured for '" + material.name() + "'.");
        continue;
      }
      String operationName = attributeSection.getString(attributeName + ".operation");
      AttributeModifier.Operation operation;
      try {
        operation = AttributeModifier.Operation.valueOf(operationName);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Unknown operation '" + operationName + "' configured for '" + material.name() + "'.");
        continue;
      }
      double amount = attributeSection.getDouble(attributeName + ".amount");

      attributes.put(attribute, new AttributeModifier(UUID.randomUUID(),
          attributeName.toLowerCase(Locale.ROOT), amount, operation));
    }
    return attributes;
  }
}
