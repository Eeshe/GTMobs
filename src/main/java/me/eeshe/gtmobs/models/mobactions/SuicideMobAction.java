package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class SuicideMobAction extends MobAction {

  public SuicideMobAction(double chance, MobActionTarget actionTarget) {
    super(chance, actionTarget);
  }

  @Override
  public boolean execute(LivingEntity gtmobEntity, Entity attacker) {
    if (!super.execute(gtmobEntity, attacker)) {
      return false;
    }
    LivingEntity target = findActionTarget(gtmobEntity, attacker);
    if (target == null) {
      return false;
    }
    target.setHealth(0);
    return true;
  }
}
