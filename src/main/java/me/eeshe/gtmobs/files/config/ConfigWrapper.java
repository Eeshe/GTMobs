package me.eeshe.gtmobs.files.config;

import me.eeshe.gtmobs.util.LogUtil;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ConfigWrapper {
  private final Plugin plugin;
  private final String folderName;
  private final String fileName;
  private FileConfiguration config;
  private File configFile;
  private boolean hasSyntaxError;

  public ConfigWrapper(Plugin plugin, String folderName, String fileName) {
    this.plugin = plugin;
    this.folderName = folderName;
    this.fileName = fileName;
  }

  public void createFile() {
    reloadConfig();
    saveConfig();
    loadConfig();
  }

  public FileConfiguration getConfig() {
    if (this.config == null)
      reloadConfig();
    if (hasSyntaxError) {
      LogUtil.sendWarnLog("There is a syntax error with the configuration file '" + fileName + "'.");
      return null;
    }
    return this.config;
  }

  public void loadConfig() {
    this.config.options().copyDefaults(true);
    saveConfig();
  }

  public void reloadConfig() {
    if (this.configFile == null)
      if (this.folderName != null && !this.folderName.isEmpty()) {
        this.configFile = new File(this.plugin.getDataFolder() + File.separator + this.folderName, this.fileName);
      } else {
        this.configFile = new File(this.plugin.getDataFolder(), this.fileName);
      }
    try {
      Yaml yaml = new Yaml();
      yaml.load(new FileInputStream(configFile));
    } catch (Exception e) {
      if (!(e instanceof FileNotFoundException)) {
        hasSyntaxError = true;
        e.printStackTrace();
      }
    }
    this.config = YamlConfiguration.loadConfiguration(this.configFile);
  }

  public void saveConfig() {
    if (this.config == null || this.configFile == null)
      return;
    try {
      getConfig().save(this.configFile);
    } catch (IOException iOException) {
      LogUtil.sendWarnLog("Could not save config to" + configFile);
    }
  }

  public void writeDefaults() {

  }
}
