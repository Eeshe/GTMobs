package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class ConfigExplosion {
  private final float power;
  private final boolean setFire;
  private final boolean breakBlocks;

  public ConfigExplosion(float power, boolean setFire, boolean breakBlocks) {
    this.power = power;
    this.setFire = setFire;
    this.breakBlocks = breakBlocks;
  }

  public void create(Location location) {
    location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), power, setFire, breakBlocks);
  }

  public float getPower() {
    return power;
  }

  public boolean shouldSetFire() {
    return setFire;
  }

  public boolean shouldBreakBlocks() {
    return breakBlocks;
  }
}
