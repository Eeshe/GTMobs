package me.eeshe.gtmobs.util;

import org.bukkit.Material;

public class ParseUtil {

  /**
   * Parses the passed material name into its Material instance.
   *
   * @param materialName    Name that will be parsed.
   * @param defaultMaterial Material to be returned in case the original couldn't
   *                        be found.
   * @return Material corresponding to the passed Material name.
   */
  public static Material parseMaterial(String materialName, Material defaultMaterial) {
    Material material = Material.matchMaterial(materialName);
    if (material == null)
      return defaultMaterial;

    return material;
  }
}
