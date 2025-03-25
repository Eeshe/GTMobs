package me.eeshe.gtmobs.listeners;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.ActiveMob;
import me.eeshe.gtmobs.models.FakePlayer;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.ItemEntity;
import me.eeshe.gtmobs.util.LogUtil;
import net.minecraft.server.v1_12_R1.DataWatcher;
import net.minecraft.server.v1_12_R1.DataWatcher.Item;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.PacketPlayOutUpdateAttributes;

public class PacketHandler implements Listener {
  // USED ONLY TO DEBUG WHAT PACKETS CRASH CLIENTS, DO NOT USE IN PRODUCTION
  private static final List<Class<?>> IGNORED_PACKET_CLASSES = List.of();

  private static final List<Class<?>> HANDLED_PACKET_CLASSES = List.of(
      PacketPlayOutEntityMetadata.class,
      PacketPlayOutEntityStatus.class,
      PacketPlayOutUpdateAttributes.class,
      PacketPlayOutAnimation.class,
      PacketPlayOutSpawnEntityLiving.class);
  private static final List<Integer> FAKE_PLAYER_IGNORED_DATA_WATCHER_TYPES = List.of(
      12, 14);
  private static final List<Integer> ITEM_ENTITY_IGNORED_DATA_WATCHER_TYPES = List.of(
      7, 8, 9, 10, 12, 14);

  private final Map<UUID, Deque<Packet<?>>> packetDequeus = new HashMap<>();
  private final GTMobs plugin;

  public PacketHandler(GTMobs plugin) {
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
    // Bukkit.getScheduler().runTaskLater(plugin,
    // () -> sendDisguisePackets(player), 10L);
    registerDisguisePacketListener(player);
  }

