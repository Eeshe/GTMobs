package me.eeshe.gtmobs.commands;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.Message;
import org.bukkit.command.CommandSender;

public class CommandReload extends PluginCommand {

  public CommandReload(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("reload");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.RELOAD_COMMAND_INFO);
    setUsageMessage(Message.RELOAD_COMMAND_USAGE);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    GTMobs.getInstance().reload();
    Message.RELOAD_COMMAND_SUCCESS.sendSuccess(sender);
  }
}
