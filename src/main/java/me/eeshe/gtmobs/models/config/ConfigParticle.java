package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public record ConfigParticle(boolean enabled, Particle particle, int amount, double xOffSet, double yOffSet,
    double zOffSet, double extra, Object data) {

  /**
   * Spawns the ConfigParticle at the passed location.
   *
   * @param location Location the particle will be spawned in.
   */
  public void spawn(Location location) {
    if (!enabled)
      return;
    World world = location.getWorld();
    if (world == null)
      return;

    location.getWorld().spawnParticle(particle, location, amount, xOffSet, yOffSet, zOffSet, extra, data);
  }

  /**
   * Spawns the Particle only for the passed player.
   *
   * @param player   Player to spawn the particle to.
   * @param location Location to spawn the particle in.
   */
  public void spawn(Player player, Location location) {
    if (!enabled)
      return;
    World world = location.getWorld();
    if (world == null)
      return;

    player.spawnParticle(particle, location, amount, xOffSet, yOffSet, zOffSet, extra, data);
  }
}
