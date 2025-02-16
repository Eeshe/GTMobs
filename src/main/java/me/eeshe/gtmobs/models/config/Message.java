package me.eeshe.gtmobs.models.config;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.files.config.ConfigWrapper;
import me.eeshe.gtmobs.util.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public enum Message {
  UNKNOWN_COMMAND("unknown-command", "&cUnknown command. Run &l/gtmobs &chelp to see the full list of commands."),

  PLAYER_COMMAND("player-command", "Not available for consoles."),

  CONSOLE_COMMAND("console-command", "Not available for players."),

  NO_PERMISSION("no-permission", "&cYou don't have permissions to run this command."),

  PLAYER_NOT_FOUND("player-not-found", "&cUnknown player &l%player%&c."),

  NON_NUMERIC_INPUT("non-numeric-input", "&cYou must enter a numeric value."),

  INVALID_NUMERIC_INPUT_ZERO("invalid-numeric-input-zero", "&cValue must be higher than 0."),

  USAGE_TEXT("usage-text", "&cUsage: %usage%."),

  ON_COOLDOWN("on-cooldown", "{ERROR_COLOR}Cooldown: &l%cooldown%"),

  INVALID_TIME_FORMAT("invalid-time-format", "&cInvalid time format. Use &lAdBhCmDs."),

  HELP_COMMAND_INFO("help-command-info", "Displays this list."),

  HELP_COMMAND_USAGE("help-command-usage", "/gtmobs help"),

  HELP_TEXT_HEADER("help-text-header", "&e&lCommands"),

  HELP_TEXT_COMMAND_FORMAT("help-text-command-format", "&b%usage% &e- &9%info%"),

  NO_AVAILABLE_COMMANDS("no-available-commands", "&cYou don't have access to any commands."),

  RELOAD_COMMAND_INFO("reload-command-info", "Reloads the plugin's configuration file."),

  RELOAD_COMMAND_USAGE("reload-command-usage", "/gtmobs reload"),

  RELOAD_COMMAND_SUCCESS("reload-command-success", "&aConfiguration successfully reloaded."),

  SPAWN_COMMAND_INFO("spawn-command-info", "Spawns the specified GTMob."),
  SPAWN_COMMAND_USAGE("spawn-command-usage", "/gtmobs spawn <Mob> [Amount] [Radius]"),
  GTMOB_NOT_FOUND("gtmob-not-found", "&cGTMob &l%id%&c not found."),
  SPAWN_COMMAND_SUCCESS("spawn-command-success", "&aSuccessfully spawned &l%amount% %id%&a."),

  KILL_ALL_COMMAND_INFO("killall-command-info",
      "Kills all the GTMobs. Can receive a radius parameter to limit the killing area."),
  KILL_ALL_COMMAND_USAGE("killall-command-usage", "/gtmobs killall [Radius]"),
  KILL_ALL_COMMAND_SUCCESS("killall-command-success", "&aSuccessfully killed &l%amount%&a GTMobs."),
  ;

  private final static ConfigWrapper CONFIG_WRAPPER = new ConfigWrapper(GTMobs.getInstance(), null, "messages.yml");

  private final String path;
  private final String defaultValue;

  Message(String path, String defaultValue) {
    this.path = path;
    this.defaultValue = defaultValue;
  }

  /**
   * Setups the configurable Message enum.
   */
  public static void setup() {
    writeDefaults();
  }

  private static void writeDefaults() {
    FileConfiguration config = CONFIG_WRAPPER.getConfig();
    for (Message penMessage : Message.values()) {
      String path = penMessage.getPath();
      if (config.contains(path))
        continue;

      config.addDefault(penMessage.getPath(), penMessage.getDefaultValue());
    }
    config.options().copyDefaults(true);

    CONFIG_WRAPPER.saveConfig();
    CONFIG_WRAPPER.reloadConfig();
  }

  public static ConfigWrapper getConfigWrapper() {
    return CONFIG_WRAPPER;
  }

  /**
   * Sends the message to the passed CommandSender and plays the success sound.
   *
   * @param sender CommandSender the message will be sent to.
   */
  public void sendSuccess(CommandSender sender) {
    send(sender, Sound.SUCCESS);
  }

  /**
   * Sends the message to the passed CommandSender replacing the passed
   * placeholders and plays the success sound.
   *
   * @param sender       CommandSender the message will be sent to.
   * @param placeholders Placeholders that will be replaced in the message.
   */
  public void sendSuccess(CommandSender sender, Map<String, String> placeholders) {
    send(sender, Sound.SUCCESS, placeholders);
  }

  /**
   * Sends the message to the passed CommandSender and plays the error sound.
   *
   * @param sender CommandSender the message will be sent to.
   */
  public void sendError(CommandSender sender) {
    send(sender, Sound.ERROR);
  }

  /**
   * Sends the message to the passed CommandSender replacing the passed
   * placeholders and plays the error sound.
   *
   * @param sender       CommandSender the message will be sent to.
   * @param placeholders Placeholders that will be replaced in the message.
   */
  public void sendError(CommandSender sender, Map<String, String> placeholders) {
    send(sender, Sound.ERROR, placeholders);
  }

  /**
   * Sends the message to the passed CommandSender and plays the passed Sound.
   *
   * @param sender   CommandSender the message will be sent to.
   * @param libSound Sound that will be played.
   */
  public void send(CommandSender sender, Sound libSound) {
    send(sender, false, libSound, new HashMap<>());
  }

  /**
   * Sends the message to the passed CommandSender replacing the passed
   * placeholders.
   *
   * @param sender       CommandSender the message will be sent to.
   * @param placeholders Placeholders that will be replaced in the message.
   */
  public void send(CommandSender sender, Map<String, String> placeholders) {
    send(sender, false, null, placeholders);
  }

  /**
   * Sends the message to the passed CommandSender and plays the passed Sound
   * replacing the passed placeholders.
   *
   * @param sender       CommandSender the message will be sent to.
   * @param libSound     Sound that will be played.
   * @param placeholders Placeholders that will be replaced in the message.
   */
  public void send(CommandSender sender, Sound libSound, Map<String, String> placeholders) {
    send(sender, false, libSound, placeholders);
  }

  /**
   * Sends the message to the passed CommandSender replacing the passed
   * placeholders and plays the passed Sound.
   *
   * @param sender       CommandSender the message will be sent to.
   * @param actionBar    Whether the message will be sent in the action bar.
   * @param libSound     Sound that will be sent.
   * @param placeholders Placeholders that will be replaced.
   */
  public void send(CommandSender sender, boolean actionBar, Sound libSound, Map<String, String> placeholders) {
    String message = StringUtil.formatColor(getFormattedValue(placeholders));
    if (!actionBar) {
      sender.sendMessage(message);
    } else if (sender instanceof Player) {
      ((Player) sender).spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder(message).create());
    }
    if (libSound == null)
      return;

    libSound.play(sender);
  }

  /**
   * Returns the value formatted with the passed placeholders.
   *
   * @param placeholders Placeholders that will be formatted.
   * @return Value formatted with the passed placeholders.
   */
  public String getFormattedValue(Map<String, String> placeholders) {
    String message = getValue();
    for (Map.Entry<String, String> entry : placeholders.entrySet()) {
      message = message.replace(entry.getKey(), entry.getValue());
    }
    return message;
  }

  public String getValue() {
    return CONFIG_WRAPPER.getConfig().getString(path, defaultValue);
  }

  public String getPath() {
    return path;
  }

  public String getDefaultValue() {
    return defaultValue;
  }
}
