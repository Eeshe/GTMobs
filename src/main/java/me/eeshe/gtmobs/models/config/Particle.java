package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.files.config.ConfigWrapper;
import me.eeshe.gtmobs.util.ConfigUtil;

public enum Particle {
  ;

  private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(GTMobs.getInstance(), null, "particles.yml");
  private final String path;
  private final org.bukkit.Particle defaultParticle;
  private final int defaultAmount;
  private final double defaultXOffSet;
  private final double defaultYOffSet;
  private final double defaultZOffSet;
  private final double defaultSpeed;

  private Particle(String path, org.bukkit.Particle defaultParticle, int defaultAmount, double defaultXOffSet,
      double defaultYOffSet, double defaultZOffSet, double defaultSpeed) {
    this.path = path;
    this.defaultParticle = defaultParticle;
    this.defaultAmount = defaultAmount;
    this.defaultXOffSet = defaultXOffSet;
    this.defaultYOffSet = defaultYOffSet;
    this.defaultZOffSet = defaultZOffSet;
    this.defaultSpeed = defaultSpeed;
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
      ConfigUtil.writeConfigParticle(config, particle.getPath(), particle.createDefaultConfigParticle());
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
    return new ConfigParticle(defaultParticle, defaultAmount, defaultXOffSet,
        defaultZOffSet, defaultZOffSet, defaultSpeed);
  }

  public String getPath() {
    return path;
  }

  public org.bukkit.Particle getDefaultParticle() {
    return defaultParticle;
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

  public double getDefaultSpeed() {
    return defaultSpeed;
  }

}
