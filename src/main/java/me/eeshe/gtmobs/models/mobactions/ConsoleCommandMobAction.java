package me.eeshe.gtmobs.models.mobactions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.util.CommandUtil;

public class ConsoleCommandMobAction extends MobAction {
  private final List<String> commands;
  private final IntRange delayTicks;

  public ConsoleCommandMobAction(double chance, List<String> commands,
      IntRange delayTicks) {
    super(chance);
    this.commands = commands;
    this.delayTicks = delayTicks;
  }

  @Override
  public boolean execute(LivingEntity gtmobEntity, Entity attacker) {
    if (!super.execute(gtmobEntity, attacker)) {
      return false;
    }
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      OfflinePlayer attackerPlayer = null;
      if (attacker instanceof OfflinePlayer) {
        attackerPlayer = (OfflinePlayer) attacker;
      }
      CommandUtil.executeCommands(attackerPlayer, commands);
    }, delayTicks.generateRandom());
    return true;
  }

  public List<String> getCommands() {
    return commands;
  }

  public IntRange getDelayTicks() {
    return delayTicks;
  }
}
