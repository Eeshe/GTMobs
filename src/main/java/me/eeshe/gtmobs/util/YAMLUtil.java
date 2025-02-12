package me.eeshe.gtmobs.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class YAMLUtil {

  /**
   * Saves the passed Location to the passed data file directly to the passed
   * path.
   * It uses exact locations, which means that X Y and Z coordinates will be
   * floating numbers.
   *
   * @param location Location that will be saved.
   * @param dataFile Data file where the location will be saved.
   * @param path     Path the location will be saved into.
   */
  //
  // It uses exact locations, which means that X Y and Z coordinates will be
  // floating numbers
  public static void writeLocationToYaml(Location location, FileConfiguration dataFile, String path) {
    dataFile.set(path + ".x", location.getX());
    dataFile.set(path + ".y", location.getY());
    dataFile.set(path + ".z", location.getZ());
    dataFile.set(path + ".world", location.getWorld().getName());
  }

  /**
   * Saves the passed Location to the passed data file directly to the passed
   * path.
   * It uses block locations, which means that X Y and Z coordinates will be
   * integers.
   *
   * @param location Location that will be saved.
   * @param dataFile Data file where the location will be saved.
   * @param path     Path the location will be saved into.
   */
  // Saves the passed Location to the passed data file directly to the passed path
  // It uses block locations, which means that X Y and Z coordinates will be int
  // numbers
  public static void writeBlockLocationToYaml(Location location, FileConfiguration dataFile, String path) {
    dataFile.set(path + ".x", location.getBlockX());
    dataFile.set(path + ".y", location.getBlockY());
    dataFile.set(path + ".z", location.getBlockZ());
    dataFile.set(path + ".world", location.getWorld().getName());
  }

  /**
   * Loads the location found in the passed data file in the passed path.
   *
   * @param dataFile Data file where the location will be loaded from.
   * @param path     Path where the location will be searched.
   * @return Location found in the passed data file in the passed path.
   */
  public static Location fetchLocationFromYaml(FileConfiguration dataFile, String path) {
    int x = dataFile.getInt(path + ".x");
    int y = dataFile.getInt(path + ".y");
    int z = dataFile.getInt(path + ".z");
    float pitch = (float) dataFile.getDouble(path + ".pitch");
    float yaw = (float) dataFile.getDouble(path + ".yaw");
    String worldName = dataFile.getString(path + ".world");
    if (worldName == null) {
      LogUtil.sendWarnLog("Missing World data for location saved on '" + dataFile.getName() + "' in path '" +
          path + "'.");
      return null;
    }
    World world = Bukkit.getWorld(worldName);
    if (world == null) {
      LogUtil.sendWarnLog("Unknown world '" + worldName + "' for location saved on '" + dataFile.getName() +
          "' in path '" + path + "'.");
      return null;
    }
    Location location = new Location(world, x, y, z);
    location.setPitch(pitch);
    location.setYaw(yaw);
    return location;
  }

  /**
   * Writes the passed list of UUIDs to the passed data file.
   *
   * @param uuids    List of UUIDs that will be written in the passed data file.
   * @param dataFile Data file where the UUIDs will be written.
   * @param path     Path where the UUIDs will be written.
   */
  public static void writeUuidList(List<UUID> uuids, FileConfiguration dataFile, String path) {
    List<String> stringUuids = new ArrayList<>();
    for (UUID uuid : uuids) {
      stringUuids.add(uuid.toString());
    }
    dataFile.set(path, stringUuids);
  }

  /**
   * Fetches the saved list of UUIDs from the passed data file using the passed
   * path.
   *
   * @param dataFile Data file where the UUIDs will be fetched from.
   * @param path     Path where the UUIDs are written.
   * @return Saved list of UUIDs from the passed data file using the passed path.
   */
  public static List<UUID> fetchUuidList(FileConfiguration dataFile, String path) {
    List<UUID> uuids = new ArrayList<>();
    for (String stringUuid : dataFile.getStringList(path)) {
      uuids.add(UUID.fromString(stringUuid));
    }
    return uuids;
  }

  /**
   * Fetches the ItemStacks found in the passed data file on the passed data file.
   *
   * @param dataFile Data file where the ItemStacks will be fetched from.
   * @param path     Path where the data will be searched.
   * @return ItemStacks found in the passed data file on the passed data file.
   */
  public static ItemStack[] fetchItemStacks(FileConfiguration dataFile, String path) {
    return dataFile.getList(path, new ArrayList<>()).toArray(new ItemStack[0]);
  }
}
