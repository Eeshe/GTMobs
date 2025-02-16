package me.eeshe.gtmobs.commands;

import me.eeshe.gtmobs.GTMobs;

import java.util.List;

public class GTMobsCommand extends PluginCommand {

  public GTMobsCommand(GTMobs plugin) {
    super(plugin);

    setName("gtmobs");
    setPermission("gtmobs.base");
    setSubcommands(List.of(
        new CommandSpawn(plugin, this),
        new CommandKillAll(plugin, this),
        new CommandReload(plugin, this),
        new CommandHelp(plugin, this)));
  }
}
