package me.eeshe.gtmobs.listeners;

import java.util.Iterator;
import java.util.List;

import org.bukkit.Bukkit;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.FakePlayer;
import me.eeshe.gtmobs.models.ItemEntity;
import me.eeshe.gtmobs.packets.WrapperPlayServerEntityMetadata;
import me.eeshe.gtmobs.util.LogUtil;

public class MetadataHandler {
  private static final List<Integer> FAKE_PLAYER_IGNORED_DATA_WATCHER_TYPES = List.of(
      12, 14);
  private static final List<Integer> ITEM_ENTITY_IGNORED_DATA_WATCHER_TYPES = List.of(
      7, 8, 9, 10, 12, 14);

  private final GTMobs plugin;

  public MetadataHandler(GTMobs plugin) {
    this.plugin = plugin;
    if (!(Bukkit.getPluginManager().isPluginEnabled("ProtocolLib"))) {
      return;
    }
    ProtocolLibrary.getProtocolManager()
        .addPacketListener(new PacketAdapter(plugin, PacketType.Play.Server.ENTITY_METADATA) {

          @Override
          public void onPacketSending(PacketEvent event) {
            event.setPacket(event.getPacket().deepClone());
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event.getPacket());

            int packetEntityId = packet.getId();
            List<WrappedWatchableObject> packetData = packet.getPackedItems();
            Iterator<WrappedWatchableObject> dataIterator = packetData.iterator();
            while (dataIterator.hasNext()) {
              WrappedWatchableObject dataValue = dataIterator.next();
              int packetTypeId = dataValue.getIndex();
              if (isIgnoredFakePlayerPacket(packetEntityId, packetTypeId) ||
                  isIgnoredItemEntityPacket(packetEntityId, packetTypeId)) {
                dataIterator.remove();
                continue;
              }
            }
            packet.setPackedItems(packetData);
            event.setPacket(packet.getHandle());
          }

          @Override
          public void onPacketReceiving(PacketEvent event) {
            LogUtil.sendWarnLog("ENTITY METADATA RECEIVING");
            event.setPacket(event.getPacket().deepClone());
            WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event.getPacket());

            int packetEntityId = packet.getId();
            List<WrappedWatchableObject> packetData = packet.getPackedItems();
            Iterator<WrappedWatchableObject> dataIterator = packetData.iterator();
            while (dataIterator.hasNext()) {
              WrappedWatchableObject dataValue = dataIterator.next();
              int packetTypeId = dataValue.getIndex();
              if (isIgnoredFakePlayerPacket(packetEntityId, packetTypeId) ||
                  isIgnoredItemEntityPacket(packetEntityId, packetTypeId)) {
                dataIterator.remove();
                continue;
              }
            }
            packet.setPackedItems(packetData);
            event.setPacket(packet.getHandle());
          }
        });
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

}
