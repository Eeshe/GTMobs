package me.eeshe.gtmobs.models.mobactions;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.util.StringUtil;

public class MessageMobAction extends MobAction {
  private final String message;
  private final double radius;
  private final long delayTicks;

  public MessageMobAction(String message, double radius, long delayTicks) {
    this.message = message;
    this.radius = radius;
    this.delayTicks = delayTicks;
  }

  @Override
  public void execute(LivingEntity gtMobEntity, Entity attacker) {
    Bukkit.getScheduler().runTaskLater(GTMobs.getInstance(), () -> {
      for (Entity entity : gtMobEntity.getNearbyEntities(radius, radius, radius)) {
        if (!(entity instanceof Player)) {
          continue;
        }
        ((Player) entity).sendMessage(StringUtil.formatColor(message));
      }
    }, delayTicks);
  }

  @Override
  public String toString() {
    return MobActionType.MESSAGE.name() + ":" + String.join("-", List.of(
        "[" + message + "]",
        String.valueOf(radius),
        String.valueOf(delayTicks)));
  }
}
