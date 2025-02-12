package me.eeshe.gtmobs.util;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

public final class PermissionUtil {

  /**
   * Computes the player's highest value of the passed permission.
   *
   * @param player           Player to compute.
   * @param permissionPrefix Permission prefix to search.
   * @param defaultValue     Default returned value.
   * @return The player's highest value of the passed permission.
   */
  public static int computePermissionValue(Player player, String permissionPrefix, int defaultValue) {
    if (player.isOp())
      return Integer.MAX_VALUE;

    int highestPermissionAmount = 0;
    for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
      var permission = attachmentInfo.getPermission();
      if (!permission.startsWith(permissionPrefix))
        continue;

      highestPermissionAmount = Math.max(highestPermissionAmount,
          Integer.parseInt(permission.replace(permissionPrefix, "")));
    }
    if (highestPermissionAmount == 0)
      return defaultValue;

    return highestPermissionAmount;
  }
}
