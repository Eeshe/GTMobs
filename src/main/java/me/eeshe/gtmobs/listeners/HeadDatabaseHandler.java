package me.eeshe.gtmobs.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.eeshe.gtmobs.GTMobs;

public class HeadDatabaseHandler implements Listener {
  private final GTMobs plugin;

  public HeadDatabaseHandler(GTMobs plugin) {
    this.plugin = plugin;
  }

  /**
   * Reloads the GTMobs once the HeadDatabase is loaded
   *
   * @param event DatabaseLoadEvent
   */
  @EventHandler
  public void onHeadDatabaseLoad(DatabaseLoadEvent event) {
    plugin.getGTMobManager().load();
  }
}
