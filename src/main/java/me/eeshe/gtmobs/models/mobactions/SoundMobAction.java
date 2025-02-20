package me.eeshe.gtmobs.models.mobactions;

import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import me.eeshe.gtmobs.models.config.ConfigSound;

public class SoundMobAction extends MobAction {
  private final ConfigSound configSound;

  public SoundMobAction(ConfigSound configSound) {
    this.configSound = configSound;
  }

  @Override
  public void execute(LivingEntity gtMobEntity, Entity attacker) {
    configSound.play(gtMobEntity.getLocation());
  }

  @Override
  public String toString() {
    return MobActionType.SOUND.name() + ":" + configSound.toString();
  }
}
