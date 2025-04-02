package me.eeshe.gtmobs.files.config;

import java.text.DecimalFormat;

import org.bukkit.configuration.file.FileConfiguration;

import me.eeshe.gtmobs.GTMobs;

public class MainConfig extends ConfigWrapper {
  private static final String MOB_SPAWN_LIMIT_PATH = "mob-spawn-limit";
  private static final String PACKET_DEBUGGING_PATH = "packet-debugging";
  private static final String DESPAWN_SETTINGS_PATH = "despawn-settings";
  private static final String DESPAWN_TIME_PATH = DESPAWN_SETTINGS_PATH + ".time";
  private static final String DESPAWN_RADIUS_PATH = DESPAWN_SETTINGS_PATH + ".radius";

  public MainConfig(GTMobs plugin) {
    super(plugin, null, "config.yml");
  }

  @Override
  public void writeDefaults() {
    FileConfiguration config = getConfig();

    config.addDefault("decimal-format", "#,###.##");
    config.addDefault(MOB_SPAWN_LIMIT_PATH, 100);
    config.addDefault(PACKET_DEBUGGING_PATH, false);
    writeDefaultDespawnSettings();

    config.options().copyDefaults(true);
    saveConfig();
    reloadConfig();
  }

  /**
   * Writes the default despawn settings for GTMobs
   */
  private void writeDefaultDespawnSettings() {
    FileConfiguration config = getConfig();

    config.addDefault(DESPAWN_TIME_PATH, 120);
    config.addDefault(DESPAWN_RADIUS_PATH, 30);
  }

  /**
   * Returns the configured DecimalFormat.
   *
   * @return Configured DecimalFormat.
   */
  public DecimalFormat getDecimalFormat() {
    return new DecimalFormat(getConfig().getString("decimal-format", "#,###.##"));
  }

  /**
   * Returns the configured mob spawn limit.
   *
   * @return Configured mob spawn limit.
   */
  public int getMobSpawnLimit() {
    return getConfig().getInt(MOB_SPAWN_LIMIT_PATH);
  }

  /**
   * Returns the configured packet debugging configuration.
   *
   * @return Configured packet debugging configuration.
   */
  public boolean isPacketDebuggingEnabled() {
    return getConfig().getBoolean(PACKET_DEBUGGING_PATH);
  }

  /**
   * Returns the configured time before a GTMob despawns in seconds
   *
   * @return Time before a GTMob despawns in seconds
   */
  public long getDespawnTimeSeconds() {
    return getConfig().getLong(DESPAWN_TIME_PATH);
  }

  /**
   * Returns the configured distance at which GTMobs start despawning
   *
   * @return Distance at which GTMobs start despawning
   */
  public double getDespawnRadius() {
    return getConfig().getDouble(DESPAWN_RADIUS_PATH);
  }
}
