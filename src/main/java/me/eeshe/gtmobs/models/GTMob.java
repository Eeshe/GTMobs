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
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.ConfigKnockback;
import me.eeshe.gtmobs.models.config.ConfigParticle;
import me.eeshe.gtmobs.models.config.ConfigSound;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.models.mobactions.MobActionChain;
import me.eeshe.gtmobs.util.StringUtil;

public class GTMob {

  private final String id;
  private final EntityType entityType;
  private final boolean isBaby;
  private final boolean isAggressive;
  private final boolean disabledVanillaAttack;
  private final String displayName;
  private final MobDisguise disguise;
  private final Map<EquipmentSlot, ItemStack> equipment;
  private final ConfigKnockback meleeKnockback;
  private final Map<Attribute, Double> attributes;
  private final List<ConfigSound> spawnSounds;
  private final List<ConfigParticle> spawnParticles;
  private final List<ConfigParticle> onHitParticles;
  private final List<ConfigParticle> onDeathParticles;
  private final IntRange experienceDrop;
  private final List<MobActionChain> onSpawnActions;
  private final List<MobActionChain> onHitActions;
  private final List<MobActionChain> onTargetHitActions;
  private final List<MobActionChain> onDeathActions;

  public GTMob(String id, EntityType entityType, boolean isBaby, boolean isAggressive,
      boolean disabledVanillaAttack, String displayName, MobDisguise disguise,
      Map<EquipmentSlot, ItemStack> equipment, ConfigKnockback meleeKnockback,
      Map<Attribute, Double> attributes, List<ConfigSound> spawnSounds,
      List<ConfigParticle> spawnParticles, List<ConfigParticle> onHitParticles,
      List<ConfigParticle> onDeathParticles, IntRange experienceDrop,
      List<MobActionChain> onSpawnActions, List<MobActionChain> onHitActions,
      List<MobActionChain> onTargetHitActions, List<MobActionChain> onDeathActions) {
    this.id = id;
    this.entityType = entityType;
    this.isBaby = isBaby;
    this.isAggressive = isAggressive;
    this.disabledVanillaAttack = disabledVanillaAttack;
    this.displayName = displayName;
    this.disguise = disguise;
    this.equipment = equipment;
    this.meleeKnockback = meleeKnockback;
    this.attributes = attributes;
    this.spawnSounds = spawnSounds;
    this.spawnParticles = spawnParticles;
    this.onHitParticles = onHitParticles;
    this.onDeathParticles = onDeathParticles;
    this.experienceDrop = experienceDrop;
    this.onSpawnActions = onSpawnActions;
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
    if (GTMobs.getInstance().getActiveMobManager().hasReachedSpawnLimit(location.getWorld())) {
      return null;
    }
    LivingEntity livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
    if (livingEntity.isDead()) {
      // Mob couldn't spawn
      return null;
    }
    livingEntity.setCustomName(StringUtil.formatColor(displayName));
    livingEntity.setCustomNameVisible(true);
    if (livingEntity instanceof PigZombie) {
      ((PigZombie) livingEntity).setAngry(isAggressive);
    }

    applyAge(livingEntity);
    setEquipment(livingEntity);
    applyAttributes(livingEntity);

    ActiveMob activeMob = new ActiveMob(livingEntity, id, spawner);
    activeMob.register();
    if (disguise != null) {
      disguise.apply(activeMob);
    }
    // Execute spawn actions
    for (MobActionChain mobActionChain : onSpawnActions) {
      mobActionChain.attemptExecution(livingEntity, null);
    }
    // Play spawn particles
    for (ConfigParticle spawnParticle : spawnParticles) {
      spawnParticle.spawn(location);
    }
    // Play spawn sound
    for (ConfigSound spawnSound : spawnSounds) {
      spawnSound.play(location);
    }
    return livingEntity;
  }

  /**
   * Applies the GTMob age to the passed living entity
   *
   * @param livingEntity Living entity to apply the age to
   */
  private void applyAge(LivingEntity livingEntity) {
    if (livingEntity instanceof Zombie) {
      ((Zombie) livingEntity).setBaby(isBaby);
    } else if (livingEntity instanceof Ageable) {
      if (isBaby()) {
        ((Ageable) livingEntity).setBaby();
      } else {
        ((Ageable) livingEntity).setAdult();
      }
    }
  }

  /**
   * Sets the equipment of the GTMob to the passed living entity
   *
   * @param livingEntity Living entity to equip
   */
  private void setEquipment(LivingEntity livingEntity) {
    EntityEquipment entityEquipment = livingEntity.getEquipment();
    if (entityEquipment != null) {
      entityEquipment.setHelmet(equipment.getOrDefault(EquipmentSlot.HEAD, null));
      entityEquipment.setHelmetDropChance(0);
      entityEquipment.setChestplate(equipment.getOrDefault(EquipmentSlot.CHEST, null));
      entityEquipment.setChestplateDropChance(0);
      entityEquipment.setLeggings(equipment.getOrDefault(EquipmentSlot.LEGS, null));
      entityEquipment.setLeggingsDropChance(0);
      entityEquipment.setBoots(equipment.getOrDefault(EquipmentSlot.FEET, null));
      entityEquipment.setBootsDropChance(0);
      entityEquipment.setItemInMainHand(equipment.getOrDefault(EquipmentSlot.HAND, null));
      entityEquipment.setItemInMainHandDropChance(0);
      entityEquipment.setItemInOffHand(equipment.getOrDefault(EquipmentSlot.OFF_HAND, null));
      entityEquipment.setItemInOffHandDropChance(0);
    }
  }

  /**
   * Applies the attributes of the GTMob to the passed living entity
   *
   * @param livingEntity Living entity to apply the attributes to
   */
  private void applyAttributes(LivingEntity livingEntity) {
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

  public boolean isAggressive() {
    return isAggressive;
  }

  public boolean hasDisabledVanillaAttack() {
    return disabledVanillaAttack;
  }

  public String getDisplayName() {
    return displayName;
  }

  public MobDisguise getDisguise() {
    return disguise;
  }

  public Map<EquipmentSlot, ItemStack> getEquipment() {
    return equipment;
  }

  public ConfigKnockback getMeleeKnockback() {
    return meleeKnockback;
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

  public List<MobActionChain> getOnSpawnActions() {
    return onSpawnActions;
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
