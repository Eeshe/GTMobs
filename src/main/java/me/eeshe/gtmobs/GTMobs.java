package me.eeshe.gtmobs;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.eeshe.gtmobs.commands.CommandCompleter;
import me.eeshe.gtmobs.commands.CommandPackets;
import me.eeshe.gtmobs.commands.CommandRunner;
import me.eeshe.gtmobs.commands.GTMobsCommand;
import me.eeshe.gtmobs.commands.PluginCommand;
import me.eeshe.gtmobs.files.config.ConfigWrapper;
import me.eeshe.gtmobs.files.config.ItemConfig;
import me.eeshe.gtmobs.files.config.MainConfig;
import me.eeshe.gtmobs.files.config.MobConfig;
import me.eeshe.gtmobs.listeners.DisguiseHandler;
import me.eeshe.gtmobs.listeners.GTMobHandler;
import me.eeshe.gtmobs.managers.ActiveMobManager;
import me.eeshe.gtmobs.managers.DataManager;
import me.eeshe.gtmobs.managers.GTMobManager;
import me.eeshe.gtmobs.managers.SpawnerManager;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.models.config.Particle;
import me.eeshe.gtmobs.models.config.Sound;

public class GTMobs extends JavaPlugin {

  /**
   * HashMap that links all the subcommand Strings with their respective
   * subcommand instance.
   */
  // CommandName, SubcommandInstance
  private final Map<String, PluginCommand> commands = new LinkedHashMap<>();
  private final List<ConfigWrapper> configFiles = new ArrayList<>();
  private final List<DataManager> dataManagers = new ArrayList<>();

  private MainConfig mainConfig;
  private ItemConfig itemConfig;
  private MobConfig mobConfig;

  private GTMobManager gtMobManager;
  private ActiveMobManager activeMobManager;
  private SpawnerManager spawnerManager;

  private CommandExecutor commandExecutor;
  private CommandCompleter commandCompleter;

  /**
   * Creates and returns a static instance of the Plugin's main class.
   *
   * @return Instance of the main class of the plugin.
   */
  public static GTMobs getInstance() {
    return GTMobs.getPlugin(GTMobs.class);
  }

  @Override
  public void onEnable() {
    setupFiles();
    registerManagers();
    registerCommands();
    registerListeners();
    for (DataManager dataManager : dataManagers) {
      dataManager.load();
    }
  }

  /**
   * Creates and configures all the config files of the plugin.
   */
  public void setupFiles() {
    this.mainConfig = new MainConfig(this);
    this.itemConfig = new ItemConfig(this);
    this.mobConfig = new MobConfig(this);
    Message.setup();
    Sound.setup();
    Particle.setup();
    configFiles.addAll(List.of(
        mainConfig,
        itemConfig,
        mobConfig,
        Message.getConfigWrapper(),
        Sound.getConfigWrapper(),
        Particle.getConfigWrapper()));
    for (ConfigWrapper configFile : configFiles) {
      configFile.writeDefaults();
    }
  }

  /**
   * Registers all the needed managers in order for the plugin to work.
   */
  private void registerManagers() {
    this.gtMobManager = new GTMobManager(this);
    this.activeMobManager = new ActiveMobManager(this);
    this.spawnerManager = new SpawnerManager(this);
    dataManagers.addAll(List.of(
        gtMobManager,
        activeMobManager,
        spawnerManager));
  }

  /**
   * Registers the commands, runners and tab completers.
   */
  private void registerCommands() {
    this.commandExecutor = new CommandRunner(this);
    this.commandCompleter = new CommandCompleter(this);
    registerCommands(List.of(
        new GTMobsCommand(this),
        new CommandPackets(this)));
  }

  /**
   * Registers the passed list of LibCommands.
   *
   * @param pluginCommands LibCommands to register.
   */
  public void registerCommands(List<PluginCommand> pluginCommands) {
    for (PluginCommand penPluginCommand : pluginCommands) {
      org.bukkit.command.PluginCommand pluginCommand = penPluginCommand.getPlugin().getServer()
          .getPluginCommand(penPluginCommand.getName());
      if (pluginCommand == null)
        continue;

      this.commands.put(penPluginCommand.getName(), penPluginCommand);
      pluginCommand.setExecutor(commandExecutor);
      pluginCommand.setTabCompleter(commandCompleter);
    }
  }

  /**
   * Registers all the event listeners the plugin might need.
   */
  private void registerListeners() {
    PluginManager pluginManager = Bukkit.getPluginManager();

    pluginManager.registerEvents(new GTMobHandler(this), this);
    pluginManager.registerEvents(new DisguiseHandler(this), this);
  }

  @Override
  public void onDisable() {
    commands.clear();
    for (DataManager dataManager : dataManagers) {
      dataManager.unload();
    }
  }

  /**
   * Reloads the config files and the data managers of the plugin.
   */
  public void reload() {
    for (ConfigWrapper configFile : configFiles) {
      configFile.reloadConfig();
    }
    setupFiles();
    for (DataManager dataManager : dataManagers) {
      dataManager.reload();
    }
  }

  public Map<String, PluginCommand> getCommands() {
    return commands;
  }

  public MainConfig getMainConfig() {
    return mainConfig;
  }

  public ItemConfig getItemConfig() {
    return itemConfig;
  }

  public MobConfig getMobConfig() {
    return mobConfig;
  }

  public GTMobManager getGTMobManager() {
    return gtMobManager;
  }

  public ActiveMobManager getActiveMobManager() {
    return activeMobManager;
  }

  public SpawnerManager getSpawnerManager() {
    return spawnerManager;
  }
}
