package me.eeshe.gtmobs.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.config.Message;
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
    setPlayerCommand(true);
    setCompletions(Map.of(0, (sender, args) -> {
      return new ArrayList<>(plugin.getGTMobManager().getGTMobs().keySet());
    }));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    Player player = (Player) sender;
    String mobId = args[0];
    GTMob gtMob = GTMob.fromId(mobId);
    if (gtMob == null) {
      Message.GTMOB_NOT_FOUND.sendError(player, Map.of("%id%", mobId));
      return;
    }
    Integer amount = 1;
    if (args.length > 1) {
      amount = StringUtil.parseInteger(player, args[1]);
      if (amount == null) {
        return;
      }
      if (amount < 1) {
        Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(player);
        return;
      }
    }
    Double radius = 0D;
    if (args.length > 2) {
      radius = StringUtil.parseDouble(player, args[2]);
      if (radius == null) {
        return;
      }
      if (amount < 1) {
        Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(player);
        return;
      }
    }
    List<Location> safeLocations = LocationUtil.computeEmptyLocationsForSpawn(player.getEyeLocation(),
        radius.intValue(), amount);
    if (safeLocations.isEmpty()) {
      Message.NO_VALID_SPAWN_LOCATION.sendError(sender);
      return;
    }
    Random random = ThreadLocalRandom.current();
    for (int i = 0; i < amount; i++) {
      Location spawnLocation = safeLocations.get(random.nextInt(safeLocations.size()));
      gtMob.spawn(spawnLocation);
    }
    Message.SPAWN_COMMAND_SUCCESS.sendSuccess(player, Map.ofEntries(
        Map.entry("%amount%", StringUtil.formatNumber(amount)),
        Map.entry("%id%", mobId)));
  }
}
