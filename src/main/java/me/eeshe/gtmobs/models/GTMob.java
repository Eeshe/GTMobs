package me.eeshe.gtmobs.models;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Ageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Zombie;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.ConfigParticle;
import me.eeshe.gtmobs.models.config.ConfigSound;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.models.mobactions.MobActionChain;
import me.eeshe.gtmobs.util.StringUtil;

public class GTMob {

  private final String id;
  private final EntityType entityType;
  private final boolean isBaby;
  private final String displayName;
  private final Map<Attribute, Double> attributes;
  private final List<ConfigSound> spawnSounds;
  private final List<ConfigParticle> spawnParticles;
  private final List<ConfigParticle> onHitParticles;
  private final List<ConfigParticle> onDeathParticles;
  private final IntRange experienceDrop;
  private final List<MobActionChain> onHitActions;
  private final List<MobActionChain> onTargetHitActions;
  private final List<MobActionChain> onDeathActions;

  public GTMob(String id, EntityType entityType, boolean isBaby, String displayName,
      Map<Attribute, Double> attributes, List<ConfigSound> spawnSounds,
      List<ConfigParticle> spawnParticles, List<ConfigParticle> onHitParticles,
      List<ConfigParticle> onDeathParticles, IntRange experienceDrop,
      List<MobActionChain> onHitActions, List<MobActionChain> onTargetHitActions,
      List<MobActionChain> onDeathActions) {
    this.id = id;
    this.entityType = entityType;
    this.isBaby = isBaby;
    this.displayName = displayName;
    this.attributes = attributes;
    this.spawnSounds = spawnSounds;
    this.spawnParticles = spawnParticles;
    this.onHitParticles = onHitParticles;
    this.onDeathParticles = onDeathParticles;
    this.experienceDrop = experienceDrop;
    this.onHitActions = onHitActions;
    this.onTargetHitActions = onTargetHitActions;
    this.onDeathActions = onDeathActions;
  }

  /**
   * Searches and returns the GTMob corresponding to the passed ID.
   *
   * @param id ID to search for.
   * @return GTMob corresponding to the passed ID.
   */
  public static GTMob fromId(String id) {
    if (id == null) {
      return null;
    }
    return GTMobs.getInstance().getGTMobManager().getGTMobs().get(id);
  }

  /**
   * Registers the GTMob in the GTMobsManager class.
   */
  public void register() {
    GTMobs.getInstance().getGTMobManager().getGTMobs().put(id, this);
  }

  /**
   * Unregisters the GTMob from the GTMobsManager class.
   */
  public void unregister() {
    GTMobs.getInstance().getGTMobManager().getGTMobs().remove(id);
  }

  /**
   * Spawns the GTMob in the specified location.
   *
   * @param location Location to spawn the mob in.
   * @return Spawned LivingEntity.
   */
  public LivingEntity spawn(Location location) {
    return spawn(location, null);
  }

  /**
   * Spawns the GTMob in the specified location from the passed Spawner
   *
   * @param location Location to spawn the mob in
   * @param spawner  Spawner that spawned the mob
   * @return Spawned LivingEntity
   */
  public LivingEntity spawn(Location location, Spawner spawner) {
    if (location == null) {
      return null;
    }
    LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
    livingEntity.setCustomName(StringUtil.formatColor(displayName));
    livingEntity.setCustomNameVisible(true);

    // Attempt to set baby setting
    if (livingEntity instanceof Zombie) {
      ((Zombie) livingEntity).setBaby(isBaby);
    } else if (livingEntity instanceof Ageable) {
      if (isBaby()) {
        ((Ageable) livingEntity).setBaby();
      } else {
        ((Ageable) livingEntity).setAdult();
      }
    }

    // Apply attributes
    for (Entry<Attribute, Double> entry : attributes.entrySet()) {
      AttributeInstance attributeInstance = livingEntity.getAttribute(entry.getKey());
      if (attributeInstance == null) {
        continue;
      }
      attributeInstance.setBaseValue(entry.getValue());
      if (entry.getKey() == Attribute.GENERIC_MAX_HEALTH) {
        livingEntity.setHealth(entry.getValue());
      }
    }
    // Play spawn particles
    for (ConfigParticle spawnParticle : spawnParticles) {
      spawnParticle.spawn(location);
    }
    // Play spawn sound
    for (ConfigSound spawnSound : spawnSounds) {
      spawnSound.play(location);
    }
    new ActiveMob(livingEntity, id, spawner).register();
    return livingEntity;
  }

  public String getId() {
    return id;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public boolean isBaby() {
    return isBaby;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Map<Attribute, Double> getAttributes() {
    return attributes;
  }

  public List<ConfigSound> getSpawnSounds() {
    return spawnSounds;
  }

  public List<ConfigParticle> getSpawnParticles() {
    return spawnParticles;
  }

  public List<ConfigParticle> getOnHitParticles() {
    return onHitParticles;
  }

  public List<ConfigParticle> getOnDeathParticles() {
    return onDeathParticles;
  }

  public IntRange getExperienceDrop() {
    return experienceDrop;
  }

  public List<MobActionChain> getOnHitActions() {
    return onHitActions;
  }

  public List<MobActionChain> getOnTargetHitActions() {
    return onTargetHitActions;
  }

  public List<MobActionChain> getOnDeathActions() {
    return onDeathActions;
  }

}
