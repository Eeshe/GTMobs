package me.eeshe.gtmobs.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.files.SpawnerFile;
import me.eeshe.gtmobs.models.Spawner;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.util.ConfigUtil;
import me.eeshe.gtmobs.util.YAMLUtil;

public class SpawnerManager extends DataManager {
  private final Map<String, Spawner> spawners = new HashMap<>();

  public SpawnerManager(GTMobs plugin) {
    super(plugin);
  }

  @Override
  public void load() {
    loadStoredSpawners();
  }

  /**
   * Loads all the stored spawners
   */
  private void loadStoredSpawners() {
    File spawnersDirectory = new File(getPlugin().getDataFolder() + "/spawners");
    if (!spawnersDirectory.exists()) {
      return;
    }
    File[] spawnerDataFiles = spawnersDirectory.listFiles();
    if (spawnerDataFiles == null) {
      return;
    }
    for (File spawnerDataFile : spawnerDataFiles) {
      Spawner spawner = fetch(spawnerDataFile);
      if (spawner == null) {
        continue;
      }
      spawner.load();
    }
  }

  /**
   * Fetches the Spawner stored in the passed data file
   *
   * @param spawnerDataFile Data file to fetch the Spawner from
   * @return Fetched Spawner
   */
  private Spawner fetch(File spawnerDataFile) {
    SpawnerFile spawnerFile = new SpawnerFile(spawnerDataFile);
    FileConfiguration spawnerData = spawnerFile.getData();
    if (spawnerData.getKeys(true).isEmpty()) {
      return null;
    }
    String id = spawnerData.getString("id");
    Location location = YAMLUtil.fetchLocationFromYaml(spawnerData, "location");
    String mobId = spawnerData.getString("mob-id");
    IntRange amount = ConfigUtil.fetchIntRange(spawnerData, "amount");
    long frequencyTicks = spawnerData.getLong("frequency-ticks");
    double spawnRadius = spawnerData.getDouble("spawn-radius");
    double triggerRadius = spawnerData.getDouble("trigger-radius");
    int limit = spawnerData.getInt("limit");

    return new Spawner(id, location, mobId, amount, frequencyTicks, spawnRadius, triggerRadius, limit);
  }

  @Override
  public void unload() {
    for (Spawner spawner : new ArrayList<>(spawners.values())) {
      spawner.unload();
    }
  }

  /**
   * Saves the passed Spawner to its corresponding .yml file
   *
   * @param spawner Spawner to save
   */
  public void save(Spawner spawner) {
    SpawnerFile spawnerFile = new SpawnerFile(getPlugin(), spawner);
    FileConfiguration spawnerData = spawnerFile.getData();

    spawnerData.set("id", spawner.getId());
    YAMLUtil.writeLocationToYaml(spawner.getLocation(), spawnerData, "location");
    spawnerData.set("mob-id", spawner.getMobId());
    ConfigUtil.writeIntRange(spawnerData, "amount", spawner.getAmount());
    spawnerData.set("frequency-ticks", spawner.getFrequencyTicks());
    spawnerData.set("spawn-radius", spawner.getSpawnRadius());
    spawnerData.set("trigger-radius", spawner.getTriggerRadius());
    spawnerData.set("limit", spawner.getLimit());

    spawnerFile.saveData();
  }

  /**
   * Deletes the data of the passed Spawner
   *
   * @param spawner Spawner to delete
   */
  public void delete(Spawner spawner) {
    new SpawnerFile(getPlugin(), spawner).deleteData();
  }

  public Map<String, Spawner> getSpawners() {
    return spawners;
  }
}
