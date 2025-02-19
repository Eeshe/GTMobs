package me.eeshe.gtmobs.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.Spawner;
import me.eeshe.gtmobs.models.config.Message;
import me.eeshe.gtmobs.models.config.Sound;
import me.eeshe.gtmobs.util.CompletionUtil;
import me.eeshe.gtmobs.util.LogUtil;
import me.eeshe.gtmobs.util.Messager;
import me.eeshe.gtmobs.util.StringUtil;

public class CommandSpawner extends PluginCommand {
  private static final List<PluginCommand> SPAWNER_COMMANDS = new ArrayList<>();

  public CommandSpawner(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    SPAWNER_COMMANDS.addAll(List.of(
        new CommandSpawnerCreate(plugin, this),
        new CommandSpawnerInfo(plugin, this),
        new CommandSpawnerCopy(plugin, this),
        new CommandSpawnerDelete(plugin, this),
        new CommandSpawnerList(plugin, this),
        new CommandSpawnerTeleport(plugin, this),
        new CommandSpawnerSet(plugin, this)));

    setName("spawner");
    setPermission("gtmobs.admin");
    setSubcommands(SPAWNER_COMMANDS);
  }
}

class CommandSpawnerCreate extends PluginCommand {

  public CommandSpawnerCreate(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("create");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_CREATE_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_CREATE_COMMAND_USAGE);
    setArgumentAmount(1);
    setPlayerCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    Player player = (Player) sender;
    String spawnerId = args[0];
    if (Spawner.fromId(spawnerId) != null) {
      Message.SPAWNER_ALREADY_EXISTS.sendError(player, Map.of("%id%", spawnerId));
      return;
    }
    Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
    if (targetBlock == null) {
      Message.NOT_LOOKING_AT_BLOCK.sendError(player);
      return;
    }
    if (Spawner.fromLocation(targetBlock.getLocation()) != null) {
      Message.ALREADY_SPAWNER_BLOCK.sendError(player);
      return;
    }
    new Spawner(spawnerId, targetBlock.getLocation()).register();
    Message.SPAWNER_CREATE_COMMAND_SUCCESS.sendSuccess(player, Map.of("%id%", spawnerId));
  }
}

class CommandSpawnerInfo extends PluginCommand {

  public CommandSpawnerInfo(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("info");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_INFO_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_INFO_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
    setCompletions(Map.of(0, (sender, args) -> CompletionUtil.getSpawnerIds()));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    String spawnerId = args[0];
    Spawner spawner = Spawner.fromId(spawnerId);
    if (spawner == null) {
      Message.SPAWNER_NOT_FOUND.sendError(sender, Map.of("%spawner%", spawnerId));
      return;
    }
    Location location = spawner.getLocation();
    Message.SPAWNER_INFO_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%spawner%", spawner.getId()),
        Map.entry("%mob%", String.valueOf(spawner.getMobId())),
        Map.entry("%location%", String.format("X: %s | Y: %s | Z: %s",
            location.getBlockX(), location.getBlockY(), location.getBlockZ())),
        Map.entry("%minimum%", StringUtil.formatNumber(spawner.getAmount().getMin())),
        Map.entry("%maximum%", StringUtil.formatNumber(spawner.getAmount().getMax())),
        Map.entry("%limit%", StringUtil.formatNumber(spawner.getLimit())),
        Map.entry("%interval%", StringUtil.formatSeconds(spawner.getFrequencyTicks() / 20)),
        Map.entry("%radius%", StringUtil.formatNumber(spawner.getRadius()))));
  }
}

class CommandSpawnerCopy extends PluginCommand {

  public CommandSpawnerCopy(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("copy");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_COPY_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_COPY_COMMAND_USAGE);
    setArgumentAmount(2);
    setPlayerCommand(true);
    setCompletions(Map.of(0, (sender, args) -> CompletionUtil.getSpawnerIds()));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    Player player = (Player) sender;
    String sourceSpawnerId = args[0];
    Spawner sourceSpawner = Spawner.fromId(sourceSpawnerId);
    if (sourceSpawner == null) {
      Message.SPAWNER_NOT_FOUND.sendError(player, Map.of("%spawner%", sourceSpawnerId));
      return;
    }
    Block targetBlock = player.getTargetBlock((Set<Material>) null, 5);
    if (targetBlock == null) {
      Message.NOT_LOOKING_AT_BLOCK.sendError(player);
      return;
    }
    if (Spawner.fromLocation(targetBlock.getLocation()) != null) {
      Message.ALREADY_SPAWNER_BLOCK.sendError(player);
      return;
    }
    String newSpawnerId = args[1];
    if (Spawner.fromId(newSpawnerId) != null) {
      Message.SPAWNER_ALREADY_EXISTS.sendError(player, Map.of("%id%", newSpawnerId));
      return;
    }
    new Spawner(newSpawnerId, targetBlock.getLocation(), sourceSpawner).register();
    Message.SPAWNER_COPY_COMMAND_SUCCESS.sendSuccess(player, Map.ofEntries(
        Map.entry("%source%", sourceSpawnerId),
        Map.entry("%spawner%", newSpawnerId)));
  }
}

