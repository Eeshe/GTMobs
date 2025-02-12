package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.util.LocationUtil;

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
  public void execute(LivingEntity gtmobEntity, LivingEntity attacker) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      Location gtmobLocation = gtmobEntity.getLocation();
      for (int i = 0; i < amountRange.generateRandom(); i++) {
        // TODO: Spawn mob
      }
    }, delayTicks.generateRandom());
  }
}
