package me.eeshe.gtmobs.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LocationUtil {

  /**
   * Generates a random location around the passed center with the passed offset.
   *
   * @param center  Location used as center.
   * @param offset  Offset that will be used to generate the location.
   * @param randomY Whether the Y coordinate should be randomized.
   * @param safe    Whether the random location needs to be safe.
   * @return Random location with the passed offset.
   */
  public static Location generateRandomLocationOffset(Location center, double offset, boolean randomY, boolean safe) {
    ThreadLocalRandom random = ThreadLocalRandom.current();
    double xOffset = random.nextDouble(-offset, offset);
    double zOffset = random.nextDouble(-offset, offset);
    Location location = center.clone();
    if (randomY) {
      location.add(0, random.nextDouble(-offset, offset), 0);
    }
    if (!safe)
      return location.add(xOffset, 0, zOffset);

    return checkRandomLocation(location.add(xOffset, 0, zOffset));
  }

  /**
   * Generates a random location around the passed center with the passed radius.
   *
   * @param center Location used as center.
   * @param radius Radius around the center the location will be generated in.
   * @return Random location with the passed offset.
   */
  public static Location generateRandomLocationRadius(Location center, double radius) {
    ThreadLocalRandom random = ThreadLocalRandom.current();

    // Generate a random angle in radians between 0 and 2π (360 degrees)
    double angle = random.nextDouble(0, 2 * Math.PI);

    // Calculate the random X and Y offsets using the polar coordinates
    double offsetX = radius * Math.cos(angle);
    double offsetY = radius * Math.sin(angle);

    return checkRandomLocation(center.clone().add(offsetX, 0, offsetY));
  }

  /**
   * Checks if the passed location is a safe random location.
   *
   * @param location Location that will be checked.
   * @return Modified location on its safe point. Null if it couldn't be found.
   */
  private static Location checkRandomLocation(Location location) {
    if (isSafeLocation(location))
      return location;

    // Adjust Y location
    int yOffset = 5; // Maximum layers of Y that will be checked. It includes both negative and
                     // positive values.
    int portalY = location.getBlockY();
    for (int y = portalY - yOffset; y < portalY + yOffset; y++) {
      location.setY(y);

      if (isSafeLocation(location))
        return location;
    }
    return null;
  }

  /**
   * Calculates the passed amount of locations in between the two passed
   * locations.
   *
   * @param start          Location where the calculation will start.
   * @param end            Location where the calculation will end.
   * @param locationAmount Number of locations that will be calculated.
   * @return List of locations in between the two passed locations.
   */
  public static List<Location> calculateLocationsBetween(Location start, Location end, int locationAmount) {
    List<Location> points = new ArrayList<>();
    final World world = start.getWorld();
    if (!world.equals(end.getWorld()))
      return points;

    final double xDiff = (end.getX() - start.getX()) / (locationAmount + 1.0);
    final double yDiff = (end.getY() - start.getY()) / (locationAmount + 1.0);
    final double zDiff = (end.getZ() - start.getZ()) / (locationAmount + 1.0);
    for (int i = 0; i < locationAmount; i++) {
      double x = start.getX() + xDiff * (i + 1);
      double y = start.getY() + yDiff * (i + 1);
      double z = start.getZ() + zDiff * (i + 1);
      points.add(new Location(world, x, y, z));
    }
    return points;
  }

  /**
   * Calculates the passed amount of locations starting in the passed starting
   * point following the passed direction.
   *
   * @param startLocation Location where the calculation will start.
   * @param direction     Direction the locations will move in.
   * @param increment     Increment between each location.
   * @param amount        Amount of locations to be calculated.
   * @return List of calculated locations.
   */
  public static List<Location> calculateLocationsInDirection(
      Location startLocation,
      Vector direction,
      double increment,
      int amount) {
    List<Location> locations = new ArrayList<>();
    Vector vectorIncrement = direction.clone().normalize().multiply(increment);

    startLocation = startLocation.clone();
    for (int i = 0; i < amount; i++) {
      startLocation.add(vectorIncrement);
      locations.add(startLocation.clone());
    }
    return locations;
  }

  /**
   * Gets the location behind the passed entity with the passed distance.
   *
   * @param livingEntity LivingEntity whose behind location will be returned.
   * @param distance     Distance behind the living entity.
   * @param safe         Whether the location should be safe.
   * @return Location behind the passed entity with the passed distance.
   */
  public static Location getLocationBehind(LivingEntity livingEntity, int distance, boolean safe) {
    Location entityLocation = livingEntity.getLocation();
    Location behindLocation = null;
    for (int i = 0; i < distance; i++) {
      double nX;
      double nZ;
      float nang = entityLocation.getYaw() + 90;
      if (nang < 0)
        nang += 360;
      nX = Math.cos(Math.toRadians(nang));
      nZ = Math.sin(Math.toRadians(nang));

      behindLocation = entityLocation.subtract(nX, 0, nZ);
      if (safe && !isSafeLocation(behindLocation))
        break;
    }
    return behindLocation;
  }

  /**
   * Checks if the passed location is a safe location.
   *
   * @param location Location that will be checked.
   * @return True if the passed location is a safe location.
   */
  public static boolean isSafeLocation(Location location) {
    if (location.getBlock().getType().isSolid())
      return false;

    Block locationBlock = location.getBlock();
    if (locationBlock.getRelative(BlockFace.DOWN).isEmpty())
      return false;

    return locationBlock.getRelative(BlockFace.UP).isEmpty();
  }
}
