package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public abstract class MobAction {
  private final double chance;
  private final MobActionTarget actionTarget;

  public MobAction(double chance) {
    this.chance = chance;
    this.actionTarget = null;
  }

  public MobAction(double chance, MobActionTarget actionTarget) {
    this.chance = chance;
    this.actionTarget = actionTarget;
  }

  /**
   * Rolls the chance for the MobAction.
   *
   * @return True if the action should be executed, false if not.
   */
  public boolean rollChance() {
    return Math.random() < chance;
  }

  /**
   * Finds the target of the MobAction based on the MobActionTarget.
   *
   * @param gtmobEntity GTMob.
   * @param attacker    Entity attacking the GTMob.
   * @return Target of the MobAction based on the MobActionTarget.
   */
  public LivingEntity findActionTarget(LivingEntity gtmobEntity, LivingEntity attacker) {
    LivingEntity target = null;
    if (actionTarget == MobActionTarget.SELF) {
      target = gtmobEntity;
    } else if (actionTarget == MobActionTarget.TARGET && gtmobEntity instanceof Monster) {
      target = ((Monster) gtmobEntity).getTarget();
    } else {
      target = attacker;
    }
    return target;
  }

  /**
   * Executes the MobAction.
   *
   * @param gtmobEntity GTMob that is executing the action.
   * @param attacker    Entity that is attacking the GTMob.
   */
  public abstract void execute(LivingEntity gtmobEntity, LivingEntity attacker);
}
