package me.eeshe.gtmobs.models.mobactions;

public enum MobActionTarget {
  SELF,
  ATTACKER,
  TARGET;

  /**
   * Searches and returns the MobActionTarget corresponding to the passed name.
   *
   * @param name Name that will be searched.
   * @return MobActionTarget corresponding to the passed name.
   */
  public static MobActionTarget fromName(String name) {
    try {
      return MobActionTarget.valueOf(name.toUpperCase().trim());
    } catch (Exception e) {
      return null;
    }
  }
}
