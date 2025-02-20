package me.eeshe.gtmobs.models;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.eeshe.gtmobs.GTMobs;

public class ActiveMob {
  private final LivingEntity livingEntity;
  private final String gtMobId;
  private final Spawner spawner;

  private BukkitTask despawnTask;

  public ActiveMob(LivingEntity livingEntity, String gtMobId, Spawner spawner) {
    this.livingEntity = livingEntity;
    this.gtMobId = gtMobId;
    this.spawner = spawner;
  }

  /**
   * Searches and returns the ActiveMob corresponding to the passed entity.
   *
   * @param entity Entity to search.
   * @return ActiveMob corresponding to the passed entity.
   */
  public static ActiveMob fromEntity(Entity entity) {
    if (!(entity instanceof LivingEntity)) {
      return null;
    }
    return GTMobs.getInstance().getActiveMobManager().getActiveMobs().get(entity.getUniqueId());
  }

  /**
   * Searches and returns the ActiveMob corresponding to the passed Spawner
   *
   * @param spawner Spawner to search
   * @return ActiveMob corresponding to the passed Spawner
   */
  public static ActiveMob fromSpawner(Spawner spawner) {
    if (spawner == null) {
      return null;
    }
    for (ActiveMob activeMob : GTMobs.getInstance().getActiveMobManager().getActiveMobs().values()) {
      if (!spawner.equals(activeMob.getSpawner())) {
        continue;
      }
      return activeMob;
    }
    return null;
  }

  /**
   * Registers the ActiveMob in the ActiveMobManager class.
   */
  public void register() {
    GTMobs.getInstance().getActiveMobManager().getActiveMobs().put(livingEntity.getUniqueId(), this);
    startDespawnTask();
  }

  /**
   * Starts the task that will periodically try to despawn the ActiveMob
   */
  private void startDespawnTask() {
    GTMobs plugin = GTMobs.getInstance();
    this.despawnTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
      private final long despawnTimerSeconds = plugin.getMainConfig().getDespawnTimeSeconds();
      private long despawnCounter = 0;

      @Override
      public void run() {
        if (hasPlayersAround()) {
          despawnCounter = 0;
          return;
        }
        if (despawnCounter >= despawnTimerSeconds) {
          despawn();
          return;
        }
        despawnCounter += 1;
      }
    }, 20L, 20L);
  }

  /**
   * Checks if the ActiveMob has players nearby based on the despawn radius
   *
   * @return True if the ActiveMob has nearby players
   */
  private boolean hasPlayersAround() {
    double despawnRadius = GTMobs.getInstance().getMainConfig().getDespawnRadius();
    for (Entity entity : livingEntity.getNearbyEntities(despawnRadius, despawnRadius, despawnRadius)) {
      if (!(entity instanceof Player)) {
        continue;
      }
      return true;
    }
    return false;
  }

  /**
   * Despawns and unregisters the GTMob
   */
  public void despawn() {
    livingEntity.remove();
    unregister();
  }

  /**
   * Unregisters the ActiveMob from the ActiveMobManager class
   */
  public void unregister() {
    GTMobs.getInstance().getActiveMobManager().getActiveMobs().remove(livingEntity.getUniqueId());
    if (spawner != null) {
      spawner.removeSpawnedMob(livingEntity);
    }
    stopDespawnTask();
  }

  /**
   * Stops the despawn task
   */
  private void stopDespawnTask() {
    if (despawnTask == null) {
      return;
    }
    despawnTask.cancel();
    despawnTask = null;
  }

  public LivingEntity getLivingEntity() {
    return livingEntity;
  }

  public GTMob getGTMob() {
    return GTMob.fromId(gtMobId);
  }

  public String getGTMobId() {
    return gtMobId;
  }

  public Spawner getSpawner() {
    return spawner;
  }
}
