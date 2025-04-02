package me.eeshe.gtmobs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.util.CommandUtil;
import me.eeshe.gtmobs.util.LocationUtil;
import me.eeshe.gtmobs.util.StringUtil;

public class CommandSpawn extends PluginCommand {

  public CommandSpawn(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("spawn");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWN_COMMAND_INFO);
    setUsageMessage(Message.SPAWN_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
    setCompletions(Map.ofEntries(
        Map.entry(0, (sender, args) -> {
          return new ArrayList<>(plugin.getGTMobManager().getGTMobs().keySet());
        }),
        Map.entry(3, (sender, args) -> null)));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    if (args.length < 4 && !(sender instanceof Player)) {
      Message.NOT_ENOUGH_ARGUMENTS.sendError(sender);
      return;
    }
    String mobId = args[0];
    GTMob gtMob = GTMob.fromId(mobId);
    if (gtMob == null) {
      Message.GTMOB_NOT_FOUND.sendError(sender, Map.of("%id%", mobId));
      return;
    }
    Integer amount = 1;
    if (args.length > 1) {
      amount = StringUtil.parseInteger(sender, args[1]);
      if (amount == null) {
        return;
      }
      if (amount < 1) {
        Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(sender);
        return;
      }
    }
    Double radius = 0D;
    if (args.length > 2) {
      radius = StringUtil.parseDouble(sender, args[2]);
      if (radius == null) {
        return;
      }
      if (amount < 1) {
        Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(sender);
        return;
      }
    }
    Location location;
    if (args.length >= 4) {
      Player target = CommandUtil.getOnlineTarget(sender, args[3]);
      if (target == null) {
        return;
      }
      location = target.getLocation();
    } else {
      location = ((Player) sender).getLocation();
    }
    List<Location> safeLocations = LocationUtil.computeLocationsForSpawn(location,
        radius.intValue(), amount);
    if (safeLocations.isEmpty()) {
      Message.NO_VALID_SPAWN_LOCATION.sendError(sender);
      return;
    }
    int spawnedMobs = 0;
    for (int i = 0; i < amount; i++) {
      Location spawnLocation = safeLocations.size() == 1 ? safeLocations.get(0)
          : safeLocations.remove(0);
      LivingEntity spawnedMob = gtMob.spawn(spawnLocation);
      if (spawnedMob == null) {
        continue;
      }
      spawnedMobs += 1;
    }
    if (spawnedMobs != amount) {
      Message.SPAWN_ERROR.sendError(sender);
      if (amount == 1) {
        return;
      }
    }
    Message.SPAWN_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%amount%", StringUtil.formatNumber(spawnedMobs)),
        Map.entry("%id%", mobId)));
  }
}