class CommandSpawnerDelete extends PluginCommand {

  public CommandSpawnerDelete(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("delete");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_DELETE_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_DELETE_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
    setCompletions(Map.of(0, (sender, args) -> CompletionUtil.getSpawnerIds()));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    String spawnerId = args[0];
    Spawner spawner = Spawner.fromId(spawnerId);
    if (spawner == null) {
      Message.SPAWNER_NOT_FOUND.sendError(sender, Map.of("%spawner%", spawnerId));
      return;
    }
    spawner.unregister();
    Message.SPAWNER_DELETE_COMMAND_SUCCESS.sendSuccess(sender, Map.of("%spawner%", spawnerId));
  }
}

class CommandSpawnerList extends PluginCommand {

  public CommandSpawnerList(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("list");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_LIST_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_LIST_COMMAND_USAGE);
    setArgumentAmount(0);
    setUniversalCommand(true);
    setCompletions(Map.of(0, (sender, args) -> List.of("near")));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    boolean isNearSearch = false;
    if (args.length > 0) {
      isNearSearch = args[0].equalsIgnoreCase("near");
    }
    if (isNearSearch && !(sender instanceof Player)) {
      Message.PLAYER_COMMAND.sendError(sender);
      return;
    }
    Double radius = 0D;
    if (isNearSearch && args.length > 1) {
      radius = StringUtil.parseDouble(sender, args[1]);
      if (radius == null) {
        return;
      }
    }
    if (isNearSearch && radius < 1) {
      Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(sender);
      return;
    }
    Integer page = 1;
    if (args.length > 0) {
      int pageIndex;
      if (isNearSearch) {
        pageIndex = 2;
      } else {
        pageIndex = 0;
      }
      if (pageIndex >= args.length) {
        Message.USAGE_TEXT.sendError(sender, Map.of("%usage%", getUsageMessage().getValue()));
        return;
      }
      page = StringUtil.parseInteger(sender, args[pageIndex]);
      if (page == null) {
        return;
      }
      if (page < 1) {
        Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(sender);
        return;
      }
    }
    List<Spawner> spawners;
    if (!isNearSearch) {
      spawners = new ArrayList<>(getPlugin().getSpawnerManager()
          .getSpawners().values());
    } else {
      // Filter only near spawners
      Location center = ((Player) sender).getLocation();
      radius = Math.pow(radius, 2);
      spawners = new ArrayList<>();
      for (Spawner spawner : getPlugin().getSpawnerManager().getSpawners().values()) {
        if (spawner.getLocation().distanceSquared(center) > radius) {
          continue;
        }
        spawners.add(spawner);
      }
    }
    int pageSize = 10;

    // Calculate start and end indices
    int startIndex = (page - 1) * pageSize;
    if (startIndex > spawners.size()) {
      Message.PAGE_NOT_FOUND.sendError(sender, Map.of("%page%", StringUtil.formatNumber(page)));
      return;
    }
    int endIndex = Math.min(startIndex + pageSize, spawners.size());

    // Get the sublist for the specified page
    spawners = spawners.subList(startIndex, endIndex);
    if (spawners.isEmpty()) {
      Message.NO_SPAWNERS_FOUND.sendError(sender);
      return;
    }

    Messager.sendMessage(sender, generateSpawnerListString(spawners));
    Sound.SUCCESS.play(sender);
  }

  /**
   * Generates the String with the spawner list
   *
   * @param spawners Spawners to display in the list
   * @return Spawner list String
   */
  private String generateSpawnerListString(List<Spawner> spawners) {
    StringBuilder stringBuilder = new StringBuilder();
    Message spawnerEntryFormat = Message.SPAWNER_LIST_ENTRY;
    for (int index = 0; index < spawners.size(); index++) {
      Spawner spawner = spawners.get(index);
      stringBuilder.append(spawnerEntryFormat.getFormattedValue(
          Map.of("%spawner%", spawner.getId())));
      if (index == spawners.size() - 1) {
        continue;
      }
      stringBuilder.append("\n");
    }
    return stringBuilder.toString();
  }
}

