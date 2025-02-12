package me.eeshe.gtmobs.commands;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.util.Messager;
import org.bukkit.command.CommandSender;

public class CommandHelp extends PluginCommand {

  public CommandHelp(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("help");
    setPermission("gtmobs.player");
    setInfoMessage(Message.HELP_COMMAND_INFO);
    setUsageMessage(Message.HELP_COMMAND_USAGE);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    Messager.sendHelpMessage(sender, getParentCommand().getSubcommands().values());
  }
}
