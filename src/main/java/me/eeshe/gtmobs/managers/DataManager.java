package me.eeshe.gtmobs.managers;

import org.bukkit.plugin.Plugin;

public abstract class DataManager {
  private final Plugin plugin;

  public DataManager(Plugin plugin) {
    this.plugin = plugin;
  }

  /**
   * Handles all actions upon plugin enabling.
   */
  public void onEnable() {
    load();
  }

  /**
   * Loads the manager.
   */
  public abstract void load();

  /**
   * Handles all actions upon plugin disabling.
   */
  public void onDisable() {
    unload();
  }

  /**
   * Reloads the DataManager.
   */
  public void reload() {
    unload();
    load();
  }

  /**
   * Unloads the manager.
   */
  public abstract void unload();

  public Plugin getPlugin() {
    return plugin;
  }
}
