package me.eeshe.gtmobs.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.ActiveMob;

public class ActiveMobManager extends DataManager {
  private final Map<UUID, ActiveMob> activeMobs = new HashMap<>();

  public ActiveMobManager(GTMobs plugin) {
    super(plugin);
  }

  @Override
  public void load() {

  }

  @Override
  public void unload() {

  }

  public Map<UUID, ActiveMob> getActiveMobs() {
    return activeMobs;
  }

}
