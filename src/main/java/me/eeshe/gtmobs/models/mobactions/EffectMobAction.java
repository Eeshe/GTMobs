package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

public class EffectMobAction extends MobAction {

  private final PotionEffect potionEffect;

  public EffectMobAction(double chance, PotionEffect potionEffect, MobActionTarget actionTarget) {
    super(chance, actionTarget);
    this.potionEffect = potionEffect;
  }

  @Override
  public void execute(LivingEntity gtmobEntity, LivingEntity attacker) {
    LivingEntity potionTarget = findActionTarget(gtmobEntity, attacker);
    if (potionTarget == null) {
      return;
    }
    potionTarget.addPotionEffect(potionEffect, true);
  }
}
