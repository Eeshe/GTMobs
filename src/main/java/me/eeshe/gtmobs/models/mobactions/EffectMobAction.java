package me.eeshe.gtmobs.models.mobactions;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

public class EffectMobAction extends MobAction {

  private final PotionEffect potionEffect;

  public EffectMobAction(MobActionTarget actionTarget, PotionEffect potionEffect) {
    super(actionTarget);
    this.potionEffect = potionEffect;
  }

  @Override
  public void execute(LivingEntity gtmobEntity, Entity attacker) {
    LivingEntity potionTarget = findActionTarget(gtmobEntity, attacker);
    if (potionTarget == null) {
      return;
    }
    potionTarget.addPotionEffect(potionEffect, true);
    return;
  }

  @Override
  public String toString() {
    return getMobActionType().name() + ":" + String.join("-", List.of(
        potionEffect.getType().getName(),
        String.valueOf(potionEffect.getAmplifier() + 1),
        String.valueOf(potionEffect.getDuration()),
        getActionTarget().name()));
  }

  public PotionEffect getPotionEffect() {
    return potionEffect;
  }
}
