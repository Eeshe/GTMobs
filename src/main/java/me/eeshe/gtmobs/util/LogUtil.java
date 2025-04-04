package me.eeshe.gtmobs.util;

import java.util.logging.Logger;

import org.bukkit.Bukkit;

import me.eeshe.gtmobs.GTMobs;

public class LogUtil {
  /**
   * Plugin name fetched from the plugin.yml that will be used as a prefix on the
   * console.
   */
  protected static final String PLUGIN_NAME = GTMobs.getInstance().getDescription().getName();
  protected static final Logger LOGGER = Bukkit.getLogger();

  /**
   * Sends a log to the server console informing about the passed text.
   *
   * @param text Text that will be displayed in the server console.
   */
  public static void sendInfoLog(String text) {
    LOGGER.info("[" + PLUGIN_NAME + "] " + text);
  }

  /**
   * Sends a log to the server console warning about the passed text.
   *
   * @param text Text that will be displayed in the server console as a warning.
   */
  public static void sendWarnLog(String text) {
    LOGGER.warning("[" + PLUGIN_NAME + "] " + text);
  }
}
