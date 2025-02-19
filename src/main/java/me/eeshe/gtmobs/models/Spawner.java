package me.eeshe.gtmobs.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitTask;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.util.LocationUtil;
import me.eeshe.gtmobs.util.LogUtil;

public class Spawner {
  private final String id;
  private final Location location;
  private String mobId;
  private IntRange amount;
  private long frequencyTicks;
  private double radius;
  private int limit;

  private final Set<UUID> spawnedMobs = new HashSet<>();
  private BukkitTask spawnTask;

  public Spawner(String id, Location location) {
    this.id = id;
    this.location = location;
    this.mobId = null;
    this.amount = new IntRange(1, 1);
    this.frequencyTicks = 100L;
    this.radius = 5;
    this.limit = 8;
  }

  public Spawner(String id, Location location, Spawner spawner) {
    this.id = id;
    this.location = location;
    this.mobId = spawner.getMobId();
    this.amount = spawner.getAmount();
    this.frequencyTicks = spawner.getFrequencyTicks();
    this.radius = spawner.getRadius();
    this.limit = spawner.getLimit();
  }

  public Spawner(String id, Location location, String mobId, IntRange amount, long frequencyTicks, double radius,
      int limit) {
    this.id = id;
    this.location = location;
    this.mobId = mobId;
    this.amount = amount;
    this.frequencyTicks = frequencyTicks;
    this.radius = radius;
    this.limit = limit;
  }

  /**
   * Searches and returns the Spawner corresponding to the passed ID
   *
   * @param id ID to search
   * @return Spawner corresponding to the passed ID
   */
  public static Spawner fromId(String id) {
    return GTMobs.getInstance().getSpawnerManager().getSpawners().get(id);
  }

  /**
   * Searches and returns the Spawner corresponding to the passed ID
   *
   * @param location Location to search
   * @return Spawner corresponding to the passed location
   */
  public static Spawner fromLocation(Location location) {
    if (location == null) {
      return null;
    }
    for (Spawner spawner : GTMobs.getInstance().getSpawnerManager().getSpawners().values()) {
      if (!location.equals(spawner.getLocation())) {
        continue;
      }
      return spawner;
    }
    return null;
  }

  /**
   * Loads and saves the data of the Spawner
   */
  public void register() {
    GTMobs.getInstance().getSpawnerManager().save(this);
    load();
  }

  /**
   * Loads the Spawner in the SpawnerManager class
   */
  public void load() {
    GTMobs.getInstance().getSpawnerManager().getSpawners().put(id, this);
    startSpawnTask();
  }

  /**
   * Unloads the Spawner and deletes its data
   */
  public void unregister() {
    unload();
    GTMobs.getInstance().getSpawnerManager().delete(this);
  }

  /**
   * Unloads the Spawner from the SpawnerManager class
   */
  public void unload() {
    stopSpawnTask();
    despawnMobs();
    GTMobs.getInstance().getSpawnerManager().getSpawners().remove(id);
  }

  /**
   * Despawns all the mobs spawned by the Spawner
   */
  private void despawnMobs() {
    for (UUID spawnedMobUuid : new ArrayList<>(spawnedMobs)) {
      ActiveMob activeMob = ActiveMob.fromEntity(Bukkit.getEntity(spawnedMobUuid));
      if (activeMob == null) {
        continue;
      }
      activeMob.despawn();
    }
    spawnedMobs.clear();
  }

  /**
   * Starts the task that will periodically spawn GTMobs
   */
  public void startSpawnTask() {
    this.spawnTask = Bukkit.getScheduler().runTaskTimer(GTMobs.getInstance(), () -> {
      if (!canSpawn()) {
        return;
      }
      spawn();
    }, frequencyTicks, frequencyTicks);
  }

  /**
   * Checks if new mobs can be spawned based in the amount of already spawned
   * mobs
   *
   * @return Whether mobs can be spawned
   */
  public boolean canSpawn() {
    return spawnedMobs.size() < limit;
  }

  /**
   * Spawns a round of the GTMob
   */
  public void spawn() {
    GTMob gtMob = GTMob.fromId(mobId);
    if (gtMob == null) {
      LogUtil.sendWarnLog("Unknown GTMob '" + mobId + "' for spawner '" + id + "'.");
      return;
    }
    for (int i = 0; i < Math.min(amount.generateRandom(), limit - spawnedMobs.size()); i++) {
      Location spawnLocation = LocationUtil.generateRandomLocationOffset(location,
          radius, false, true);
      if (spawnLocation == null) {
        continue;
      }
      LivingEntity entity = gtMob.spawn(spawnLocation, this);
      spawnedMobs.add(entity.getUniqueId());
    }
  }

  /**
   * Stops the spawn task
   */
  public void stopSpawnTask() {
    if (spawnTask == null) {
      return;
    }
    spawnTask.cancel();
    spawnTask = null;
  }

  /**
   * Reloads the spawn task.
   */
  public void reloadSpawnTask() {
    stopSpawnTask();
    startSpawnTask();
  }

  /**
   * Save the Spawner's data
   */
  private void saveData() {
    reloadSpawnTask();
    GTMobs.getInstance().getSpawnerManager().save(this);
  }

  public String getId() {
    return id;
  }

  public Location getLocation() {
    return location;
  }

  public String getMobId() {
    return mobId;
  }

  public void setMobId(String mobId) {
    this.mobId = mobId;
    saveData();
  }

  public IntRange getAmount() {
    return amount;
  }

  public void setMaximumAmount(int maximum) {
    setAmount(new IntRange(amount.getMin(), maximum));
  }

  public void setMinimumAmount(int minimum) {
    setAmount(new IntRange(minimum, amount.getMax()));
  }

  public void setAmount(IntRange amount) {
    this.amount = amount;
    saveData();
  }

  public long getFrequencyTicks() {
    return frequencyTicks;
  }

  public void setFrequencyTicks(long frequencyTicks) {
    this.frequencyTicks = frequencyTicks;
    saveData();
  }

  public double getRadius() {
    return radius;
  }

  public void setRadius(double radius) {
    this.radius = radius;
    saveData();
  }

  public int getLimit() {
    return limit;
  }

  public void setLimit(int limit) {
    this.limit = limit;
    saveData();
  }

  public Set<UUID> getSpawnedMobs() {
    return spawnedMobs;
  }

  public void addSpawnedMob(Entity mob) {
    spawnedMobs.add(mob.getUniqueId());
  }

  public void removeSpawnedMob(Entity mob) {
    spawnedMobs.remove(mob.getUniqueId());
  }
}
