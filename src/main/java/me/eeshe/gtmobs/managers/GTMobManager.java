package me.eeshe.gtmobs.managers;

import java.util.HashMap;
import java.util.Map;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;

public class GTMobManager extends DataManager {
  private final Map<String, GTMob> gtMobs = new HashMap<>();

  public GTMobManager(GTMobs plugin) {
    super(plugin);
  }

  @Override
  public void load() {
    for (GTMob gtMob : getPlugin().getMobConfig().fetchGTMobs()) {
      gtMob.register();
    }
  }

  @Override
  public void unload() {
    gtMobs.clear();
  }

  public Map<String, GTMob> getGTMobs() {
    return gtMobs;
  }

}