class CommandSpawnerTeleport extends PluginCommand {

  public CommandSpawnerTeleport(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("teleport");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_TELEPORT_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_TELEPORT_COMMAND_USAGE);
    setArgumentAmount(1);
    setPlayerCommand(true);
    setCompletions(Map.of(0, (sender, args) -> CompletionUtil.getSpawnerIds()));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    Player player = (Player) sender;
    String spawnerId = args[0];
    Spawner spawner = Spawner.fromId(spawnerId);
    if (spawner == null) {
      Message.SPAWNER_NOT_FOUND.sendError(player, Map.of("%spawner%", spawnerId));
      return;
    }
    Location teleportLocation = spawner.getLocation().clone().add(0.5, 1, 0.5);
    teleportLocation.setPitch(90);
    teleportLocation.setYaw(0);

    player.teleport(teleportLocation);
    Sound.TELEPORT.play(player);
  }
}

class CommandSpawnerSet extends PluginCommand {
  private static final List<PluginCommand> SPAWNER_SET_COMMANDS = new ArrayList<>();

  public CommandSpawnerSet(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    if (this.getClass().equals(CommandSpawnerSet.class)) {
      // Only register if it's the main /gtmobs spawner set <Spawner> command
      SPAWNER_SET_COMMANDS.addAll(List.of(
          new CommandSpawnerSetMob(plugin, this),
          new CommandSpawnerSetMin(plugin, this),
          new CommandSpawnerSetMax(plugin, this),
          new CommandSpawnerSetLimit(plugin, this),
          new CommandSpawnerSetInterval(plugin, this),
          new CommandSpawnerSetRadius(plugin, this)));
    }
    setName("set");
    setPermission("gtmobs.admin");
    setArgumentAmount(2);
    setSubcommands(SPAWNER_SET_COMMANDS);
    setCompletions(Map.ofEntries(
        Map.entry(0, (sender, args) -> CompletionUtil.getSpawnerIds()),
        Map.entry(1, (sender, args) -> new ArrayList<>(getSubcommands().keySet()))));
  }

  @Override
  public void execute(CommandSender sender, String[] args) {
    String spawnerId = args[0];
    Spawner spawner = Spawner.fromId(spawnerId);
    if (spawner == null) {
      Message.SPAWNER_NOT_FOUND.sendError(sender, Map.of("%spawner%", spawnerId));
      return;
    }
    String subcommandName = args[1];
    if (!getSubcommands().containsKey(subcommandName)) {
      Messager.sendHelpMessage(sender, getSubcommands().values());
      return;
    }
    CommandSpawnerSet subcommand = (CommandSpawnerSet) getSubcommand(subcommandName);
    if (args.length - 2 < subcommand.getArgumentAmount()) {
      Message.USAGE_TEXT.sendError(sender, Map.of("%usage%", subcommand.getUsageMessage().getValue()));
      return;
    }
    subcommand.execute(sender, Arrays.copyOfRange(args, 2, args.length), spawner);
  }

  public void execute(CommandSender sender, String[] args, Spawner spawner) {
  }
}

class CommandSpawnerSetMob extends CommandSpawnerSet {

  public CommandSpawnerSetMob(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("mob");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_SET_MOB_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_SET_MOB_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
    setCompletions(Map.of(0, (sender, args) -> {
      return new ArrayList<>(plugin.getGTMobManager().getGTMobs().keySet());
    }));
  }

  @Override
  public void execute(CommandSender sender, String[] args, Spawner spawner) {
    LogUtil.sendWarnLog("SET MOB ARGS: " + Arrays.toString(args));
    String mobId = args[0];
    GTMob gtMob = GTMob.fromId(mobId);
    if (gtMob == null) {
      Message.GTMOB_NOT_FOUND.sendError(sender, Map.of("%id%", mobId));
      return;
    }
    spawner.setMobId(mobId);
    Message.SPAWNER_SET_MOB_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%spawner%", spawner.getId()),
        Map.entry("%mob%", mobId)));
  }
}

