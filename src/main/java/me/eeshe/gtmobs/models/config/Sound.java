package me.eeshe.gtmobs.models.config;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.files.config.ConfigWrapper;
import me.eeshe.gtmobs.util.ConfigUtil;

public enum Sound {
  SUCCESS("success", true, org.bukkit.Sound.BLOCK_NOTE_PLING, 0.6F, 1.6F),
  ERROR("error", true, org.bukkit.Sound.BLOCK_NOTE_BASS, 0.6F, 0.6F),
  BACK("back", true, org.bukkit.Sound.UI_BUTTON_CLICK, 0.6F, 1.1F),
  PREVIOUS_PAGE("previous-page", true, org.bukkit.Sound.UI_BUTTON_CLICK, 0.6F, 1.1F),
  NEXT_PAGE("next-page", true, org.bukkit.Sound.UI_BUTTON_CLICK, 0.6F, 1.1F),
  INPUT_REQUEST("input-request", true, org.bukkit.Sound.BLOCK_NOTE_XYLOPHONE, 0.6F, 1.2F),
  TELEPORT("teleport", true, org.bukkit.Sound.ENTITY_ENDERMEN_TELEPORT, 0.6F, 1.2F),
  MOB_HIT_SOUND("mob-hit-sound", true, org.bukkit.Sound.ENTITY_PLAYER_HURT, 1F, 1F);

  private static final ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(GTMobs.getInstance(), null, "sounds.yml");
  private final String path;
  private final boolean defaultEnabled;
  private final org.bukkit.Sound defaultSound;
  private final float defaultVolume;
  private final float defaultPitch;

  Sound(String path, boolean defaultEnabled, org.bukkit.Sound defaultSound, float defaultVolume, float defaultPitch) {
    this.path = path;
    this.defaultEnabled = defaultEnabled;
    this.defaultSound = defaultSound;
    this.defaultVolume = defaultVolume;
    this.defaultPitch = defaultPitch;
  }

  /**
   * Setups the configurable Sound enum.
   */
  public static void setup() {
    writeDefaults();
  }

  private static void writeDefaults() {
    FileConfiguration config = CONFIG_WRAPPER.getConfig();
    for (Sound sound : Sound.values()) {
      ConfigUtil.writeConfigSound(config, sound.getPath(), sound.createDefaultConfigSound());
    }
    config.options().copyDefaults(true);

    CONFIG_WRAPPER.saveConfig();
    CONFIG_WRAPPER.reloadConfig();
  }

  public static ConfigWrapper getConfigWrapper() {
    return CONFIG_WRAPPER;
  }

  /**
   * Plays the sound to the passed CommandSender.
   *
   * @param sender CommandSender the sound will be played for.
   */
  public void play(CommandSender sender) {
    if (!(sender instanceof Player))
      return;

    ConfigSound configSound = fetchConfigSound();
    Player player = (Player) sender;
    player.playSound(player.getLocation(), configSound.getSound(), configSound.getVolume(), configSound.getPitch());
  }

  /**
   * Plays teh sound in the passed location.
   *
   * @param location Location the sound will be played in.
   */
  public void play(Location location) {
    ConfigSound configSound = fetchConfigSound();
    if (location.getWorld() == null)
      return;

    location.getWorld().playSound(location, configSound.getSound(), configSound.getVolume(), configSound.getPitch());
  }

  /**
   * Fetches the ConfigSound
   *
   * @return ConfigSound.
   */
  private ConfigSound fetchConfigSound() {
    ConfigSound configParticle = ConfigUtil.fetchConfigSound(CONFIG_WRAPPER.getConfig(), path);
    if (configParticle == null)
      return createDefaultConfigSound();

    return configParticle;
  }

  /**
   * Creates a ConfigSound instance with the default values of the Sound.
   *
   * @return ConfigSound instance with the default values of the Sound.
   */
  private ConfigSound createDefaultConfigSound() {
    return new ConfigSound(defaultSound, defaultEnabled, defaultVolume, defaultPitch);
  }

  public String getPath() {
    return path;
  }

  public boolean getDefaultEnabled() {
    return defaultEnabled;
  }

  public org.bukkit.Sound getDefaultSound() {
    return defaultSound;
  }

  public float getDefaultVolume() {
    return defaultVolume;
  }

  public float getDefaultPitch() {
    return defaultPitch;
  }
}
