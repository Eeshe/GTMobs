package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public record ConfigSound(org.bukkit.Sound sound, boolean enabled, float volume, float pitch) {

  public void play(Player player) {
    player.playSound(player.getLocation(), sound, volume, pitch);
  }

  public void play(Location location) {
    location.getWorld().playSound(location, sound, volume, pitch);
  }

  public void play(Player player, Location location) {
    player.playSound(location, sound, volume, pitch);
  }
}
