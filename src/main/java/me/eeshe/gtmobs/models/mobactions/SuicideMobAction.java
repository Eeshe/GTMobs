package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.LivingEntity;

public class SuicideMobAction extends MobAction {

  public SuicideMobAction(double chance, MobActionTarget actionTarget) {
    super(chance, actionTarget);
  }

  @Override
  public void execute(LivingEntity gtmobEntity, LivingEntity attacker) {
    LivingEntity target = findActionTarget(gtmobEntity, attacker);
    if (target == null) {
      return;
    }
    target.setHealth(0);
  }
}
