package me.eeshe.gtmobs.models.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;

public class ConfigSound {
  private final Sound sound;
  private final boolean enabled;
  private final float volume;
  private final float pitch;
  private final long delayTicks;

  public ConfigSound(Sound sound, boolean enabled, float volume, float pitch) {
    this.sound = sound;
    this.enabled = enabled;
    this.volume = volume;
    this.pitch = pitch;
    this.delayTicks = 0;
  }

  public ConfigSound(Sound sound, float volume, float pitch, long delayTicks) {
    this.enabled = true;
    this.sound = sound;
    this.volume = volume;
    this.pitch = pitch;
    this.delayTicks = delayTicks;
  }

  public void play(Player player) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(),
        () -> player.playSound(player.getLocation(), sound, volume, pitch), delayTicks);
    ;
  }

  public void play(Location location) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(),
        () -> location.getWorld().playSound(location, sound, volume, pitch), delayTicks);
  }

  public void play(Player player, Location location) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(),
        () -> player.playSound(location, sound, volume, pitch), delayTicks);
  }

  public String toString() {
    return sound.name() + "-" + volume + "-" + pitch;
  }

  public Sound getSound() {
    return sound;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public float getVolume() {
    return volume;
  }

  public float getPitch() {
    return pitch;
  }

  public long getDelayTicks() {
    return delayTicks;
  }
}
