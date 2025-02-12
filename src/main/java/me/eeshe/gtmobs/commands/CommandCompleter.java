package me.eeshe.gtmobs.commands;

import me.eeshe.gtmobs.GTMobs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class handles the tab completion system, adding commands to the tab
 * completer only if the player has
 * the required permissions.
 */
public class CommandCompleter implements TabCompleter {
  private final GTMobs plugin;

  public CommandCompleter(GTMobs plugin) {
    this.plugin = plugin;
  }

  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    PluginCommand command = plugin.getCommands().get(cmd.getName());
    if (command == null)
      return new ArrayList<>();
    if (!command.checkPermission(sender))
      return new ArrayList<>();

    if (!command.getSubcommands().isEmpty() && args.length > 1) {
      command = command.getSubcommand(args[0]);
      args = Arrays.copyOfRange(args, 1, args.length);
    }
    if (command == null)
      return new ArrayList<>();
    List<String> completions = command.getCommandCompletions(sender, args);
    if (completions == null)
      return null;

    return getCompletion(completions, args);
  }

  /**
   * Searches for matches between the passed arguments and the passed String[]
   * based on the specified index.
   *
   * @param arguments Possible arguments the player can type into the tab
   *                  completion.
   * @param args      Arguments the player is currently using in the command
   *                  completion.
   * @return ArrayList with all the matching completions.
   */
  private List<String> getCompletion(List<String> arguments, String[] args) {
    if (arguments == null)
      return null;

    List<String> results = new ArrayList<>();
    for (String argument : arguments) {
      if (!argument.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
        continue;

      results.add(argument);
    }
    return results;
  }
}
