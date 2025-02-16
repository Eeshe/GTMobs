package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

public abstract class MobAction {
  private final MobActionTarget actionTarget;

  public MobAction() {
    this.actionTarget = null;
  }

  public MobAction(MobActionTarget actionTarget) {
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
   * @param gtMobEntity GTMob that is executing the action.
   * @param attacker    Entity that is attacking the GTMob.
   * @return True if the MobAction was executed.
   */
  public abstract void execute(LivingEntity gtMobEntity, Entity attacker);

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

  public MobActionTarget getActionTarget() {
    return actionTarget;
  }

  public abstract String toString();
}
