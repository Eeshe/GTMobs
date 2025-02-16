package me.eeshe.gtmobs.files.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.eeshe.gtmobs.GTMobs;
import me.eeshe.gtmobs.models.GTMob;
import me.eeshe.gtmobs.models.config.ConfigParticle;
import me.eeshe.gtmobs.models.config.ConfigSound;
import me.eeshe.gtmobs.models.config.IntRange;
import me.eeshe.gtmobs.models.mobactions.ConsoleCommandMobAction;
import me.eeshe.gtmobs.models.mobactions.EffectMobAction;
import me.eeshe.gtmobs.models.mobactions.MobAction;
import me.eeshe.gtmobs.models.mobactions.MobActionTarget;
import me.eeshe.gtmobs.models.mobactions.MobActionType;
import me.eeshe.gtmobs.models.mobactions.SpawnMobAction;
import me.eeshe.gtmobs.models.mobactions.SuicideMobAction;
import me.eeshe.gtmobs.util.ConfigUtil;
import me.eeshe.gtmobs.util.LogUtil;

public class MobConfig extends ConfigWrapper {

  public MobConfig(GTMobs plugin) {
    super(plugin, null, "mobs.yml");
  }

  @Override
  public void writeDefaults() {
    writeDefaultGTMobs();

    getConfig().options().copyDefaults(true);
    saveConfig();
    reloadConfig();
  }

