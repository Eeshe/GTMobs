package me.eeshe.gtmobs.models;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;

public class ActiveMob {
  private final LivingEntity livingEntity;
  private final String gtMobId;

  public ActiveMob(LivingEntity livingEntity, String gtMobId) {
    this.livingEntity = livingEntity;
    this.gtMobId = gtMobId;
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
}
