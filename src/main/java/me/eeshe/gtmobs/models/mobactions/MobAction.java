package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public class MobAction {
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
   * Finds the target of the MobAction based on the MobActionTarget.
   *
   * @param gtmobEntity GTMob.
   * @param attacker    Entity attacking the GTMob.
   * @return Target of the MobAction based on the MobActionTarget.
   */
  public LivingEntity findActionTarget(LivingEntity gtmobEntity, Entity attacker) {
    LivingEntity target = null;
    if (actionTarget == MobActionTarget.SELF) {
      target = gtmobEntity;
    } else if (actionTarget == MobActionTarget.TARGET && gtmobEntity instanceof Monster) {
      target = ((Monster) gtmobEntity).getTarget();
    } else if (attacker instanceof LivingEntity) {
      target = (LivingEntity) attacker;
    }
    return target;
  }

  /**
   * Executes the MobAction.
   *
   * @param gtmobEntity GTMob that is executing the action.
   * @param attacker    Entity that is attacking the GTMob.
   * @return True if the MobAction was executed.
   */
  public boolean execute(LivingEntity gtmobEntity, Entity attacker) {
    return rollChance();
  }

  /**
   * Rolls the chance for the MobAction.
   *
   * @return True if the action should be executed, false if not.
   */
  public boolean rollChance() {
    return Math.random() < chance;
  }

  public MobActionType getMobActionType() {
    if (this instanceof ConsoleCommandMobAction) {
      return MobActionType.CONSOLE_COMMAND;
    } else if (this instanceof EffectMobAction) {
      return MobActionType.EFFECT;
    } else if (this instanceof SpawnMobAction) {
      return MobActionType.SPAWN;
    } else if (this instanceof SuicideMobAction) {
      return MobActionType.SUICIDE;
    } else {
      return null;
    }
  }

  public double getChance() {
    return chance;
  }

  public MobActionTarget getActionTarget() {
    return actionTarget;
  }

}