  /**
   * Registers the packet listener that will handle MobDisguise packets
   *
   * @param player Player to register the listener to
   */
  private void registerDisguisePacketListener(Player player) {
    packetDequeus.put(player.getUniqueId(), new ArrayDeque<>(20));
    ChannelDuplexHandler handler = new ChannelDuplexHandler() {

      @Override
      public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (HANDLED_PACKET_CLASSES.contains(msg.getClass())) {
          try {
            Packet<?> packet = (Packet<?>) msg;
            int packetEntityId = getPacketEntityId(packet);
            if ((packet instanceof PacketPlayOutUpdateAttributes || packet instanceof PacketPlayOutAnimation) &&
                ItemEntity.isItemEntityId(packetEntityId)) {
              // Item entities shouldn't send UpdateAttribute or Animation
              // packets
              return;
            }
            if (msg instanceof PacketPlayOutSpawnEntityLiving) {
              handleSpawnEntityLivingPacket(player, (PacketPlayOutSpawnEntityLiving) packet);
            }
            if (FakePlayer.isFakePlayerId(packetEntityId) ||
                ItemEntity.isItemEntityId(packetEntityId)) {
              if (packet instanceof PacketPlayOutEntityStatus &&
                  !shouldSendStatusPacket((PacketPlayOutEntityStatus) packet)) {
                return;
              }
              if (packet instanceof PacketPlayOutEntityMetadata) {
                handleMetadataPacket((PacketPlayOutEntityMetadata) packet);
              }
            }
          } catch (Exception ignore) {
          }
        }
        if (IGNORED_PACKET_CLASSES.contains(msg.getClass())) {
          return;
        }
        cachePacket(player, (Packet<?>) msg);
        super.write(ctx, msg, promise);
      }
    };
    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
    nmsPlayer.playerConnection.networkManager.channel.pipeline().addBefore("packet_handler",
        "GTMobs",
        handler);
  }

  /**
   * Gets the entity ID of the passed packet through reflection
   *
   * @param packet Packet to get the entity ID of
   * @return Entity ID
   */
  private int getPacketEntityId(Packet<?> packet) {
    // Use reflection to access the packet's private attributes
    Class<?> packetClass = packet.getClass();
    try {
      // Accessing entity ID
      Field idField = packetClass.getDeclaredField("a");
      idField.setAccessible(true);
      int packetEntityId = (int) idField.get(packet);
      idField.setAccessible(false);
      return packetEntityId;
    } catch (Exception ignored) {
    }
    return -1;
  }

  /**
   * Whether the passed Entity Status packet should be sent.
   * The status data 3 sets the entity as dying, which needs to be avoided to
   * stop the fake players from dropping their fake equipment
   *
   * @param packet PacketPlayOutEntityStatus
   */
  private boolean shouldSendStatusPacket(PacketPlayOutEntityStatus packet) {
    try {
      // Accessing status byte
      Field statusField = packet.getClass().getDeclaredField("b");
      statusField.setAccessible(true);
      byte status = (byte) statusField.get(packet);
      statusField.setAccessible(false);

      return status != 3;
    } catch (Exception ignored) {
    }
    return true;
  }

  /**
   * Handles the removal of crashing datawatchers from the passed metadata packet
   *
   * @param player Player the packet is being sent to
   */
  private void handleMetadataPacket(PacketPlayOutEntityMetadata packet) {
    int packetEntityId = getPacketEntityId(packet);
    try {
      // Accessing DataWatcher
      Field dataWatcherField = packet.getClass().getDeclaredField("b");
      dataWatcherField.setAccessible(true);
      List<DataWatcher.Item<?>> dataWatchers = (List<Item<?>>) dataWatcherField.get(packet);
      dataWatcherField.setAccessible(false);

      // LogUtil.sendWarnLog("DATA WATCHERS: " + dataWatchers.size());
      Iterator<Item<?>> dataWatcherIterator = dataWatchers.iterator();
      while (dataWatcherIterator.hasNext()) {
        Item<?> dataWatcherItem = dataWatcherIterator.next();
        // LogUtil.sendInfoLog("a:a: " + dataWatcherItem.a().a());
        // LogUtil.sendInfoLog("b: " + dataWatcherItem.b());
        // LogUtil.sendInfoLog("b Type: " +
        // dataWatcherItem.b().getClass().getSimpleName());
        // LogUtil.sendInfoLog("#######################################");

        int packetTypeId = dataWatcherItem.a().a();
        if (isIgnoredFakePlayerPacket(packetEntityId, packetTypeId) ||
            isIgnoredItemEntityPacket(packetEntityId, packetTypeId)) {
          dataWatcherIterator.remove();
          continue;
        }
      }
    } catch (Exception ignored) {
    }
  }

  /**
   * Checks if the passed packet information corresponds to a FakePlayer ignored
   * packet
   *
   * @param packetEntityId Entity ID of the packet
   * @param packetTypeId   Type ID of the packet
   * @return True if the packet should be ignored
   */
  private boolean isIgnoredFakePlayerPacket(int packetEntityId, int packetTypeId) {
    return FakePlayer.isFakePlayerId(packetEntityId)
        && FAKE_PLAYER_IGNORED_DATA_WATCHER_TYPES.contains(packetTypeId);
  }

  /**
   * Checks if the passed packet information corresponds to an ItemEntity ignored
   * packet
   *
   * @param packetEntityId Entity ID of the packet
   * @param packetTypeId   Type ID of the packet
   * @return True if the packet should be ignored
   */
  private boolean isIgnoredItemEntityPacket(int packetEntityId, int packetTypeId) {
    return ItemEntity.isItemEntityId(packetEntityId)
        && ITEM_ENTITY_IGNORED_DATA_WATCHER_TYPES.contains(packetTypeId);
  }

  /**
   * Handles the SpawnEntityLiving packet and applies the disguises of the
   * entity
   *
   * @param player Player to handle the packet for
   * @param packet Packet to handle
   */
  private void handleSpawnEntityLivingPacket(Player player,
      PacketPlayOutSpawnEntityLiving packet) {
    try {
      Class<?> packetClass = packet.getClass();

      Field uuidField = packetClass.getDeclaredField("b");
      uuidField.setAccessible(true);
      UUID uuid = (UUID) uuidField.get(packet);
      uuidField.setAccessible(false);

      ActiveMob activeMob = ActiveMob.fromEntity(Bukkit.getEntity(uuid));
      if (activeMob == null) {
        return;
      }
      Bukkit.getScheduler().runTaskLater(plugin,
          () -> activeMob.getGTMob().getDisguise().apply(activeMob, player), 10L);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void cachePacket(Player player, Packet<?> packet) {
    if (!plugin.getMainConfig().isPacketDebuggingEnabled()) {
      return;
    }
    Deque<Packet<?>> deque = packetDequeus.get(player.getUniqueId());
    if (deque.size() == 20) {
      deque.poll();
    }
    deque.offer(packet);
  }

  /**
   * Listens when a player changes worlds and sends the GTMob disguise packets
   *
   * @param event PlayerChangedWorldEvent
   */
  @EventHandler
  public void onPlayerChangeWorlds(PlayerChangedWorldEvent event) {
    // Bukkit.getScheduler().runTaskLater(plugin,
    // () -> sendDisguisePackets(event.getPlayer()), 10L);
  }

  /**
   * Listens when a player respawns and sends the disguise packets to them
   *
   * @param event PlayerRespawnEvent
   */
  @EventHandler
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    // Bukkit.getScheduler().runTaskLater(plugin,
    // () -> sendDisguisePackets(event.getPlayer()), 10L);
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

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent event) {
    if (!plugin.getMainConfig().isPacketDebuggingEnabled()) {
      return;
    }
    Player player = event.getPlayer();
    LogUtil.sendInfoLog("Last 20 packets sent to " + player.getName() + ":");
    for (Packet<?> packet : packetDequeus.get(player.getUniqueId())) {
      LogUtil.sendInfoLog("- " + packet.getClass().getSimpleName());
    }
    packetDequeus.remove(player.getUniqueId());
  }
}
