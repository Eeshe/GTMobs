package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class SuicideMobAction extends MobAction {

  public SuicideMobAction(MobActionTarget actionTarget) {
    super(actionTarget);
  }

  @Override
  public void execute(LivingEntity gtmobEntity, Entity attacker) {
    LivingEntity target = findActionTarget(gtmobEntity, attacker);
    if (target == null) {
      return;
    }
    target.setHealth(0);
  }

  @Override
  public String toString() {
    return getMobActionType().name() + ":" + getActionTarget().name();
  }
}
