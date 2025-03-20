package me.eeshe.gtmobs.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.mojang.authlib.properties.Property;

import me.eeshe.gtmobs.GTMobs;
import net.minecraft.server.v1_12_R1.DataWatcherObject;
import net.minecraft.server.v1_12_R1.DataWatcherRegistry;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PacketPlayOutScoreboardTeam;
import net.minecraft.server.v1_12_R1.PlayerConnection;
import net.minecraft.server.v1_12_R1.Scoreboard;
import net.minecraft.server.v1_12_R1.ScoreboardTeam;

public class FakePlayer {
  private static final Map<Integer, FakePlayer> FAKE_PLAYERS = new HashMap<>();

  private final EntityPlayer entityPlayer;
  private final String[] splitMobName;
  private final Map<EnumItemSlot, ItemStack> equipment;
  private final CompletableFuture<Property> skinFuture;

  public FakePlayer(EntityPlayer entityPlayer, String[] splitMobName,
      Map<EnumItemSlot, ItemStack> equipment, CompletableFuture<Property> skinFuture) {
    this.entityPlayer = entityPlayer;
    this.splitMobName = splitMobName;
    this.equipment = equipment;
    this.skinFuture = skinFuture;

    FAKE_PLAYERS.put(entityPlayer.getId(), this);
  }

  public static boolean isFakePlayerId(int entityId) {
    return FAKE_PLAYERS.containsKey(entityId);
  }

  /**
   * Spawns the FakePlayer for the passed player
   *
   * @param player Player to spawn the FakePlayer to
   */
  public void spawn(Player player, LivingEntity livingEntity) {
    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
    sendFakePlayerPackets(nmsPlayer);
    skinFuture.whenComplete((arg0, arg1) -> {
      sendFakePlayerPackets(nmsPlayer);
    });
  }

  /**
   * Sends the packets to spawn the FakePlayer for the passed NMS Player
   *
   * @param nmsPlayer NMS Player to send the packets to
   */
  private void sendFakePlayerPackets(EntityPlayer nmsPlayer) {
    PlayerConnection playerConnection = nmsPlayer.playerConnection;

    // Remove GTMob living entity
    playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));

    // Add fake player to TAB list
    playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

    // Spawn fake player
    playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));

    // Enable skin layers
    entityPlayer.getDataWatcher().set(new DataWatcherObject<>(13, DataWatcherRegistry.a), (byte) 127);

    if (splitMobName != null && splitMobName.length != 1) {
      String suffix = splitMobName.length > 2 ? splitMobName[2] : "";
      ScoreboardTeam nmsTeam = createNMSTeam(splitMobName[0], suffix);

      // Send team creation packet
      playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(nmsTeam, 0));

      // Send packet to add the fake player to the team
      playerConnection.sendPacket(new PacketPlayOutScoreboardTeam(
          nmsTeam,
          List.of(entityPlayer.getName()),
          3));
    }

    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(),
        () -> {
          playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER,
              entityPlayer));
          for (PacketPlayOutEntityEquipment equipmentPacket : createEquipmentPackets()) {
            playerConnection.sendPacket(equipmentPacket);
          }
        },
        5L);
  }

  /**
   * Creates an NMS Team with the passed prefix and suffix
   *
   * @param prefix Prefix of the team
   * @param suffix Suffix of the team
   * @return Created NMS team
   */
  private ScoreboardTeam createNMSTeam(String prefix, String suffix) {
    ScoreboardTeam scoreboardTeam = new ScoreboardTeam(new Scoreboard(),
        String.valueOf(ThreadLocalRandom.current().nextInt(999999999)));
    scoreboardTeam.setPrefix(prefix);
    scoreboardTeam.setSuffix(suffix);

    return scoreboardTeam;
  }

  /**
   * Creates the equipment packets for FakePlayer
   *
   * @return List of equipment packets to send
   */
  private List<PacketPlayOutEntityEquipment> createEquipmentPackets() {
    int entityId = entityPlayer.getId();
    List<PacketPlayOutEntityEquipment> equipmentPackets = new ArrayList<>();
    for (EnumItemSlot enumItemSlot : EnumItemSlot.values()) {
      equipmentPackets.add(new PacketPlayOutEntityEquipment(
          entityId,
          enumItemSlot,
          equipment.getOrDefault(enumItemSlot, null)));
    }
    return equipmentPackets;
  }

  public void clearEquipment() {
    for (EnumItemSlot enumItemSlot : EnumItemSlot.values()) {
      entityPlayer.setEquipment(enumItemSlot, null);
    }
    for (PacketPlayOutEntityEquipment equipmentPacket : createEquipmentPackets()) {
      for (Player player : Bukkit.getOnlinePlayers()) {
        (((CraftPlayer) player).getHandle()).playerConnection.sendPacket(equipmentPacket);
      }
    }
  }

  public static Map<Integer, FakePlayer> getFakePlayers() {
    return FAKE_PLAYERS;
  }

  public EntityPlayer getEntityPlayer() {
    return entityPlayer;
  }

  public CompletableFuture<Property> getSkinFuture() {
    return skinFuture;
  }
}
