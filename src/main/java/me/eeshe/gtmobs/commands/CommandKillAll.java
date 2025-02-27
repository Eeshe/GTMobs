package me.eeshe.gtmobs.commands;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.util.StringUtil;

public class CommandKillAll extends PluginCommand {

  public CommandKillAll(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("killall");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.KILL_ALL_COMMAND_INFO);
    setUsageMessage(Message.KILL_ALL_COMMAND_USAGE);
    setArgumentAmount(0);
    setUniversalCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    Double radius = 0D;
    if (args.length > 0 && sender instanceof Player) {
      radius = StringUtil.parseDouble(sender, args[0]);
      if (radius == null) {
        return;
      }
      if (radius < 1) {
        Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(sender);
        return;
      }
    }
    Location center = sender instanceof Player ? ((Player) sender).getLocation() : null;
    int killedAmount = getPlugin().getActiveMobManager().killAll(center, radius, true);
    Message.KILL_ALL_COMMAND_SUCCESS.sendSuccess(sender, Map.of("%amount%", StringUtil.formatNumber(killedAmount)));
  }
}
