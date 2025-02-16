package me.eeshe.gtmobs.events;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.eeshe.gtmobs.models.ActiveMob;

public class GTMobDeathEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final ActiveMob activeMob;
  private final LivingEntity killer;

  public GTMobDeathEvent(ActiveMob activeMob, LivingEntity killer) {
    this.activeMob = activeMob;
    this.killer = killer;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public ActiveMob getActiveMob() {
    return activeMob;
  }

  public LivingEntity getKiller() {
    return killer;
  }
}
