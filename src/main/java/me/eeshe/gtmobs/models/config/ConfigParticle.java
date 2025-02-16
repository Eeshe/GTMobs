package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ConfigParticle {

  private final boolean enabled;
  private final Particle particle;
  private final int amount;
  private final double xOffSet;
  private final double yOffSet;
  private final double zOffSet;
  private final double extra;
  private final Object data;

  public ConfigParticle(boolean enabled, Particle particle, int amount, double xOffSet, double yOffSet, double zOffSet,
      double extra, Object data) {
    this.enabled = enabled;
    this.particle = particle;
    this.amount = amount;
    this.xOffSet = xOffSet;
    this.yOffSet = yOffSet;
    this.zOffSet = zOffSet;
    this.extra = extra;
    this.data = data;
  }

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

  public boolean isEnabled() {
    return enabled;
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

  public double getExtra() {
    return extra;
  }

  public Object getData() {
    return data;
  }
}
