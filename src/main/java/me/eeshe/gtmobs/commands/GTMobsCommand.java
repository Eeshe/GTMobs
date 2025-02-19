package me.eeshe.gtmobs.commands;

import java.util.List;

import me.eeshe.gtmobs.GTMobs;

public class GTMobsCommand extends PluginCommand {

  public GTMobsCommand(GTMobs plugin) {
    super(plugin);

    setName("gtmobs");
    setPermission("gtmobs.base");
    setSubcommands(List.of(
        new CommandSpawn(plugin, this),
        new CommandSpawner(plugin, this),
        new CommandKillAll(plugin, this),
        new CommandReload(plugin, this),
        new CommandHelp(plugin, this)));
  }
}
