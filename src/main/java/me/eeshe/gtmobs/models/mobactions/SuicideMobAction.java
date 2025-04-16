package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;

public class SuicideMobAction extends MobAction {
  private final long delayTicks;

  public SuicideMobAction(MobActionTarget actionTarget, long delayTicks) {
    super(actionTarget);

    this.delayTicks = delayTicks;
  }

  @Override
  public void execute(LivingEntity gtmobEntity, Entity attacker) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      LivingEntity target = findActionTarget(gtmobEntity, attacker);
      if (target == null) {
        return;
      }
      target.setHealth(0);
    }, delayTicks);
  }

  @Override
  public String toString() {
    return getMobActionType().name() + ":" + getActionTarget().name();
  }
}
