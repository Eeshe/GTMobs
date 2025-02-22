package me.eeshe.gtmobs.models;

import java.util.concurrent.CompletableFuture;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.mojang.authlib.properties.Property;

import me.eeshe.gtmobs.GTMobs;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;

public class FakePlayer {
  private final EntityPlayer entityPlayer;
  private final CompletableFuture<Property> skinFuture;

  public FakePlayer(EntityPlayer entityPlayer, CompletableFuture<Property> skinFuture) {
    this.entityPlayer = entityPlayer;
    this.skinFuture = skinFuture;
  }

  public void spawn(Player player) {
    EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

    // Remove GTMob living entity
    nmsPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));

    // Add fake player to TAB list
    nmsPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

    // Spawn fake player
    nmsPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));

    // Remove fake player from TAB list
    nmsPlayer.playerConnection
        .sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));

    skinFuture.whenComplete((arg0, arg1) -> {
      // Remove GTMob living entity
      nmsPlayer.playerConnection.sendPacket(new PacketPlayOutEntityDestroy(entityPlayer.getId()));

      // Add fake player to TAB list
      nmsPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));

      // Spawn fake player
      nmsPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(entityPlayer));

      Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(),
          () -> nmsPlayer.playerConnection
              .sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer)),
          5L);
    });
  }

  public EntityPlayer getEntityPlayer() {
    return entityPlayer;
  }

  public CompletableFuture<Property> getSkinFuture() {
    return skinFuture;
  }
}
