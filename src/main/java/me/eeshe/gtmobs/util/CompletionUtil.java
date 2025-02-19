package me.eeshe.gtmobs.util;

import java.util.ArrayList;
import java.util.List;

import me.eeshe.gtmobs.GTMobs;

public class CompletionUtil {

  /**
   * Returns a list with the IDs of the loaded GTMobs
   *
   * @return List of IDs
   */
  public static List<String> getGTMobIDs() {
    return new ArrayList<>(GTMobs.getInstance().getGTMobManager().getGTMobs().keySet());
  }

  /**
   * Returns a list with the IDs of the loaded spawners
   *
   * @return List of IDs
   */
  public static List<String> getSpawnerIds() {
    return new ArrayList<>(GTMobs.getInstance().getSpawnerManager().getSpawners().keySet());
  }
}
