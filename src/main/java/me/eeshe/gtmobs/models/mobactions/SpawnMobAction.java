package me.eeshe.gtmobs.models.mobactions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.util.LocationUtil;
import me.eeshe.gtmobs.util.LogUtil;

public class SpawnMobAction extends MobAction {

  private final String mobId;
  private final int amount;
  private final double radius;
  private final long delayTicks;

  public SpawnMobAction(String mobId, int amount, double radius, long delayTicks) {
    this.mobId = mobId;
    this.amount = amount;
    this.radius = radius;
    this.delayTicks = delayTicks;
  }

  @Override
  public void execute(LivingEntity gtmobEntity, Entity attacker) {
    GTMob gtMob = GTMob.fromId(mobId);
    if (gtMob == null) {
      LogUtil.sendWarnLog("Unknown GTMob '" + mobId + "' configured for SpawnMobAction.");
      return;
    }
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      Location gtMobLocation = gtmobEntity.getLocation();
      for (int i = 0; i < amount; i++) {
        gtMob.spawn(LocationUtil.generateRandomLocationRadius(gtMobLocation, radius));
      }
    }, delayTicks);
    return;
  }

  @Override
  public String toString() {
    return getMobActionType().name() + ":" + String.join("-", List.of(
        mobId,
        String.valueOf(amount),
        String.valueOf(radius),
        String.valueOf(delayTicks)));
  }

  public String getMobId() {
    return mobId;
  }

  public int getAmount() {
    return amount;
  }

  public double getRadius() {
    return radius;
  }

  public long getDelayTicks() {
    return delayTicks;
  }
}
