package me.eeshe.gtmobs.models.config;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.files.config.ConfigWrapper;
import me.eeshe.gtmobs.util.ConfigUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public enum Title {
  TEST_TITLE("test-title", "TEST", "TITLE", 20, 50, 20);

  private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(GTMobs.getInstance(), null, "titles.yml");

  private final String path;
  private final String defaultTitle;
  private final String defaultSubtitle;
  private final int defaultFadeInTicks;
  private final int defaultDurationTicks;
  private final int defaultFadeOutTicks;

  Title(String path, String defaultTitle, String defaultSubtitle, int defaultFadeInTicks, int defaultDurationTicks,
      int defaultFadeOutTicks) {
    this.path = path;
    this.defaultTitle = defaultTitle;
    this.defaultSubtitle = defaultSubtitle;
    this.defaultFadeInTicks = defaultFadeInTicks;
    this.defaultDurationTicks = defaultDurationTicks;
    this.defaultFadeOutTicks = defaultFadeOutTicks;
  }

  /**
   * Setups the configurable Title enum.
   */
  public static void setup() {
    writeDefaults();
  }

  private static void writeDefaults() {
    FileConfiguration config = CONFIG_WRAPPER.getConfig();
    for (Title libTitle : Title.values()) {
      String path = libTitle.getPath();
      if (config.contains(path))
        continue;

      config.addDefault(path + ".title", libTitle.getDefaultTitle());
      config.addDefault(path + ".subtitle", libTitle.getDefaultSubtitle());
      config.addDefault(path + ".fade-in", libTitle.getDefaultFadeInTicks());
      config.addDefault(path + ".duration", libTitle.getDefaultDurationTicks());
      config.addDefault(path + ".fade-out", libTitle.getDefaultFadeOutTicks());
    }
    config.options().copyDefaults(true);

    CONFIG_WRAPPER.saveConfig();
    CONFIG_WRAPPER.reloadConfig();
  }

  public static ConfigWrapper getConfigWrapper() {
    return CONFIG_WRAPPER;
  }

  public void sendGlobal() {
    sendGlobal(null, new HashMap<>());
  }

  public void sendGlobal(Map<String, String> placeholders) {
    sendGlobal(null, placeholders);
  }

  public void sendGlobal(Sound sound, Map<String, String> placeholders) {
    for (Player online : Bukkit.getOnlinePlayers()) {
      if (placeholders == null) {
        send(online);
      } else {
        send(online, placeholders);
      }
      if (sound == null)
        continue;

      sound.play(online);
    }
  }

  public void send(Player player, Sound sound) {
    send(player, sound, new HashMap<>());
  }

  public void send(Player player, Sound sound, Map<String, String> placeholders) {
    fetchConfigTitle().send(player, placeholders);
    sound.play(player);
  }

  public void send(Player player) {
    send(player, new HashMap<>());
  }

  public void send(Player player, Map<String, String> placeholders) {
    fetchConfigTitle().send(player, placeholders);
  }

  private ConfigTitle fetchConfigTitle() {
    ConfigTitle configTitle = ConfigUtil.fetchConfigTitle(CONFIG_WRAPPER.getConfig(), path);
    if (configTitle == null)
      return createDefaultConfigTitle();

    return configTitle;
  }

  private ConfigTitle createDefaultConfigTitle() {
    return new ConfigTitle(defaultTitle, defaultSubtitle, defaultFadeInTicks, defaultDurationTicks,
        defaultFadeOutTicks);
  }

  public String getPath() {
    return path;
  }

  public String getDefaultTitle() {
    return defaultTitle;
  }

  public String getDefaultSubtitle() {
    return defaultSubtitle;
  }

  public long getDefaultFadeInTicks() {
    return defaultFadeInTicks;
  }

  public long getDefaultDurationTicks() {
    return defaultDurationTicks;
  }

  public long getDefaultFadeOutTicks() {
    return defaultFadeOutTicks;
  }
}
