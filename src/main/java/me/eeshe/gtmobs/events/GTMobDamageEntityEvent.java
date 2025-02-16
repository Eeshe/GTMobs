package me.eeshe.gtmobs.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.eeshe.gtmobs.models.ActiveMob;

public class GTMobDamageEntityEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final ActiveMob activeMob;
  private final Entity damaged;

  public GTMobDamageEntityEvent(ActiveMob activeMob, Entity damaged) {
    this.activeMob = activeMob;
    this.damaged = damaged;
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

  public Entity getDamaged() {
    return damaged;
  }
}
