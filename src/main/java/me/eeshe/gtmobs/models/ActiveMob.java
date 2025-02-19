package me.eeshe.gtmobs.models;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;

public class ActiveMob {
  private final LivingEntity livingEntity;
  private final String gtMobId;
  private final Spawner spawner;

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
  }

  /**
   * Unregisters the ActiveMob from the ActiveMobManager class.
   */
  public void unregister() {
    GTMobs.getInstance().getActiveMobManager().getActiveMobs().remove(livingEntity.getUniqueId());
    if (spawner != null) {
      spawner.removeSpawnedMob(livingEntity);
    }
  }

  public void despawn() {
    livingEntity.remove();
    unregister();
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
