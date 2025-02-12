package me.eeshe.gtmobs.commands;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.util.CommandUtil;
import me.eeshe.gtmobs.util.Messager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.BiFunction;

public class PluginCommand {
  private final Map<String, PluginCommand> subcommands;
  private final Map<Integer, BiFunction<CommandSender, String[], List<String>>> completions;
  private final GTMobs plugin;
  private final PluginCommand parentPluginCommand;

  private String name;
  private String permission;
  private Message infoMessage;
  private Message usageMessage;
  private int argumentAmount;
  private boolean isConsoleCommand;
  private boolean isPlayerCommand;
  private boolean isUniversalCommand;

  public PluginCommand(GTMobs plugin) {
    this.plugin = plugin;
    this.parentPluginCommand = null;
    this.subcommands = new LinkedHashMap<>();
    this.completions = new HashMap<>();
  }

  public PluginCommand(GTMobs plugin, PluginCommand parentPluginCommand) {
    this.plugin = plugin;
    this.parentPluginCommand = parentPluginCommand;
    this.subcommands = new LinkedHashMap<>();
    this.completions = new HashMap<>();
  }

  public void execute(CommandSender sender, String[] args) {
    if (args.length < 1) {
      Messager.sendHelpMessage(sender, subcommands.values());
      return;
    }
    // Compute parent commands to then find the subcommand name
    PluginCommand parentPluginCommand = getParentCommand();
    int parentCommandAmount = 0;
    while (parentPluginCommand != null) {
      parentCommandAmount += 1;
      parentPluginCommand = parentPluginCommand.getParentCommand();
    }
    String subcommandName = args[Math.min(args.length - 1, parentCommandAmount)].toLowerCase();
    PluginCommand subcommand = subcommands.get(subcommandName);
    if (subcommand == null) {
      Messager.sendHelpMessage(sender, subcommands.values());
      return;
    }
    if (isPlayerCommand && !(sender instanceof Player)) {
      Message.PLAYER_COMMAND.sendError(sender);
      return;
    }
    if (isConsoleCommand && sender instanceof Player) {
      Message.CONSOLE_COMMAND.sendError(sender);
      return;
    }
    if (args.length - 1 < subcommand.getArgumentAmount()) {
      Message.USAGE_TEXT.sendError(sender, Map.of("%usage%", subcommand.getUsageMessage().getValue()));
      return;
    }
    if (!subcommand.checkPermission(sender, true))
      return;

    subcommand.execute(sender, Arrays.copyOfRange(args, parentCommandAmount + 1, args.length));
  }

  public List<String> getCommandCompletions(CommandSender sender, String[] args) {
    if (!checkPermission(sender))
      return new ArrayList<>();
    if (completions.isEmpty()) {
      List<String> completions = new ArrayList<>();
      for (PluginCommand pluginCommand : getSubcommands().values()) {
        if (!pluginCommand.checkPermission(sender))
          continue;

        completions.add(pluginCommand.getName());
      }
      return completions;
    }

    return completions.getOrDefault(args.length - 1, (sender1, strings) -> new ArrayList<>()).apply(sender, args);
  }

  public boolean checkPermission(CommandSender sender) {
    return CommandUtil.hasPermission(sender, permission);
  }

  public boolean checkPermission(CommandSender sender, boolean sendNotification) {
    return CommandUtil.hasPermission(sender, permission, sendNotification);
  }

  public GTMobs getPlugin() {
    return plugin;
  }

  public PluginCommand getParentCommand() {
    return parentPluginCommand;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public Message getInfoMessage() {
    return infoMessage;
  }

  public void setInfoMessage(Message infoMessage) {
    this.infoMessage = infoMessage;
  }

  public Message getUsageMessage() {
    return usageMessage;
  }

  public void setUsageMessage(Message usageMessage) {
    this.usageMessage = usageMessage;
  }

  public int getArgumentAmount() {
    return argumentAmount;
  }

  public void setArgumentAmount(int argumentAmount) {
    this.argumentAmount = argumentAmount;
  }

  public boolean isConsoleCommand() {
    return isConsoleCommand;
  }

  public void setConsoleCommand(boolean consoleCommand) {
    isConsoleCommand = consoleCommand;
  }

  public boolean isPlayerCommand() {
    return isPlayerCommand;
  }

  public void setPlayerCommand(boolean playerCommand) {
    isPlayerCommand = playerCommand;
  }

  public boolean isUniversalCommand() {
    return isUniversalCommand;
  }

  public void setUniversalCommand(boolean universalCommand) {
    isUniversalCommand = universalCommand;
  }

  public Map<String, PluginCommand> getSubcommands() {
    return subcommands;
  }

  public void setSubcommands(List<PluginCommand> pluginCommands) {
    for (PluginCommand pluginCommand : pluginCommands) {
      subcommands.put(pluginCommand.getName(), pluginCommand);
    }
  }

  public PluginCommand getSubcommand(String subcommandName) {
    return subcommands.get(subcommandName);
  }

  public void setCompletions(Map<Integer, BiFunction<CommandSender, String[], List<String>>> completions) {
    this.completions.clear();
    this.completions.putAll(completions);
  }
}
