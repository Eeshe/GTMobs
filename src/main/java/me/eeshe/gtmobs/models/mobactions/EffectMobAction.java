package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

public class EffectMobAction extends MobAction {

  private final PotionEffect potionEffect;

  public EffectMobAction(double chance, MobActionTarget actionTarget, PotionEffect potionEffect) {
    super(chance, actionTarget);
    this.potionEffect = potionEffect;
  }

  @Override
  public boolean execute(LivingEntity gtmobEntity, Entity attacker) {
    if (!super.execute(gtmobEntity, attacker)) {
      return false;
    }
    LivingEntity potionTarget = findActionTarget(gtmobEntity, attacker);
    if (potionTarget == null) {
      return false;
    }
    potionTarget.addPotionEffect(potionEffect, true);
    return true;
  }

  public PotionEffect getPotionEffect() {
    return potionEffect;
  }
}
