package me.eeshe.gtmobs.models.config;

import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class ConfigKnockback {

  private final double strength;
  private final boolean airborne;

  public ConfigKnockback(double strength, boolean airborne) {
    this.strength = strength;
    this.airborne = airborne;
  }

  /**
   * Applies the ConfigKnockback to the passed Entity.
   *
   * @param entity    Entity to apply the knockback to.
   * @param direction Direction the knockback should be applied in.
   */
  public void apply(Entity entity, Vector direction) {
    Vector knockbackVector = direction.normalize();
    if (airborne) {
      knockbackVector.setY(1);
    }
    knockbackVector.multiply(strength);

    entity.setVelocity(knockbackVector);
  }

  public double getStrength() {
    return strength;
  }

  public boolean isAirborne() {
    return airborne;
  }
}
