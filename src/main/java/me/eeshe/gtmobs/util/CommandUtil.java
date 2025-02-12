package me.eeshe.gtmobs.util;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandUtil {

  /**
   * Executes the passed commands for the passed player.
   *
   * @param player   Player the commands will be run for.
   * @param commands Commands that will be run.
   */
  public static void executeCommands(OfflinePlayer player, List<String> commands) {
    for (String command : commands) {
      executeCommand(player, command);
    }
  }

  /**
   * Executes the passed command for the passed player.
   *
   * @param player  Player the commands will be run for.
   * @param command Command that will be run.
   */
  public static void executeCommand(OfflinePlayer player, String command) {
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderUtil.formatPapiPlaceholders(player, command));
  }

  /**
   * Checks if the passed player has the passed permission.
   *
   * @param commandSender Player whose permissions will be checked.
   * @param permission    Permission that will be checked.
   * @return True if the passed player has the passed permission.
   */
  public static boolean hasPermission(CommandSender commandSender, String permission) {
    return hasPermission(commandSender, permission, false);
  }

  /**
   * Checks if the passed player has the passed permission.
   *
   * @param commandSender Player whose permissions will be checked.
   * @param permission    Permission that will be checked.
   * @param sendMessage   Whether to send a no permission message.
   * @return True if the passed player has the passed permission.
   */
  public static boolean hasPermission(CommandSender commandSender, String permission, boolean sendMessage) {
    if (permission == null || permission.isEmpty())
      return true;
    if (commandSender.hasPermission(permission))
      return true;

    if (sendMessage) {
      Messager.sendNoPermissionMessage(commandSender);
    }
    return false;
  }

  /**
   * Gets the OfflinePlayer instance of the passed target name.
   *
   * @param sender     CommandSender getting the target.
   * @param targetName Name of the target.
   * @return OfflinePlayer instance of the passed target name.
   */
  public static OfflinePlayer getOfflineTarget(CommandSender sender, String targetName) {
    OfflinePlayer target = Bukkit.getOfflinePlayer(targetName);
    if (target.getName() == null) {
      Messager.sendPlayerNotFoundMessage(sender, targetName);
      return null;
    }
    return target;
  }

  /**
   * Gets the online Player instance of the passed target name.
   *
   * @param sender     CommandSender getting the target.
   * @param targetName Name of the target.
   * @return Player instance of the passed target name.
   */
  public static Player getOnlineTarget(CommandSender sender, String targetName) {
    Player target = Bukkit.getPlayerExact(targetName);
    if (target == null) {
      Messager.sendPlayerNotFoundMessage(sender, targetName);
      return null;
    }
    return target;
  }
}
