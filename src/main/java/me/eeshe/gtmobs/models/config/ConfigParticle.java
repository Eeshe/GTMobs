package me.eeshe.gtmobs.models.config;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ConfigParticle {
  private final Particle particle;
  private final int amount;
  private final double xOffSet;
  private final double yOffSet;
  private final double zOffSet;
  private final double speed;

  public ConfigParticle(Particle particle, int amount) {
    this.particle = particle;
    this.amount = amount;
    this.xOffSet = 0;
    this.yOffSet = 0;
    this.zOffSet = 0;
    this.speed = 0;
  }

  public ConfigParticle(Particle particle, int amount, double xOffSet, double yOffSet, double zOffSet, double speed) {
    this.particle = particle;
    this.amount = amount;
    this.xOffSet = xOffSet;
    this.yOffSet = yOffSet;
    this.zOffSet = zOffSet;
    this.speed = speed;
  }

  /**
   * Spawns the ConfigParticle at the passed location.
   *
   * @param location Location the particle will be spawned in.
   */
  public void spawn(Location location) {
    World world = location.getWorld();
    if (world == null)
      return;

    location.getWorld().spawnParticle(particle, location, amount, xOffSet, yOffSet, zOffSet, speed);
  }

  /**
   * Spawns the Particle only for the passed player.
   *
   * @param player   Player to spawn the particle to.
   * @param location Location to spawn the particle in.
   */
  public void spawn(Player player, Location location) {
    World world = location.getWorld();
    if (world == null)
      return;

    player.spawnParticle(particle, location, amount, xOffSet, yOffSet, zOffSet, speed);
  }

  public String toString() {
    return String.join("-", List.of(
        particle.name(),
        String.valueOf(amount),
        String.valueOf(xOffSet),
        String.valueOf(yOffSet),
        String.valueOf(zOffSet),
        String.valueOf(speed)));
  }

  public Particle getParticle() {
    return particle;
  }

  public int getAmount() {
    return amount;
  }

  public double getxOffSet() {
    return xOffSet;
  }

  public double getyOffSet() {
    return yOffSet;
  }

  public double getzOffSet() {
    return zOffSet;
  }

  public double getSpeed() {
    return speed;
  }
}
