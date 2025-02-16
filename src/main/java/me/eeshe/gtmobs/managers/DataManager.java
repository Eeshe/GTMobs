package me.eeshe.gtmobs.managers;

import me.eeshe.gtmobs.GTMobs;

public abstract class DataManager {
  private final GTMobs plugin;

  public DataManager(GTMobs plugin) {
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

  public GTMobs getPlugin() {
    return plugin;
  }
}
