package me.eeshe.gtmobs.commands;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.util.CommandUtil;
import me.eeshe.gtmobs.util.Messager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Map;

/**
 * This class controls everything regarding command execution in the plugin. It
 * checks the command and arguments
 * every time the player runs a command registered by the plugin.
 */
public class CommandRunner implements CommandExecutor {
  private final GTMobs plugin;

  public CommandRunner(GTMobs plugin) {
    this.plugin = plugin;
  }

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    String commandName = cmd.getName();
    PluginCommand penCommand = plugin.getCommands().get(commandName);
    if (penCommand == null)
      return false;
    if (args.length < penCommand.getArgumentAmount()) {
      if (penCommand.getUsageMessage() == null) {
        Messager.sendHelpMessage(sender, penCommand.getSubcommands().values());
        return true;
      }
      Message.USAGE_TEXT.sendError(sender, Map.of("%usage%", penCommand.getUsageMessage().getValue()));
      return true;
    }
    if (!CommandUtil.hasPermission(sender, penCommand.getPermission(), true))
      return true;

    penCommand.execute(sender, args);
    return true;
  }
}
