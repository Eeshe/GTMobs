package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.ConfigParticle;

public class ParticleMobAction extends MobAction {
  private final ConfigParticle configParticle;
  private final long delayTicks;

  public ParticleMobAction(ConfigParticle configParticle, long delayTicks) {
    this.configParticle = configParticle;
    this.delayTicks = delayTicks;
  }

  @Override
  public void execute(LivingEntity gtMobEntity, Entity attacker) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(),
        () -> configParticle.spawn(gtMobEntity.getLocation()), delayTicks);
  }

  @Override
  public String toString() {
    return MobActionType.PARTICLE.name() + ":" + configParticle.toString();
  }
}
