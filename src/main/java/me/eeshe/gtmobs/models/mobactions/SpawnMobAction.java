package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.util.LocationUtil;
import me.eeshe.gtmobs.util.LogUtil;

public class SpawnMobAction extends MobAction {

  private final String mobId;
  private final IntRange amountRange;
  private final double radius;
  private final IntRange delayTicks;

  public SpawnMobAction(double chance, String mobId, IntRange amountRange, double radius, IntRange delayTicks) {
    super(chance);
    this.mobId = mobId;
    this.amountRange = amountRange;
    this.radius = radius;
    this.delayTicks = delayTicks;
  }

  @Override
  public boolean execute(LivingEntity gtmobEntity, Entity attacker) {
    if (!super.execute(gtmobEntity, attacker)) {
      return false;
    }
    GTMob gtMob = GTMob.fromId(mobId);
    if (gtMob == null) {
      LogUtil.sendWarnLog("Unknown GTMob '" + mobId + "' configured for SpawnMobAction.");
      return false;
    }
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      Location gtMobLocation = gtmobEntity.getLocation();
      for (int i = 0; i < amountRange.generateRandom(); i++) {
        gtMob.spawn(LocationUtil.generateRandomLocationRadius(gtMobLocation, radius));
      }
    }, delayTicks.generateRandom());
    return true;
  }

  public String getMobId() {
    return mobId;
  }

  public IntRange getAmountRange() {
    return amountRange;
  }

  public double getRadius() {
    return radius;
  }

  public IntRange getDelayTicks() {
    return delayTicks;
  }
}
