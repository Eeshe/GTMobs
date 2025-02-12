package me.eeshe.gtmobs.util;

import me.eeshe.gtmobs.commands.PluginCommand;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.models.config.Sound;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This utility class eases the sending of messages to players while adding SFX
 * feedback to them.
 */
public class Messager {

  /**
   * Sends the passed message to the passed CommandSender, formatting all color
   * codes in the process.
   *
   * @param messageReceiver CommandSender that will receive the message.
   * @param message         Message that will be sent to the passed command
   *                        sender.
   */
  public static void sendMessage(CommandSender messageReceiver, String message) {
    messageReceiver.sendMessage(StringUtil.formatColor(message));
  }

  /**
   * Sends a message with the help text for the passed list of commands.
   *
   * @param messageReceiver CommandSender that will receive the message.
   */
  public static void sendHelpMessage(CommandSender messageReceiver, Collection<? extends PluginCommand> commandPool) {
    final StringBuilder finalMessage = new StringBuilder(Message.HELP_TEXT_HEADER.getValue() + "\n");
    final String commandFormat = Message.HELP_TEXT_COMMAND_FORMAT.getValue();
    List<? extends PluginCommand> commands = commandPool.stream()
        .filter(command -> command.checkPermission(messageReceiver)
            && command.getInfoMessage() != null && command.getUsageMessage() != null)
        .toList();
    if (commands.isEmpty()) {
      Message.NO_AVAILABLE_COMMANDS.sendError(messageReceiver);
      return;
    }
    for (int index = 0; index < commands.size(); index++) {
      PluginCommand penCommand = commands.get(index);
      if (penCommand.getInfoMessage() == null || penCommand.getUsageMessage() == null)
        continue;

      String commandText = commandFormat.replace("%usage%", penCommand.getUsageMessage().getValue());
      commandText = commandText.replace("%info%", penCommand.getInfoMessage().getValue());
      finalMessage.append(commandText);
      if (index == commands.size() - 1)
        continue;

      finalMessage.append("\n");
    }
    Messager.sendMessage(messageReceiver, finalMessage.toString());
    Sound.SUCCESS.play(messageReceiver);
  }

  /**
   * Sends an error message to the passed CommandSender indicating that it does
   * not have the required permissions for
   * some action.
   *
   * @param messageReceiver CommandSender that will receive the message.
   */
  public static void sendNoPermissionMessage(CommandSender messageReceiver) {
    Message.NO_PERMISSION.sendError(messageReceiver);
  }

  /**
   * Sends an error message to the passed CommandSender indicating that the passed
   * player name couldn't be found.
   *
   * @param messageReceiver CommandSender that will receive the message.
   * @param playerName      Name of the player that couldn't be found.
   */
  public static void sendPlayerNotFoundMessage(CommandSender messageReceiver, String playerName) {
    Map<String, String> placeholders = new HashMap<>();
    placeholders.put("%player%", playerName);
    Message.PLAYER_NOT_FOUND.sendError(messageReceiver, placeholders);
  }

  /**
   * Sends an error action bar message to the passed player indicating the passed
   * cooldown.
   *
   * @param player         Player the message will be sent to.
   * @param cooldownMillis Cooldown in milliseconds that will be displayed.
   * @param actionBar      Whether the message will be sent in the action bar.
   */
  public static void sendCooldownMessage(Player player, long cooldownMillis, boolean actionBar) {
    if (!actionBar) {
      Message.ON_COOLDOWN.sendError(player, Map.of("%cooldown%", StringUtil.formatMillis(cooldownMillis)));
    } else {
      sendActionBarMessage(player, Message.ON_COOLDOWN.getFormattedValue(
          Map.of("%cooldown%", StringUtil.formatMillis(cooldownMillis))));
      Sound.ERROR.play(player);
    }
  }

  /**
   * Sends the passed message to the passed Player as an Action Bar message. It
   * also formats all color codes in the process.
   *
   * @param messageReceiver Player that will receive the action bar message.
   * @param message         Message that will be sent to the player.
   */
  public static void sendActionBarMessage(Player messageReceiver, String message) {
    TextComponent barMessage = new TextComponent(StringUtil.formatColor(message));
    messageReceiver.spigot().sendMessage(ChatMessageType.ACTION_BAR, barMessage);
  }

  /**
   * Sends the passed title to the passed Player with the specified fade in,
   * duration and fade out.
   *
   * @param messageReceiver Player that will receive the title.
   * @param title           Title that will be sent to the player.
   * @param subtitle        Subtitle that will be sent to the player.
   * @param fadeInTicks     Fade in time of the title in seconds.
   * @param durationTicks   Duration of the title in seconds.
   * @param fadeOutTicks    Fade out time of the title in seconds.
   */
  public static void sendTitle(Player messageReceiver, String title, String subtitle, int fadeInTicks,
      int durationTicks, int fadeOutTicks) {
    if (title == null)
      return;

    title = StringUtil.formatColor(title);
    if (subtitle != null) {
      subtitle = StringUtil.formatColor(subtitle);
    }
    messageReceiver.sendTitle(title, subtitle, fadeInTicks, durationTicks, fadeOutTicks);
  }

  /**
   * Sends the passed message to all the players in the server.
   *
   * @param message Message that will be sent.
   */
  public static void sendGlobalMessage(String message) {
    for (Player online : Bukkit.getOnlinePlayers()) {
      sendMessage(online, message);
    }
  }

  /**
   * Sends the passed message to all the players in the server in the form of an
   * action bar message.
   *
   * @param message Message that will be sent.
   */
  public static void sendGlobalActionbarMessage(String message) {
    for (Player online : Bukkit.getOnlinePlayers()) {
      sendActionBarMessage(online, message);
    }
  }
}
