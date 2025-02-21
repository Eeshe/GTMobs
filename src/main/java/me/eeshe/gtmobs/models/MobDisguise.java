package me.eeshe.gtmobs.models;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class MobDisguise {

  private final boolean enabled;
  private final List<String> skinNames;
  private final ItemStack skinItem;

  public MobDisguise(boolean enabled, List<String> skinNames, ItemStack skinItem) {
    this.enabled = enabled;
    this.skinNames = skinNames;
    this.skinItem = skinItem;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public List<String> getSkinNames() {
    return skinNames;
  }

  public ItemStack getSkinItem() {
    return skinItem;
  }
}
