package me.eeshe.gtmobs.models;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_12_R1.EntityItem;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_12_R1.PlayerConnection;

public class ItemEntity {
  private static final Map<Integer, ItemEntity> ITEM_ENTITIES = new HashMap<>();

  private final EntityItem entityItem;

  public ItemEntity(EntityItem entityItem) {
    this.entityItem = entityItem;

    ITEM_ENTITIES.put(entityItem.getId(), this);
  }

  public static boolean isItemEntityId(int entityId) {
    return ITEM_ENTITIES.containsKey(entityId);
  }

  /**
   * Spawns the ItemEntity for the passed Player
   *
   * @param player Player to spawn the ItemEntity for
   */
  public void spawn(Player player) {
    PlayerConnection playerConnection = (((CraftPlayer) player).getHandle()).playerConnection;

    // Spawn entity packet
    playerConnection.sendPacket(new PacketPlayOutSpawnEntity(
        entityItem, 2));

    // Set entity metadata
    playerConnection.sendPacket(new PacketPlayOutEntityMetadata(
        entityItem.getId(),
        entityItem.getDataWatcher(),
        true));
  }

  public static Map<Integer, ItemEntity> getItemEntities() {
    return ITEM_ENTITIES;
  }

  public EntityItem getEntityItem() {
    return entityItem;
  }
}