  /**
   * Writes the default GTMobs to the config file.
   */
  private void writeDefaultGTMobs() {
    for (GTMob gtMob : List.of(
        new GTMob(
            "zombie1",
            EntityType.ZOMBIE,
            "&eGTZombie",
            Map.of(Attribute.GENERIC_MOVEMENT_SPEED, 1D),
            new ConfigSound(Sound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, true, 1.0F, 0.5F),
            List.of(
                new ConfigParticle(true, Particle.SLIME, 10, 0.1, 0.1, 0.1, 0.05, null)),
            List.of(
                new ConfigParticle(true, Particle.SMOKE_NORMAL, 10, 0.1, 0.1, 0.1, 0.05, null)),
            new IntRange(1, 5),
            List.of(
                new EffectMobAction(0.5, MobActionTarget.ATTACKER,
                    new PotionEffect(PotionEffectType.CONFUSION, 50, 0, false, true))),
            List.of(
                new EffectMobAction(0.5, MobActionTarget.ATTACKER,
                    new PotionEffect(PotionEffectType.CONFUSION, 50, 0, false, true))),
            List.of(
                new EffectMobAction(0.5, MobActionTarget.ATTACKER,
                    new PotionEffect(PotionEffectType.POISON, 50, 0, false, true)))),
        new GTMob(
            "skeleton1",
            EntityType.SKELETON,
            "&3GTSkeleton",
            Map.of(Attribute.GENERIC_MAX_HEALTH, 200D),
            new ConfigSound(Sound.ENTITY_EVOCATION_ILLAGER_CAST_SPELL, true, 1.0F, 0.5F),
            List.of(
                new ConfigParticle(true, Particle.SLIME, 10, 0.1, 0.1, 0.1, 0.05, null),
                new ConfigParticle(true, Particle.FLAME, 10, 0.1, 0.1, 0.1, 0.05, null)),
            List.of(
                new ConfigParticle(true, Particle.SMOKE_NORMAL, 10, 0.1, 0.1, 0.1, 0.05, null),
                new ConfigParticle(true, Particle.EXPLOSION_HUGE, 10, 0.1, 0.1, 0.1, 0.05, null)),
            new IntRange(10, 50),
            List.of(
                new EffectMobAction(0.5, MobActionTarget.SELF,
                    new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 50, 0, false, true))),
            List.of(
                new EffectMobAction(0.5, MobActionTarget.ATTACKER,
                    new PotionEffect(PotionEffectType.HUNGER, 50, 0, false, true))),
            List.of(
                new EffectMobAction(0.5, MobActionTarget.TARGET,
                    new PotionEffect(PotionEffectType.WITHER, 50, 0, false, true)))))) {
      writeDefaultGTMob(gtMob);
    }
  }

  /**
   * Writes the passed GTMob's to the config file.
   *
   * @param gtMob GTMob to write.
   */
  private void writeDefaultGTMob(GTMob gtMob) {
    FileConfiguration config = getConfig();
    String path = gtMob.getId();
    if (config.contains(path)) {
      return;
    }
    config.addDefault(path + ".entity-type", gtMob.getEntityType().name());
    config.addDefault(path + ".display-name", gtMob.getDisplayName());
    ConfigUtil.writeAttributeMap(config, path + ".attributes", gtMob.getAttributes());
    ConfigUtil.writeConfigSound(config, path + ".spawn-sound", gtMob.getSpawnSound());
    ConfigUtil.writeConfigParticles(config, path + ".hit-particles", gtMob.getOnHitParticles());
    ConfigUtil.writeConfigParticles(config, path + ".death-particles", gtMob.getOnDeathParticles());
    ConfigUtil.writeIntRange(config, path + ".experience-drop", gtMob.getExperienceDrop());
    writeDefaultMobActions(path + ".hit-actions", gtMob.getOnHitActions());
    writeDefaultMobActions(path + ".shot-actions", gtMob.getOnShotActions());
    writeDefaultMobActions(path + ".death-actions", gtMob.getOnDeathActions());
  }

  /**
   * Writes the passed list of MobActions to the config.
   *
   * @param path       Path to write in.
   * @param mobActions MobActions to write.
   */
  private void writeDefaultMobActions(String path, List<MobAction> mobActions) {
    int counter = 1;
    for (MobAction mobAction : mobActions) {
      writeDefaultMobAction(path + "." + counter, mobAction);
      counter += 1;
    }
  }

  /**
   * Writes the passed MobAction to the config file.
   *
   * @param path      Path to write in.
   * @param mobAction MobAction to write.
   */
  private void writeDefaultMobAction(String path, MobAction mobAction) {
    FileConfiguration config = getConfig();

    config.addDefault(path + ".type", mobAction.getMobActionType().name());
    config.addDefault(path + ".chance", mobAction.getChance());
    config.addDefault(path + ".target", mobAction.getActionTarget().name());
    switch (mobAction.getMobActionType()) {
      case CONSOLE_COMMAND:
        writeDefaultConsoleCommandMobAction(path, (ConsoleCommandMobAction) mobAction);
        break;
      case EFFECT:
        writeDefaultEffectMobAction(path, (EffectMobAction) mobAction);
        break;
      case SPAWN:
        writeDefaultSpawnMobAction(path, (SpawnMobAction) mobAction);
        break;
      default:
    }
  }

  /**
   * Writes the passed ConsoleCommandMobAction.
   *
   * @param path                    Path to write in.
   * @param consoleCommandMobAction ConsoleCommandMobAction to write.
   */
  private void writeDefaultConsoleCommandMobAction(String path, ConsoleCommandMobAction consoleCommandMobAction) {
    FileConfiguration config = getConfig();

    config.addDefault(path + ".commands", consoleCommandMobAction.getCommands());
    config.addDefault(path + ".delay-ticks", consoleCommandMobAction.getDelayTicks());
  }

  /**
   * Writes the passed EffectMobAction.
   *
   * @param path            Path to write in.
   * @param effectMobAction EffectMobAction to write.
   */
  private void writeDefaultEffectMobAction(String path, EffectMobAction effectMobAction) {
    FileConfiguration config = getConfig();

    PotionEffect effect = effectMobAction.getPotionEffect();
    config.addDefault(path + ".effect", effect.getType().getName());
    config.addDefault(path + ".amplifier", effect.getAmplifier() + 1);
    config.addDefault(path + ".duration-ticks", effect.getDuration());
    config.addDefault(path + ".particles", effect.hasParticles());
    config.addDefault(path + ".ambient", effect.isAmbient());
  }

  /**
   * Writes the passed SpawnMobAction.
   *
   * @param path           Path to write in.
   * @param spawnMobAction SpawnMobAction to write.
   */
  private void writeDefaultSpawnMobAction(String path, SpawnMobAction spawnMobAction) {
    FileConfiguration config = getConfig();

    config.addDefault(path + ".mob-id", spawnMobAction.getMobActionType());
    ConfigUtil.writeIntRange(config, path + ".amount", spawnMobAction.getAmountRange());
    config.addDefault(path + ".radius", spawnMobAction.getRadius());
    ConfigUtil.writeIntRange(config, path + ".delay-ticks", spawnMobAction.getDelayTicks());
  }

  /**
   * Fetches and returns the GTMobs configured in the config file.
   *
   * @return Configured GTMobs.
   */
  public List<GTMob> fetchGTMobs() {
    List<GTMob> gtMobs = new ArrayList<>();
    for (String mobId : getConfig().getKeys(false)) {
      GTMob gtMob = fetchGTMob(mobId);
      if (gtMob == null) {
        continue;
      }
      gtMobs.add(gtMob);
    }
    return gtMobs;
  }

  public GTMob fetchGTMob(String id) {
    FileConfiguration config = getConfig();
    ConfigurationSection mobSection = config.getConfigurationSection(id);
    if (mobSection == null) {
      return null;
    }

    String entityTypeName = mobSection.getString("entity-type", "");
    EntityType entityType;
    try {
      entityType = EntityType.valueOf(entityTypeName);
    } catch (Exception e) {
      LogUtil.sendWarnLog("Unknown Entity Type '" + entityTypeName + "' configured for mob '" + id + "'.");
      return null;
    }
    String displayName = mobSection.getString("display-name", "");
    Map<Attribute, Double> attributes = ConfigUtil.fetchAttributeMap(config, id + ".attributes");
    ConfigSound spawnSound = ConfigUtil.fetchConfigSound(config, id + ".spawn-sound");
    List<ConfigParticle> onHitParticles = ConfigUtil.fetchConfigParticles(config, id + ".hit-particles");
    List<ConfigParticle> onDeathParticles = ConfigUtil.fetchConfigParticles(config, id + ".death-particles");
    IntRange experienceDrop = ConfigUtil.fetchIntRange(config, id + ".experience-drop");
    List<MobAction> onHitActions = fetchMobActions(id + ".hit-actions");
    List<MobAction> onShotActions = fetchMobActions(id + ".shot-actions");
    List<MobAction> onDeathActions = fetchMobActions(id + ".death-actions");

    return new GTMob(id, entityType, displayName, attributes, spawnSound, onHitParticles, onDeathParticles,
        experienceDrop, onHitActions, onShotActions, onDeathActions);
  }

  private List<MobAction> fetchMobActions(String path) {
    List<MobAction> mobActions = new ArrayList<>();
    ConfigurationSection mobActionSection = getConfig().getConfigurationSection(path);
    if (mobActionSection == null) {
      return mobActions;
    }
    for (String key : mobActionSection.getKeys(false)) {
      MobAction mobAction = fetchMobAction(path + "." + key);
      if (mobAction == null) {
        continue;
      }
      mobActions.add(mobAction);
    }
    return mobActions;
  }

  /**
   * Fetches the MobAction configured in the passed path.
   *
   * @param path Path to search in.
   * @return Configured MobAction.
   */
  private MobAction fetchMobAction(String path) {
    ConfigurationSection mobActionSection = getConfig().getConfigurationSection(path);
    if (mobActionSection == null) {
      return null;
    }
    double chance = mobActionSection.getDouble("chance");
    String mobActionTargetName = mobActionSection.getString("target", "");
    MobActionTarget mobActionTarget = MobActionTarget.fromName(mobActionTargetName);
    if (mobActionTarget == null) {
      LogUtil.sendWarnLog("Unknown Mob Action Target '" + mobActionTargetName + "' configured in '" + path + "'.");
      return null;
    }
    String mobActionTypeName = mobActionSection.getString("type", "");
    MobActionType mobActionType = MobActionType.fromName(mobActionTypeName);
    if (mobActionTypeName == null) {
      LogUtil.sendWarnLog("Unknown Mob Action Type '" + mobActionTargetName + "' configured in '" + path + "'.");
      return null;
    }
    IntRange delayTicks;
    switch (mobActionType) {
      case CONSOLE_COMMAND:
        List<String> commands = mobActionSection.getStringList("commands");
        delayTicks = ConfigUtil.fetchIntRange(getConfig(), path + ".delay-ticks");

        return new ConsoleCommandMobAction(chance, commands, delayTicks);
      case EFFECT:
        String potionEffectTypeName = mobActionSection.getString("effect", "");
        PotionEffectType potionEffectType;
        try {
          potionEffectType = PotionEffectType.getByName(potionEffectTypeName);
        } catch (Exception e) {
          LogUtil
              .sendWarnLog("Unknown Potion Effect Type '" + potionEffectTypeName + "' configured in '" + path + "'.");
          return null;
        }
        int amplifier = mobActionSection.getInt("amplifier") - 1;
        int durationTicks = mobActionSection.getInt("duration-ticks");
        boolean particles = mobActionSection.getBoolean("particles");
        boolean ambient = mobActionSection.getBoolean("ambient");

        return new EffectMobAction(chance, mobActionTarget,
            new PotionEffect(potionEffectType, durationTicks, amplifier, ambient, particles));
      case SPAWN:
        String mobId = mobActionSection.getString("mob-id", "");
        IntRange amountRange = ConfigUtil.fetchIntRange(getConfig(), path + ".amount");
        double radius = mobActionSection.getDouble("radius");
        delayTicks = ConfigUtil.fetchIntRange(getConfig(), path + ".delay-ticks");

        return new SpawnMobAction(chance, mobId, amountRange, radius, delayTicks);
      case SUICIDE:
        return new SuicideMobAction(chance, mobActionTarget);
      default:
        return null;
    }
  }
}
