package me.eeshe.gtmobs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.eeshe.gtmobs.models.config.ConfigExplosion;
import me.eeshe.gtmobs.models.config.ConfigParticle;
import me.eeshe.gtmobs.models.config.ConfigSound;
import me.eeshe.gtmobs.models.config.IntRange;

public class ConfigUtil {

  public static void writeConfigItemStack(FileConfiguration config, String path, ItemStack item) {
    if (item == null)
      return;
    if (config.contains(path))
      return;

    config.set(path + ".material", item.getType().name());
    config.set(path + ".amount", item.getAmount());

    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return;
    if (meta.hasDisplayName()) {
      config.set(path + ".name", meta.getDisplayName());
    }
    List<String> lore = meta.getLore();
    if (meta.hasEnchants()) {
      String enchantmentsPath = path + ".enchantments";
      for (Map.Entry<Enchantment, Integer> entry : meta.getEnchants().entrySet()) {
        config.set(enchantmentsPath + "." + entry.getKey().getName(), entry.getValue());
      }
    }
    if (lore != null) {
      config.set(path + ".lore", lore);
    }
    if (!meta.getItemFlags().isEmpty()) {
      config.set(path + ".item-flags", meta.getItemFlags().stream().map(ItemFlag::name)
          .collect(Collectors.toList()));
    }
  }

  public static ItemStack fetchConfigItemStack(FileConfiguration config, String path) {
    ConfigurationSection itemSection = config.getConfigurationSection(path);
    if (itemSection == null)
      return null;

    String materialName = itemSection.getString("material", "");
    Material material = Material.matchMaterial(materialName);
    if (material == null) {
      LogUtil.sendWarnLog("Unknown material '" + materialName + "' for item in '" + path + "'.");
      return null;
    }
    int amount = itemSection.getInt("amount", 1);
    String displayName = itemSection.getString("name");
    List<String> lore = itemSection.getStringList("lore");
    Map<Enchantment, Integer> enchantments = fetchEnchantments(config, path + ".enchantments");
    ItemFlag[] itemFlags = fetchItemFlags(config, path + ".item-flags");

    ItemStack item = new ItemStack(material, amount);
    ItemMeta meta = item.getItemMeta();
    if (meta == null)
      return item;
    if (displayName != null) {
      meta.setDisplayName(StringUtil.formatColor(displayName));
    }
    if (!enchantments.isEmpty()) {
      if (!Arrays.asList(itemFlags).contains(ItemFlag.HIDE_ENCHANTS)) {
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
          lore.add(0, StringUtil.formatColor("&7" + StringUtil
              .formatEnum(entry.getKey().getName() + " " + StringUtil.parseToRoman(entry.getValue()))));
        }
      }
    }
    meta.setLore(lore);
    meta.addItemFlags(itemFlags);
    item.setItemMeta(meta);

    item.addUnsafeEnchantments(enchantments);

