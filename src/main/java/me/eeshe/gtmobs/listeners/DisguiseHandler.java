package me.eeshe.gtmobs.listeners;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.ActiveMob;
import me.eeshe.gtmobs.models.FakePlayer;
import me.eeshe.gtmobs.models.GTMob;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcher.Item;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;

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
    Player player = event.getPlayer();
    Bukkit.getScheduler().runTaskLater(plugin,
        () -> sendDisguisePackets(player), 10L);
    registerDisguisePacketListener(player);
  }

  /**
   * Registers the packet listener that will handle MobDisguise packets
   *
   * @param player Player to register the listener to
   */
  private void registerDisguisePacketListener(Player player) {
    ChannelDuplexHandler handler = new ChannelDuplexHandler() {

      @Override
      public void write(ChannelHandlerContext ctx, Object packet, ChannelPromise promise) throws Exception {
        if (packet instanceof PacketPlayOutEntityMetadata || packet instanceof PacketPlayOutEntityStatus) {
          try {
            // Use reflection to access the packet's private attributes
            Class<?> packetClass = packet.getClass();

            // Accessing entity ID
            Field idField = packetClass.getDeclaredField("a");
            idField.setAccessible(true);
            int packetEntityId = (int) idField.get(packet);
            idField.setAccessible(false);
            if (FakePlayer.getFakePlayers().containsKey(packetEntityId)) {
              if (packet instanceof PacketPlayOutEntityStatus) {
                // Accessing status byte
                Field statusField = packetClass.getDeclaredField("b");
                statusField.setAccessible(true);
                byte status = (byte) statusField.get(packet);
                statusField.setAccessible(false);
                if (status == 3) {
                  // Status marks the FakePlayer as dying, cancel this so it
                  // doesn't drop items in Minecraft < 1.13
                  return;
                }
              }
              // Accessing DataWatcher
              Field dataWatcherField = packetClass.getDeclaredField("b");
              dataWatcherField.setAccessible(true);
              List<DataWatcher.Item<?>> dataWatchers = (List<Item<?>>) dataWatcherField.get(packet);
              dataWatcherField.setAccessible(false);

              Iterator<Item<?>> dataWatcherIterator = dataWatchers.iterator();
              while (dataWatcherIterator.hasNext()) {
                Item<?> dataWatcherItem = dataWatcherIterator.next();
                if (dataWatcherItem.a().a() == 14 || dataWatcherItem.a().a() == 12) {
                  // These two values crash clients, don't send them
                  dataWatcherIterator.remove();
                  continue;
                }
              }
            }
          } catch (Exception ignore) {
          }
        }
        super.write(ctx, packet, promise);
      }
    };
    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
    nmsPlayer.playerConnection.networkManager.channel.pipeline().addBefore("packet_handler",
        "GTMobs",
        handler);
  }

  /**
   * Listens when a player changes worlds and sends the GTMob disguise packets
   *
   * @param event PlayerChangedWorldEvent
   */
  @EventHandler
  public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
    Bukkit.getScheduler().runTaskLater(plugin,
        () -> sendDisguisePackets(event.getPlayer()), 10L);
  }

  /**
   * Listens when a player respawns and sends the disguise packets to them
   *
   * @param event PlayerRespawnEvent
   */
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    Bukkit.getScheduler().runTaskLater(plugin,
        () -> sendDisguisePackets(event.getPlayer()), 10L);
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
