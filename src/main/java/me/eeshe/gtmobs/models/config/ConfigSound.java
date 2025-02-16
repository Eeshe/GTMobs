package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ConfigSound {
  private final Sound sound;
  private final boolean enabled;
  private final float volume;
  private final float pitch;

  public ConfigSound(Sound sound, boolean enabled, float volume, float pitch) {
    this.sound = sound;
    this.enabled = enabled;
    this.volume = volume;
    this.pitch = pitch;
  }

  public ConfigSound(Sound sound, float volume, float pitch) {
    this.enabled = true;
    this.sound = sound;
    this.volume = volume;
    this.pitch = pitch;
  }

  public void play(Player player) {
    player.playSound(player.getLocation(), sound, volume, pitch);
  }

  public void play(Location location) {
    location.getWorld().playSound(location, sound, volume, pitch);
  }

  public void play(Player player, Location location) {
    player.playSound(location, sound, volume, pitch);
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

}
