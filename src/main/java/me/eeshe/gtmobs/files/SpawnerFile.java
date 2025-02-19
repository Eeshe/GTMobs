package me.eeshe.gtmobs.files;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import me.eeshe.gtmobs.models.Spawner;

public class SpawnerFile {
  private File dataFile;
  private FileConfiguration dataYML;

  public SpawnerFile(Plugin plugin, Spawner spawner) {
    createDataFile(plugin, spawner.getId());
  }

  public SpawnerFile(File dataFile) {
    this.dataFile = dataFile;
    this.dataYML = YamlConfiguration.loadConfiguration(dataFile);
  }

  private void createDataFile(Plugin plugin, String id) {
    this.dataFile = new File(plugin.getDataFolder() + "/spawners/" + id + ".yml");
    if (!dataFile.exists()) {
      if (!dataFile.getParentFile().exists()) {
        dataFile.getParentFile().mkdirs();
      }
      try {
        dataFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    this.dataYML = YamlConfiguration.loadConfiguration(dataFile);
  }

  public FileConfiguration getData() {
    return dataYML;
  }

  public void saveData() {
    try {
      dataYML.save(dataFile);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void reloadData() {
    dataYML.setDefaults(YamlConfiguration.loadConfiguration(dataFile));
  }

  public void deleteData() {
    dataFile.delete();
  }
}
