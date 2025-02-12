package me.eeshe.gtmobs.models.config;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.files.config.ConfigWrapper;
import me.eeshe.gtmobs.util.ConfigUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public enum Particle {
  ;

  private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(GTMobs.getInstance(), null, "particles.yml");
  private final String path;
  private final boolean defaultEnabled;
  private final org.bukkit.Particle defaultParticle;
  private final int defaultAmount;
  private final double defaultXOffSet;
  private final double defaultYOffSet;
  private final double defaultZOffSet;
  private final double defaultExtra;
  private final Object defaultData;

  Particle(String path, boolean defaultEnabled, org.bukkit.Particle defaultParticle, int defaultAmount,
      double defaultXOffSet, double defaultYOffSet, double defaultZOffSet, double defaultExtra, Object defaultData) {
    this.path = path;
    this.defaultEnabled = defaultEnabled;
    this.defaultParticle = defaultParticle;
    this.defaultAmount = defaultAmount;
    this.defaultXOffSet = defaultXOffSet;
    this.defaultYOffSet = defaultYOffSet;
    this.defaultZOffSet = defaultZOffSet;
    this.defaultExtra = defaultExtra;
    this.defaultData = defaultData;
  }

  /**
   * Setups the configurable Particle enum.
   */
  public static void setup() {
    writeDefaults();
  }

  private static void writeDefaults() {
    FileConfiguration config = CONFIG_WRAPPER.getConfig();
    for (Particle particle : Particle.values()) {
      String path = particle.getPath();
      config.addDefault(path + ".particle", particle.getDefaultParticle().name());
      config.addDefault(path + ".enable", particle.getDefaultEnabled());
      config.addDefault(path + ".amount", particle.getDefaultAmount());
      config.addDefault(path + ".x-off-set", particle.getDefaultXOffSet());
      config.addDefault(path + ".y-off-set", particle.getDefaultYOffSet());
      config.addDefault(path + ".z-off-set", particle.getDefaultZOffSet());
      config.addDefault(path + ".extra", particle.getDefaultExtra());
      if (particle.getDefaultData() != null) {
        Object defaultData = particle.getDefaultData();
        config.addDefault(path + ".data", defaultData.toString());
      }
    }
    config.options().copyDefaults(true);

    CONFIG_WRAPPER.saveConfig();
    CONFIG_WRAPPER.reloadConfig();
  }

  public static ConfigWrapper getConfigWrapper() {
    return CONFIG_WRAPPER;
  }

  public void spawn(Player player, Location location) {
    fetchConfigParticle().spawn(player, location);
  }

  public void spawn(Location location) {
    fetchConfigParticle().spawn(location);
  }

  private ConfigParticle fetchConfigParticle() {
    ConfigParticle configParticle = ConfigUtil.fetchConfigParticle(CONFIG_WRAPPER.getConfig(), path);
    if (configParticle == null)
      return createDefaultConfigParticle();

    return configParticle;
  }

  private ConfigParticle createDefaultConfigParticle() {
    return new ConfigParticle(defaultEnabled, defaultParticle, defaultAmount, defaultXOffSet, defaultYOffSet,
        defaultZOffSet, defaultExtra, defaultData);
  }

  public String getPath() {
    return path;
  }

  public org.bukkit.Particle getDefaultParticle() {
    return defaultParticle;
  }

  public boolean getDefaultEnabled() {
    return defaultEnabled;
  }

  public int getDefaultAmount() {
    return defaultAmount;
  }

  public double getDefaultXOffSet() {
    return defaultXOffSet;
  }

  public double getDefaultYOffSet() {
    return defaultYOffSet;
  }

  public double getDefaultZOffSet() {
    return defaultZOffSet;
  }

  public double getDefaultExtra() {
    return defaultExtra;
  }

  public Object getDefaultData() {
    return defaultData;
  }

  public boolean isDefaultEnabled() {
    return defaultEnabled;
  }
}
