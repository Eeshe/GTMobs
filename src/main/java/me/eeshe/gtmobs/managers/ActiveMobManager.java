package me.eeshe.gtmobs.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.ActiveMob;

public class ActiveMobManager extends DataManager {
  private final Map<UUID, ActiveMob> activeMobs = new HashMap<>();

  public ActiveMobManager(GTMobs plugin) {
    super(plugin);
  }

  @Override
  public void load() {
    startGhostMobTask();
  }

  private void startGhostMobTask() {
    Bukkit.getScheduler().runTaskTimer(getPlugin(), () -> {
      for (ActiveMob activeMob : activeMobs.values()) {
        if (!activeMob.getLivingEntity().isDead()) {
          continue;
        }
        activeMob.sendRemovePacket();
      }
    }, 100L, 100L);
  }

  @Override
  public void unload() {
    killAll(null, 0, false);
  }

  /**
   * Kills all the ActiveMobs
   *
   * @param center          Center of the kill radius
   * @param radius          Radius of the Kill
   * @param clearDisguiseId Whether it should clear the ID of the disguise
   *                        entity
   * @return Killed ActiveMobs
   */
  public int killAll(Location center, double radius, boolean clearDisguiseId) {
    int killedAmount = 0;
    if (radius == 0) {
      for (ActiveMob activeMob : new ArrayList<>(getPlugin().getActiveMobManager().getActiveMobs().values())) {
        activeMob.despawn(clearDisguiseId);
        killedAmount += 1;
      }
    } else if (center != null) {
      for (Entity nearbyEntity : center.getWorld().getNearbyEntities(center, radius, radius, radius)) {
        ActiveMob gtMob = ActiveMob.fromEntity(nearbyEntity);
        if (gtMob == null) {
          continue;
        }
        gtMob.despawn(clearDisguiseId);
        killedAmount += 1;
      }
    }
    return killedAmount;
  }

  public Map<UUID, ActiveMob> getActiveMobs() {
    return activeMobs;
  }

}
