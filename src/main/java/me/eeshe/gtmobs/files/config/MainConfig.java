package me.eeshe.gtmobs.files.config;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import me.eeshe.gtmobs.GTMobs;

public class MainConfig extends ConfigWrapper {
  private static final String WORLD_MOB_SPAWN_LIMIT_PATH = "world-mob-spawn-limit";
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
    if (!config.contains(WORLD_MOB_SPAWN_LIMIT_PATH)) {
      for (Entry<String, Integer> entry : Map.ofEntries(Map.entry("world", 100)).entrySet()) {
        config.addDefault(WORLD_MOB_SPAWN_LIMIT_PATH + "." + entry.getKey(), entry.getValue());
      }
    }
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
  public int getMobSpawnLimit(World world) {
    return getConfig().getInt(WORLD_MOB_SPAWN_LIMIT_PATH + "." + world.getName(), Integer.MAX_VALUE);
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
