package me.eeshe.gtmobs.commands;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.ActiveMob;
import me.eeshe.gtmobs.models.GTMob;
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
        Message.INVALID_NUMERIC_INPUT_ZERO.sendError(sender);
        return;
      }
    }
    int killedAmount = 0;
    if (radius == 0) {
      for (ActiveMob activeMob : new ArrayList<>(getPlugin().getActiveMobManager().getActiveMobs().values())) {
        activeMob.getLivingEntity().setHealth(0);
        killedAmount += 1;
      }
    } else {
      Player player = (Player) sender;
      for (Entity nearbyEntity : player.getNearbyEntities(radius, radius, radius)) {
        ActiveMob gtMob = ActiveMob.fromEntity(nearbyEntity);
        if (gtMob == null) {
          continue;
        }
        ((LivingEntity) nearbyEntity).setHealth(0);
        killedAmount += 1;
      }
    }
    Message.KILL_ALL_COMMAND_SUCCESS.sendSuccess(sender, Map.of("%amount%", StringUtil.formatNumber(killedAmount)));
  }
}