class CommandSpawnerSetMin extends CommandSpawnerSet {

  public CommandSpawnerSetMin(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("min");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_SET_MIN_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_SET_MIN_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args, Spawner spawner) {
    Integer minimum = StringUtil.parseInteger(sender, args[0]);
    if (minimum == null) {
      return;
    }
    if (minimum < 0) {
      Message.AMOUNT_MUST_BE_ZERO_OR_HIGHER.sendError(sender);
      return;
    }
    if (minimum > spawner.getAmount().getMax()) {
      Message.MINIMUM_OVER_MAXIMUM.sendError(sender,
          Map.of("%maximum%", StringUtil.formatNumber(spawner.getAmount().getMax())));
      return;
    }
    spawner.setMinimumAmount(minimum);
    Message.SPAWNER_SET_MIN_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%spawner%", spawner.getId()),
        Map.entry("%minimum%", StringUtil.formatNumber(minimum))));
  }
}

class CommandSpawnerSetMax extends CommandSpawnerSet {

  public CommandSpawnerSetMax(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("max");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_SET_MAX_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_SET_MAX_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args, Spawner spawner) {
    Integer maximum = StringUtil.parseInteger(sender, args[0]);
    if (maximum == null) {
      return;
    }
    if (maximum < 1) {
      Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(sender);
      return;
    }
    if (maximum < spawner.getAmount().getMin()) {
      Message.MAXIMUM_UNDER_MINIMUM.sendError(sender,
          Map.of("%minimum%", StringUtil.formatNumber(spawner.getAmount().getMin())));
      return;
    }
    spawner.setMaximumAmount(maximum);
    Message.SPAWNER_SET_MAX_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%spawner%", spawner.getId()),
        Map.entry("%maximum%", StringUtil.formatNumber(maximum))));
  }
}

class CommandSpawnerSetLimit extends CommandSpawnerSet {

  public CommandSpawnerSetLimit(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("limit");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_SET_LIMIT_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_SET_LIMIT_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args, Spawner spawner) {
    Integer limit = StringUtil.parseInteger(sender, args[0]);
    if (limit == null) {
      return;
    }
    if (limit < 1) {
      Message.AMOUNT_MUST_BE_HIGHER_THAN_ZERO.sendError(sender);
      return;
    }
    spawner.setLimit(limit);
    Message.SPAWNER_SET_LIMIT_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%spawner%", spawner.getId()),
        Map.entry("%limit%", StringUtil.formatNumber(limit))));
  }
}

class CommandSpawnerSetInterval extends CommandSpawnerSet {

  public CommandSpawnerSetInterval(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("interval");
    setPermission("gtmobs.interval");
    setInfoMessage(Message.SPAWNER_SET_INTERVAL_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_SET_INTERVAL_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args, Spawner spawner) {
    long frequencyMillis = StringUtil.textToMillis(sender, args[0], true);
    if (frequencyMillis == 0) {
      return;
    }
    long frequencyTicks = TimeUnit.MILLISECONDS.toSeconds(frequencyMillis) * 20L;
    spawner.setFrequencyTicks(frequencyTicks);
    Message.SPAWNER_SET_INTERVAL_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%spawner%", spawner.getId()),
        Map.entry("%interval%", args[0])));
  }
}

class CommandSpawnerSetRadius extends CommandSpawnerSet {

  public CommandSpawnerSetRadius(GTMobs plugin, PluginCommand parentPluginCommand) {
    super(plugin, parentPluginCommand);

    setName("radius");
    setPermission("gtmobs.admin");
    setInfoMessage(Message.SPAWNER_SET_RADIUS_COMMAND_INFO);
    setUsageMessage(Message.SPAWNER_SET_RADIUS_COMMAND_USAGE);
    setArgumentAmount(1);
    setUniversalCommand(true);
  }

  @Override
  public void execute(CommandSender sender, String[] args, Spawner spawner) {
    Double radius = StringUtil.parseDouble(sender, args[0]);
    if (radius == null) {
      return;
    }
    if (radius < 0) {
      Message.AMOUNT_MUST_BE_ZERO_OR_HIGHER.sendError(sender);
      return;
    }
    spawner.setRadius(radius);
    Message.SPAWNER_SET_RADIUS_COMMAND_SUCCESS.sendSuccess(sender, Map.ofEntries(
        Map.entry("%spawner%", spawner.getId()),
        Map.entry("%radius%", StringUtil.formatNumber(radius))));
  }
}
