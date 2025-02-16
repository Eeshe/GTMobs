package me.eeshe.gtmobs.events;

import org.bukkit.entity.Entity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.eeshe.gtmobs.models.ActiveMob;

public class EntityDamageGTMobEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();

  private final Entity damager;
  private final ActiveMob activeMob;

  public EntityDamageGTMobEvent(Entity attacker, ActiveMob activeMob) {
    this.damager = attacker;
    this.activeMob = activeMob;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static HandlerList getHandlerList() {
    return HANDLER_LIST;
  }

  public Entity getDamager() {
    return damager;
  }

  public ActiveMob getActiveMob() {
    return activeMob;
  }
}
