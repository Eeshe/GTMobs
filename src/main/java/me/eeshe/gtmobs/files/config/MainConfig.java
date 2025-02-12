package me.eeshe.gtmobs.files.config;

import me.eeshe.gtmobs.GTMobs;
import org.bukkit.configuration.file.FileConfiguration;

import java.text.DecimalFormat;

public class MainConfig extends ConfigWrapper {

  public MainConfig(GTMobs plugin) {
    super(plugin, null, "config.yml");
  }

  @Override
  public void writeDefaults() {
    FileConfiguration config = getConfig();

    config.addDefault("decimal-format", "#,###.##");

    config.options().copyDefaults(true);
    saveConfig();
    reloadConfig();
  }

  /**
   * Returns the configured DecimalFormat.
   *
   * @return Configured DecimalFormat.
   */
  public DecimalFormat getDecimalFormat() {
    return new DecimalFormat(getConfig().getString("decimal-format", "#,###.##"));
  }
}
