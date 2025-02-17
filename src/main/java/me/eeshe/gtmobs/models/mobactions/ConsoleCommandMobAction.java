package me.eeshe.gtmobs.models.mobactions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.util.CommandUtil;

public class ConsoleCommandMobAction extends MobAction {
  private final List<String> commands;
  private final long delayTicks;

  public ConsoleCommandMobAction(List<String> commands, long delayTicks) {
    this.commands = commands;
    this.delayTicks = delayTicks;
  }

  @Override
  public void execute(LivingEntity gtmobEntity, Entity attacker) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      OfflinePlayer attackerPlayer = null;
      if (attacker instanceof OfflinePlayer) {
        attackerPlayer = (OfflinePlayer) attacker;
      }
      CommandUtil.executeCommands(attackerPlayer, commands);
    }, delayTicks);
  }

  @Override
  public String toString() {
    return getMobActionType().name() + ":" + String.join("-", List.of(
        String.join(";", commands),
        String.valueOf(delayTicks)));
  }

  public List<String> getCommands() {
    return commands;
  }

  public long getDelayTicks() {
    return delayTicks;
  }
}
