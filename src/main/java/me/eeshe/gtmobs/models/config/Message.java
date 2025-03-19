package me.eeshe.gtmobs.models.config;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.files.config.ConfigWrapper;
import me.eeshe.gtmobs.util.StringUtil;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public enum Message {
  UNKNOWN_COMMAND("unknown-command", "&cUnknown command. Run &l/gtmobs &chelp to see the full list of commands."),

  PLAYER_COMMAND("player-command", "Not available for consoles."),

  CONSOLE_COMMAND("console-command", "Not available for players."),

  NO_PERMISSION("no-permission", "&cYou don't have permissions to run this command."),

  PLAYER_NOT_FOUND("player-not-found", "&cUnknown player &l%player%&c."),

  NON_NUMERIC_INPUT("non-numeric-input", "&cYou must enter a numeric value."),

  AMOUNT_MUST_BE_HIGHER_THAN_ZERO("amount-must-be-higher-than-zero", "&cValue must be higher than 0."),

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
  SPAWN_COMMAND_USAGE("spawn-command-usage", "/gtmobs spawn <Mob> [Amount] [Radius] [Player]"),
  NOT_ENOUGH_ARGUMENTS("not-enough-arguments",
      "&cYou need to specify all parameters when running this command from the console."),
  NO_VALID_SPAWN_LOCATION("no-valid-spawn-location", "&cCouldn't find a valid spawn location."),
  GTMOB_NOT_FOUND("gtmob-not-found", "&cGTMob &l%id%&c not found."),
  SPAWN_ERROR("spawn-error", "&cSome mobs couldn't be spawned. Please check the difficulty or chunk mob limits."),
  SPAWN_COMMAND_SUCCESS("spawn-command-success", "&aSuccessfully spawned &l%amount% %id%&a."),

  KILL_ALL_COMMAND_INFO("killall-command-info",
      "Kills all the GTMobs. Can receive a radius parameter to limit the killing area."),
  KILL_ALL_COMMAND_USAGE("killall-command-usage", "/gtmobs killall [Radius]"),
  KILL_ALL_COMMAND_SUCCESS("killall-command-success", "&aSuccessfully killed &l%amount%&a GTMobs."),

  SPAWNER_CREATE_COMMAND_INFO("spawner-create-command-info", "Creates a spawner with the specified name"),
  SPAWNER_CREATE_COMMAND_USAGE("spawner-create-command-usage", "/gtmobs spawner create <ID>"),
  SPAWNER_ALREADY_EXISTS("spawner-already-exists", "&cAn spawner with ID &l%id%&c already exists."),
  NOT_LOOKING_AT_BLOCK("not-looking-at-block", "&cYou must be looking at a block while executing this command."),
  ALREADY_SPAWNER_BLOCK("already-spawner-block", "&cThis block already is a spawner."),
  SPAWNER_CREATE_COMMAND_SUCCESS("spawner-create-command-success", "&aSuccessfully created spawner &l%id%&a."),

  SPAWNER_INFO_COMMAND_INFO("spawner-info-command-info", "Displays information about the specified spawner."),
  SPAWNER_INFO_COMMAND_USAGE("spawner-info-command-usage", "/gtmobs spawner info <Spawner>"),
  SPAWNER_INFO_COMMAND_SUCCESS("spawner-info-command-success", "&e&l%spawner% Info\n" +
      "&eMob: &b%mob%\n" +
      "&eLocation: &b%location%\n" +
      "&eAmount: &b%minimum%-%maximum%\n" +
      "&eLimit: &b%limit%\n" +
      "&eInterval: &b%interval%\n" +
      "&eSpawn Radius: &b%spawn_radius%\n" +
      "&eTrigger Radius: &b%trigger_radius%"),

  SPAWNER_COPY_COMMAND_INFO("spawner-copy-command-info",
      "Copies the source spawner's configurations to a new spawner."),
  SPAWNER_COPY_COMMAND_USAGE("spawner-copy-command-usage", "/gtmobs spawner copy <Source> <NewSpawnerID>"),
  SPAWNER_COPY_COMMAND_SUCCESS("spawner-copy-command-success",
      "&aSuccessfully copied &l%source%'s&a configurations to new spawner &l%spawner%&a."),

  SPAWNER_DELETE_COMMAND_INFO("spawner-delete-command-info", "Deletes the specified spawner."),
  SPAWNER_DELETE_COMMAND_USAGE("spawner-delete-command-usage", "/gtmobs spawner delete"),
  SPAWNER_DELETE_COMMAND_SUCCESS("spawner-delete-command-success", "&aSuccessfully deleted &l%spawner%&a."),

  SPAWNER_LIST_COMMAND_INFO("spawner-list-command-info", "Lists all the active spawners"),
  SPAWNER_LIST_COMMAND_USAGE("spawner-list-command-usage", "/gtmobs spawner list [Near] [Radius] [Page]"),
  NO_SPAWNERS_FOUND("no-spawners-found", "&cNo spawners found."),
  PAGE_NOT_FOUND("page-not-found", "&cPage &l%page%&c not found."),
  SPAWNER_LIST_ENTRY("spawner-list-entry", "&e- &a%spawner%"),

  SPAWNER_TELEPORT_COMMAND_INFO("spawner-teleport-command-info", "Teleports you to the specified spawner."),
  SPAWNER_TELEPORT_COMMAND_USAGE("spawner-teleport-command-usage", "/gtmobs spawner teleport <Spawner>"),

  SPAWNER_SET_MOB_COMMAND_INFO("spawner-set-mob-command-info", "Sets the specified mob as the selected spawner's mob."),
  SPAWNER_SET_MOB_COMMAND_USAGE("spawner-set-mob-command-usage", "/gtmobs spawner set <Spawner> mob <Mob>"),
  SPAWNER_NOT_FOUND("spawner-not-found", "&cSpawner &l%spawner%&c not found."),
  SPAWNER_SET_MOB_COMMAND_SUCCESS("spawner-set-mob-command-success",
      "&aSuccessfully set &l%spawner%'s&a mob to &l%mob%&a."),

  SPAWNER_SET_MIN_COMMAND_INFO("spawner-set-min-command-info",
      "Sets the minimum amount of spawned mobs of the specified spawner."),
  SPAWNER_SET_MIN_COMMAND_USAGE("spawner-set-min-command-usage", "/gtmobs spawner set <Spawner> min <MinimumAmount>"),
  AMOUNT_MUST_BE_ZERO_OR_HIGHER("amount-must-be-zero-or-higher", "&cAmount must be 0 or higher."),
  MINIMUM_OVER_MAXIMUM("minimum-over-maximum", "&cThe minimum amount can't exceed the maximum amount &l(%maximum%)&c."),
  SPAWNER_SET_MIN_COMMAND_SUCCESS("spawner-set-min-command-success",
      "&aSuccessfully set &l%spawner%'s&a minimum amount to &l%minimum%&a."),

  SPAWNER_SET_MAX_COMMAND_INFO("spawner-set-max-command-info",
      "Sets the maximum amount of spawned mobs of the specified spawner."),
  SPAWNER_SET_MAX_COMMAND_USAGE("spawner-set-max-command-usage", "/gtmobs spawner set <Spawner> max <MaximumAmount>"),
  MAXIMUM_UNDER_MINIMUM("maximum-under-minimum",
      "&cThe maximum amount can't be below the minimum amount &l(%minimum%)&c."),
  SPAWNER_SET_MAX_COMMAND_SUCCESS("spawner-set-max-command-success",
      "&aSuccessfully set &l%spawner%'s&a maximum amount to &l%maximum%&a."),

  SPAWNER_SET_LIMIT_COMMAND_INFO("spawner-set-limit-command-info",
      "Sets the limit of spawned mobs of the specified spawner."),
  SPAWNER_SET_LIMIT_COMMAND_USAGE("spawner-set-limit-command-usage", "/gtmobs spawner set <Spawner> limit <Limit>"),
  SPAWNER_SET_LIMIT_COMMAND_SUCCESS("spawner-set-limit-command-success",
      "&aSuccessfully set &l%spawner%'s&a limit to &l%limit%&a."),

  SPAWNER_SET_INTERVAL_COMMAND_INFO("spawner-set-interval-command-info",
      "Sets the time interval between each mob spawn"),
  SPAWNER_SET_INTERVAL_COMMAND_USAGE("spawner-set-interval-command-usage",
      "/gtmobs spawner set <Spawner> interval <Time>"),
  SPAWNER_SET_INTERVAL_COMMAND_SUCCESS("spawner-set-interval-command-success",
      "&aSuccessfully set &l%spawner%'s&a spawn interval to &l%interval%&a."),

  SPAWNER_SET_SPAWN_RADIUS_COMMAND_INFO("spawner-set-spawn-radius-command-info",
      "Sets the spawn radius of the spawner"),
  SPAWNER_SET_SPAWN_RADIUS_COMMAND_USAGE("spawner-set-spawn-radius-command-usage",
      "/gtmobs spawner set <Spawner> spawnradius <Radius>"),
  SPAWNER_SET_SPAWN_RADIUS_COMMAND_SUCCESS("spawner-set-spawn-radius-command-success",
      "&aSuccessfully set &l%spawner%'s&a spawn radius to &l%spawn_radius%&a."),

  SPAWNER_SET_TRIGGER_RADIUS_COMMAND_INFO("spawner-set-trigger-radius-command-info",
      "Sets the trigger radius of the spawner"),
  SPAWNER_SET_TRIGGER_RADIUS_COMMAND_USAGE("spawner-set-trigger-radius-command-usage",
      "/gtmobs spawner set <Spawner> triggerradius <Radius>"),
  SPAWNER_SET_TRIGGER_RADIUS_COMMAND_SUCCESS("spawner-set-trigger-radius-command-success",
      "&aSuccessfully set &l%spawner%'s&a trigger radius to &l%trigger_radius%&a."),
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
