package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ConfigParticle {
  private final Particle particle;
  private final int amount;

  public ConfigParticle(Particle particle, int amount) {
    this.particle = particle;
    this.amount = amount;
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

    location.getWorld().spawnParticle(particle, location, amount);
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

    player.spawnParticle(particle, location, amount);
  }

  public String toString() {
    return particle.name() + "-" + amount;
  }

  public Particle getParticle() {
    return particle;
  }

  public int getAmount() {
    return amount;
  }
}