    return item;
  }

  private static Map<Enchantment, Integer> fetchEnchantments(FileConfiguration config, String path) {
    Map<Enchantment, Integer> enchantments = new HashMap<>();
    ConfigurationSection enchantmentSection = config.getConfigurationSection(path);
    if (enchantmentSection == null)
      return enchantments;
    for (String enchantmentName : enchantmentSection.getKeys(false)) {
      Enchantment enchantment = Enchantment.getByName(enchantmentName);
      if (enchantment == null) {
        LogUtil.sendWarnLog("Unknown enchantment '" + enchantmentName + "' configured for '" + path + "'.");
        continue;
      }
      enchantments.put(enchantment, enchantmentSection.getInt(enchantmentName));
    }
    return enchantments;
  }

  private static ItemFlag[] fetchItemFlags(FileConfiguration config, String path) {
    List<ItemFlag> itemFlags = new ArrayList<>();
    for (String flagName : config.getStringList(path)) {
      ItemFlag itemFlag;
      try {
        itemFlag = ItemFlag.valueOf(flagName);
      } catch (Exception e) {
        LogUtil.sendWarnLog("Unknown item flag '" + flagName + "' configured in '" + path + "'.");
        continue;
      }
      itemFlags.add(itemFlag);
    }
    return itemFlags.toArray(new ItemFlag[0]);
  }

  /**
   * Writes the passed ConfigSound to the passed configuration file.
   *
   * @param config      Config file to write.
   * @param path        Path to write in.
   * @param configSound ConfigSound to write.
   */
  public static void writeConfigSound(FileConfiguration config, String path, ConfigSound configSound) {
    if (configSound == null) {
      return;
    }
    config.set(path + ".sound", configSound.getSound().name());
    config.set(path + ".enable", configSound.isEnabled());
    config.set(path + ".volume", configSound.getVolume());
    config.set(path + ".pitch", configSound.getPitch());
  }

  /**
   * Fetches the configured ConfigSound.
   *
   * @return Configured ConfigSound.
   */
  public static ConfigSound fetchConfigSound(FileConfiguration config, String path) {
    ConfigurationSection soundSection = config.getConfigurationSection(path);
    if (soundSection == null)
      return null;

    String soundName = soundSection.getString("sound");
    org.bukkit.Sound sound;
    try {
      sound = org.bukkit.Sound.valueOf(soundName);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Unknown sound '" + soundName + "' configured for '" + path + "'.");
      return null;
    }
    boolean enabled = config.getBoolean(path + ".enable");
    float volume = (float) config.getDouble(path + ".volume");
    float pitch = (float) config.getDouble(path + ".pitch");

    return new ConfigSound(sound, enabled, volume, pitch);
  }

  /**
   * Writes the passed list of ConfigParticles to the passed config file and path.
   *
   * @param config          Config file to write.
   * @param path            Path to write in.
   * @param configParticles ConfigParticle to write.
   */
  public static void writeConfigParticles(FileConfiguration config, String path, List<ConfigParticle> configParticles) {
    if (configParticles == null) {
      return;
    }
    config.set(path, String.join(",", configParticles.stream()
        .map(ConfigParticle::toString).collect(Collectors.toList())));
  }

  /**
   * Writes the passed ConfigParticle to the passed config file and path.
   *
   * @param config         Config file to write.
   * @param path           Path to write in.
   * @param configParticle ConfigParticle to write.
   */
  public static void writeConfigParticle(FileConfiguration config, String path, ConfigParticle configParticle) {
    if (configParticle == null) {
      return;
    }
    config.set(path, configParticle.toString());
  }

  /**
   * Fetches the list of ConfigParticles configured in the passed config file and
   * path.
   *
   * @param config Configuration file to search in.
   * @param path   Path to search in.
   * @return List of the found ConfigParticles.
   */
  public static List<ConfigParticle> fetchConfigParticles(FileConfiguration config, String path) {
    List<ConfigParticle> configParticles = new ArrayList<>();
    ConfigurationSection particleSection = config.getConfigurationSection(path);
    if (particleSection == null) {
      return configParticles;
    }
    for (String key : particleSection.getKeys(false)) {
      ConfigParticle configParticle = fetchConfigParticle(config, path + "." + key);
      if (configParticle == null) {
        continue;
      }
      configParticles.add(configParticle);
    }
    return configParticles;
  }

  /**
   * Fetches the configured ConfigParticle.
   *
   * @param config Configuration file.
   * @param path   Path of the particle.
   * @return Configured ConfigParticle.
   */
  public static ConfigParticle fetchConfigParticle(FileConfiguration config, String path) {
    ConfigurationSection particleSection = config.getConfigurationSection(path);
    if (particleSection == null)
      return null;

    String particleName = particleSection.getString("particle");
    Particle particle;
    try {
      particle = Particle.valueOf(particleName);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Unknown particle '" + particleName + "' configured for '" + path + "'.");
      return null;
    }
    int amount = particleSection.getInt("amount");
    double xOffSet = particleSection.getDouble("x-off-set");
    double yOffSet = particleSection.getDouble("y-off-set");
    double zOffSet = particleSection.getDouble("z-off-set");
    double speed = particleSection.getDouble("speed");
    return new ConfigParticle(particle, amount, xOffSet, yOffSet, zOffSet,
        speed);
  }

  /**
   * Computes a List of ConfigParticles from the passed string
   *
   * @param configParticleChainString String to compute
   * @return Computed ConfigParticles
   */
  public static List<ConfigParticle> computeConfigParticles(String configParticleChainString) {
    List<ConfigParticle> configParticles = new ArrayList<>();
    String[] configParticleStrings = configParticleChainString.split(",");
    if (configParticleStrings.length == 0) {
      ConfigParticle configParticle = computeConfigParticle(configParticleChainString);
      if (configParticle != null) {
        configParticles.add(configParticle);
      }
    }
    for (String configParticleString : configParticleStrings) {
      ConfigParticle configParticle = computeConfigParticle(configParticleString);
      if (configParticle == null) {
        continue;
      }
      configParticles.add(configParticle);
    }
    return configParticles;
  }

  /**
   * Computes a ConfigParticle from the passed string
   *
   * @param configParticleString String to compute
   * @return Computed ConfigParticle
   */
  public static ConfigParticle computeConfigParticle(String configParticleString) {
    String[] params = configParticleString.split("-");
    if (params.length != 2 && params.length != 6) {
      LogUtil.sendWarnLog("Invalid ConfigParticle string '" + configParticleString + "'.");
      return null;
    }
    Particle particle;
    try {
      particle = Particle.valueOf(params[0]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Unknown particle '" + params[0] + "' configured in '" + configParticleString + "'.");
      return null;
    }
    int amount;
    try {
      amount = Integer.parseInt(params[1]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid amount '" + params[1] + "' configured in '" + configParticleString + "'.");
      return null;
    }
    if (params.length == 2) {
      return new ConfigParticle(particle, amount);
    }
    double xOffSet;
    try {
      xOffSet = Double.parseDouble(params[2]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid X Off Set '" + params[2] + "' configured in '" + configParticleString + "'.");
      return null;
    }
    double yOffSet;
    try {
      yOffSet = Double.parseDouble(params[3]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid Y Off Set '" + params[3] + "' configured in '" + configParticleString + "'.");
      return null;
    }
    double zOffSet;
    try {
      zOffSet = Double.parseDouble(params[4]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid X Off Set '" + params[4] + "' configured in '" + configParticleString + "'.");
      return null;
    }
    double speed;
    try {
      speed = Double.parseDouble(params[5]);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Invalid X Off Set '" + params[5] + "' configured in '" + configParticleString + "'.");
      return null;
    }
    return new ConfigParticle(particle, amount, xOffSet, yOffSet, zOffSet, speed);
  }

  /**
   * Fetches the configured material in the passed path within the passed config
   * file.
   *
   * @param config Configuration file the material will be fetched from.
   * @param path   Configuration path the material will be fetched from.
   * @return Configured material in the passed path within the passed config file.
   */
  public static Material fetchMaterial(FileConfiguration config, String path) {
    String generatedItemName = config.getString(path, "");
    Material generatedItem = Material.matchMaterial(generatedItemName);
    if (generatedItem == null) {
      LogUtil.sendWarnLog("Unknown item '" + generatedItemName + "' configured in '" + path + "'.");
      return null;
    }
    return generatedItem;
  }

  public static void writeIntRange(FileConfiguration config, String path, IntRange intRange) {
    config.set(path + ".min", intRange.getMin());
    config.set(path + ".max", intRange.getMax());
  }

  public static IntRange fetchIntRange(FileConfiguration config, String path) {
    ConfigurationSection rangeSection = config.getConfigurationSection(path);
    if (rangeSection == null)
      return new IntRange(0, 0);

    int max = rangeSection.getInt("max");
    int min = rangeSection.getInt("min");

    return new IntRange(max, min);
  }

  public static void writeConfigExplosion(ConfigExplosion configExplosion, FileConfiguration config, String path) {
    config.set(path + ".power", configExplosion.getPower());
    config.set(path + ".set-fire", configExplosion.shouldSetFire());
    config.set(path + ".break-blocks", configExplosion.shouldBreakBlocks());
  }

  public static ConfigExplosion fetchConfigExplosion(FileConfiguration config, String path) {
    ConfigurationSection explosionSection = config.getConfigurationSection(path);
    if (explosionSection == null)
      return null;

    float power = (float) explosionSection.getDouble("power");
    boolean shouldSetFire = explosionSection.getBoolean("set-fire");
    boolean shouldBreakBlocks = explosionSection.getBoolean("break-blocks");

    return new ConfigExplosion(power, shouldSetFire, shouldBreakBlocks);
  }

  /**
   * Writes the passed additional configs to the passed path.
   *
   * @param path              Path the additional configs will be written to.
   * @param additionalConfigs Additional configs that will be written.
   */
  public static void writeAdditionalConfigs(FileConfiguration config, String path, Map<?, Object> additionalConfigs) {
    for (Map.Entry<?, Object> entry : additionalConfigs.entrySet()) {
      String newPath = path + "." + entry.getKey();
      Object value = entry.getValue();
      if (value instanceof Map<?, ?>) {
        Map<?, ?> map = (Map<?, ?>) value;
        writeAdditionalConfigs(config, newPath, (Map<?, Object>) map);
      } else if (value instanceof IntRange) {
        IntRange intRange = (IntRange) value;
        config.addDefault(newPath + ".min", intRange.getMin());
        config.addDefault(newPath + ".max", intRange.getMax());
      } else {
        config.addDefault(newPath, entry.getValue());
      }
    }
  }

  /**
   * Writes the passed Map of attributes to the passed config file.
   *
   * @param config       Config file to write.
   * @param path         Path to write the attributes in.
   * @param attributeMap Map of attributes to write.
   */
  public static void writeAttributeMap(FileConfiguration config, String path,
      Map<Attribute, Double> attributeMap) {
    if (attributeMap == null) {
      return;
    }
    for (Entry<Attribute, Double> entry : attributeMap.entrySet()) {
      config.set(path + "." + entry.getKey(), entry.getValue());
    }
  }

  /**
   * Fetches the attributes Map stored in the passed config file and path.
   *
   * @param config Configuration file to search.
   * @param path   Path to search in.
   * @return The fetched Attribute Map.
   */
  public static Map<Attribute, Double> fetchAttributeMap(FileConfiguration config, String path) {
    Map<Attribute, Double> attributeMap = new HashMap<>();
    ConfigurationSection attributeSection = config.getConfigurationSection(path);
    if (attributeSection == null) {
      return attributeMap;
    }
    for (String attributeName : attributeSection.getKeys(false)) {
      try {
        Attribute attribute = Attribute.valueOf(attributeName);
        attributeMap.put(attribute, attributeSection.getDouble(attributeName));
      } catch (Exception e) {
        LogUtil.sendWarnLog("Unknown attribute '" + attributeName + "' found in path '" + path + "'.");
        continue;
      }
    }
    return attributeMap;
  }
}
