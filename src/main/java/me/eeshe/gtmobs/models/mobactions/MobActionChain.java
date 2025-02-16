package me.eeshe.gtmobs.models.mobactions;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

public class MobActionChain {
  private final List<MobAction> mobActions;
  private final double chance;

  public MobActionChain(List<MobAction> mobActions, double chance) {
    this.mobActions = mobActions;
    this.chance = chance;
  }

  /**
   * Attempts to execute the action chain if the random roll succeeds.
   *
   * @param gtMobEntity Entity of the GTMob
   * @param attacker    Attacker entity
   */
  public void attemptExecution(LivingEntity gtMobEntity, Entity attacker) {
    if (Math.random() > chance) {
      return;
    }
    execute(gtMobEntity, attacker);
  }

  /**
   * Executes each action of the chain
   *
   * @param gtMobEntity Entity of the GTMob
   * @param attacker    Attacker entity
   */
  public void execute(LivingEntity gtMobEntity, Entity attacker) {
    for (MobAction mobAction : mobActions) {
      mobAction.execute(gtMobEntity, attacker);
    }
  }

  public String toString() {
    return String.join(",", mobActions.stream().map(MobAction::toString).toList()) + "-(" + chance + ")";
  }

  public List<MobAction> getMobActions() {
    return mobActions;
  }

  public double getChance() {
    return chance;
  }

}
