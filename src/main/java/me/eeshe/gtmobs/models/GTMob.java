package me.eeshe.gtmobs.models;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.ConfigParticle;
import me.eeshe.gtmobs.models.config.ConfigSound;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.models.mobactions.MobAction;
import me.eeshe.gtmobs.models.mobactions.MobActionChain;
import me.eeshe.gtmobs.util.StringUtil;

public class GTMob {

  private final String id;
  private final EntityType entityType;
  private final String displayName;
  private final Map<Attribute, Double> attributes;
  private final ConfigSound spawnSound;
  private final List<ConfigParticle> onHitParticles;
  private final List<ConfigParticle> onDeathParticles;
  private final IntRange experienceDrop;
  private final List<MobActionChain> onHitActions;
  private final List<MobActionChain> onShotActions;
  private final List<MobActionChain> onDeathActions;

  public GTMob(String id, EntityType entityType, String displayName, Map<Attribute, Double> attributes,
      ConfigSound spawnSound, List<ConfigParticle> onHitParticles, List<ConfigParticle> onDeathParticles,
      IntRange experienceDrop, List<MobActionChain> onHitActions,
      List<MobActionChain> onShotActions, List<MobActionChain> onDeathActions) {
    this.id = id;
    this.entityType = entityType;
    this.displayName = displayName;
    this.attributes = attributes;
    this.spawnSound = spawnSound;
    this.onHitParticles = onHitParticles;
    this.onDeathParticles = onDeathParticles;
    this.experienceDrop = experienceDrop;
    this.onHitActions = onHitActions;
    this.onShotActions = onShotActions;
    this.onDeathActions = onDeathActions;
  }

  /**
   * Searches and returns the GTMob corresponding to the passed ID.
   *
   * @param id ID to search for.
   * @return GTMob corresponding to the passed ID.
   */
  public static GTMob fromId(String id) {
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
    if (location == null) {
      return null;
    }
    LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
    livingEntity.setCustomName(StringUtil.formatColor(displayName));
    livingEntity.setCustomNameVisible(true);

    // Apply attributes
    for (Entry<Attribute, Double> entry : attributes.entrySet()) {
      AttributeInstance attributeInstance = livingEntity.getAttribute(entry.getKey());
      if (attributeInstance == null) {
        continue;
      }
      attributeInstance.setBaseValue(entry.getValue());
    }
    // Play spawn sound
    if (spawnSound != null) {
      spawnSound.play(location);
    }
    new ActiveMob(livingEntity, id).register();
    return livingEntity;
  }

  public String getId() {
    return id;
  }

  public EntityType getEntityType() {
    return entityType;
  }

  public String getDisplayName() {
    return displayName;
  }

  public Map<Attribute, Double> getAttributes() {
    return attributes;
  }

  public ConfigSound getSpawnSound() {
    return spawnSound;
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

  public List<MobActionChain> getOnShotActions() {
    return onShotActions;
  }

  public List<MobActionChain> getOnDeathActions() {
    return onDeathActions;
  }

}
