package me.eeshe.gtmobs.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.events.EntityDamageGTMobEvent;
import me.eeshe.gtmobs.events.GTMobDamageEntityEvent;
import me.eeshe.gtmobs.events.GTMobDeathEvent;
import me.eeshe.gtmobs.models.ActiveMob;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.config.ConfigParticle;
import me.eeshe.gtmobs.models.config.Sound;
import me.eeshe.gtmobs.models.mobactions.MobActionChain;

public class GTMobHandler implements Listener {
  private final GTMobs plugin;

  public GTMobHandler(GTMobs plugin) {
    this.plugin = plugin;
  }

  /**
   * Listens when two entities damage each other and handles any interaction with
   * a GTMob.
   *
   * @param event EntityDamageByEntityEvent.
   */
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.isCancelled()) {
      return;
    }
    Entity damager = event.getDamager();
    Entity damaged = event.getEntity();
    if (ActiveMob.fromEntity(damager) != null) {
      Bukkit.getPluginManager()
          .callEvent(new GTMobDamageEntityEvent(ActiveMob.fromEntity(damager), damaged));
    } else if (ActiveMob.fromEntity(damaged) != null) {
      Bukkit.getPluginManager()
          .callEvent(new EntityDamageGTMobEvent(damager, ActiveMob.fromEntity(damaged)));
    }
  }

  /**
   * Listens when a GTMob is damaged and executes its on-hit actions.
   *
   * @param event EntityDamageGTMobEvent.
   */
  @EventHandler
  public void onEntityDamageGTMob(EntityDamageGTMobEvent event) {
    ActiveMob activeMob = event.getActiveMob();
    GTMob gtMob = activeMob.getGTMob();

    Entity damager = event.getDamager();
    for (MobActionChain mobActionChain : gtMob.getOnHitActions()) {
      mobActionChain.attemptExecution(activeMob.getLivingEntity(), damager);
    }
    Location location = activeMob.getLivingEntity().getLocation().add(0, 1, 0);
    for (ConfigParticle configParticle : gtMob.getOnHitParticles()) {
      configParticle.spawn(location);
    }
    Sound.MOB_HIT_SOUND.play(activeMob.getLivingEntity().getLocation());
  }

  /**
   * Listens when a GTMob damages an entity and executes the onHitTarget
   * actions.
   *
   * @param event GTMobDamageEntityEvent
   */
  @EventHandler
  public void onGTMobDamageEntity(GTMobDamageEntityEvent event) {
    ActiveMob activeMob = event.getActiveMob();
    GTMob gtMob = activeMob.getGTMob();
    Entity damaged = event.getDamaged();

    for (MobActionChain mobActionChain : gtMob.getOnTargetHitActions()) {
      mobActionChain.attemptExecution(activeMob.getLivingEntity(), damaged);
    }
  }

  /**
   * Listens when an entity dies, if it's a GTMob, calls the GTMobDeathEvent.
   *
   * @param event EntityDeathEvent.
   */
  @EventHandler
  public void onEntityDeath(EntityDeathEvent event) {
    LivingEntity entity = event.getEntity();
    ActiveMob activeMob = ActiveMob.fromEntity(entity);
    if (activeMob == null) {
      return;
    }
    event.setDroppedExp(activeMob.getGTMob().getExperienceDrop().generateRandom());
    // event.getDrops().clear();
    Bukkit.getPluginManager().callEvent(new GTMobDeathEvent(activeMob, entity.getKiller()));
  }

  /**
   * Listens when a GTMob dies and handles it.
   *
   * @param event GTMobDeathEvent.
   */
  @EventHandler
  public void onGTMobDeath(GTMobDeathEvent event) {
    ActiveMob activeMob = event.getActiveMob();

    LivingEntity livingEntity = activeMob.getLivingEntity();
    Location particleLocation = livingEntity.getLocation().add(0, 1, 0);
    GTMob gtMob = activeMob.getGTMob();

    for (MobActionChain mobActionChain : gtMob.getOnDeathActions()) {
      mobActionChain.attemptExecution(livingEntity, event.getKiller());
    }
    for (ConfigParticle configParticle : gtMob.getOnDeathParticles()) {
      configParticle.spawn(particleLocation);
    }
    Bukkit.getScheduler().runTaskLater(plugin, () -> activeMob.unregister(), 1L);
  }
}
