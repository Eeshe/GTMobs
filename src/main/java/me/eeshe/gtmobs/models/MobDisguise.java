package me.eeshe.gtmobs.models;

import org.bukkit.inventory.ItemStack;

public class MobDisguise {

  private final boolean enabled;
  private final String skinName;
  private final ItemStack skinItem;

  public MobDisguise(boolean enabled, String skinName, ItemStack skinItem) {
    this.enabled = enabled;
    this.skinName = skinName;
    this.skinItem = skinItem;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public String getSkinName() {
    return skinName;
  }

  public ItemStack getSkinItem() {
    return skinItem;
  }
}
