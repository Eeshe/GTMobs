package me.eeshe.gtmobs.listeners;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.ActiveMob;
import me.eeshe.gtmobs.models.GTMob;

public class DisguiseHandler implements Listener {
  private final GTMobs plugin;

  public DisguiseHandler(GTMobs plugin) {
    this.plugin = plugin;
  }

  /**
   * Listens when a player joins the server and sends the GTMob disguise packets
   *
   * @param event PlayerJoinEvent
   */
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Bukkit.getScheduler().runTaskLater(plugin,
        () -> sendDisguisePackets(event.getPlayer()), 1L);
  }

  /**
   * Listens when a player changes worlds and sends the GTMob disguise packets
   *
   * @param event PlayerChangedWorldEvent
   */
  @EventHandler
  public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
    Bukkit.getScheduler().runTaskLater(plugin,
        () -> sendDisguisePackets(event.getPlayer()), 1L);
  }

  /**
   * Sends the disguise packets for all the GTMobs in the passed player's world
   *
   * @param player Player to send the packets to
   */
  private void sendDisguisePackets(Player player) {
    World playerWorld = player.getWorld();
    for (ActiveMob activeMob : plugin.getActiveMobManager().getActiveMobs().values()) {
      if (!activeMob.getLivingEntity().getWorld().equals(playerWorld)) {
        continue;
      }
      GTMob gtMob = activeMob.getGTMob();
      if (gtMob.getDisguise() == null) {
        continue;
      }
      gtMob.getDisguise().apply(activeMob, player);
    }
  }
}
