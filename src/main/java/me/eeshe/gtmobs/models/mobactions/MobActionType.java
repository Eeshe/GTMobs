package me.eeshe.gtmobs.models.mobactions;

public enum MobActionType {
  CONSOLE_COMMAND,
  EFFECT,
  SPAWN,
  SUICIDE;

  /**
   * Searches and returns the MobActionType corresponding to the passed name.
   *
   * @param name Name that will be searched.
   * @return MobActionType corresponding to the passed name.
   */
  public static MobActionType fromName(String name) {
    try {
      return MobActionType.valueOf(name.toUpperCase());
    } catch (Exception e) {
      return null;
    }
  }
}
